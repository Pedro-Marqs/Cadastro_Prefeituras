package com.services;

import com.domains.*;
import com.domains.dtos.SecretariaDTO;
import com.mappers.SecretariaMapper;
import com.repositories.PrefeituraRepository;
import com.repositories.SecretariaRepository;
import com.services.exceptions.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.hibernate.dialect.SybaseASEDialect.MAX_PAGE_SIZE;

@Service
public class SecretariaService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final SecretariaRepository secretariaRepo;
    private final PrefeituraRepository prefeituraRepo;

    public SecretariaService(SecretariaRepository secretariaRepo,
                           PrefeituraRepository prefeituraRepo) {
        this.secretariaRepo = secretariaRepo;
        this.prefeituraRepo = prefeituraRepo;
    }

    /* =================== READ =================== */

    /** Não Paginado */
    @Transactional(readOnly = true)
    public List<SecretariaDTO> findAll(){
        //retorna uma lista de SecretariaDTO
        return SecretariaMapper.toDtoList(secretariaRepo.findAll());
    }

    /** Paginado */
    @Transactional(readOnly = true)
    public Page<SecretariaDTO> findAll(Pageable pageable) {
        final Pageable effective;
        if (pageable == null || pageable.isUnpaged()) {
            effective = Pageable.unpaged();
        } else {
            effective = PageRequest.of(
                    Math.max(0, pageable.getPageNumber()),
                    Math.min(pageable.getPageSize(), MAX_PAGE_SIZE),
                    pageable.getSort()
            );
        }

        Page<Secretaria> page = secretariaRepo.findAll(effective);
        return SecretariaMapper.toDtoPage(page);
    }

    /** Paginado, filtrando por prefeitura */
    @Transactional(readOnly = true)
    public Page<SecretariaDTO> findAllByPrefeitura(Long prefeituraId, Pageable pageable) {
        if (prefeituraId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "prefeituraId é obrigatório");
        }

        // valida existência da prefeitura para erro claro
        if (!prefeituraRepo.existsById(Long.valueOf(prefeituraId))) {
            throw new ObjectNotFoundException("Prefeitura não encontrado: id=" + prefeituraId);
        }

        // ✅ trate unpaged aqui
        final Pageable effective;
        if (pageable == null || pageable.isUnpaged()) {
            effective = Pageable.unpaged();
        } else {
            effective = PageRequest.of(
                    Math.max(0, pageable.getPageNumber()),
                    Math.min(pageable.getPageSize(), MAX_PAGE_SIZE),
                    pageable.getSort()
            );
        }

        Page<Secretaria> page = secretariaRepo.findByPrefeitura_Id(prefeituraId, effective);
        return SecretariaMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por prefeitura (reaproveita o paginado com unpaged) */
    @Transactional(readOnly = true)
    public List<SecretariaDTO> findAllByPrefeitura(Long prefeituraId) {
        return findAllByPrefeitura(prefeituraId, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public Page<SecretariaDTO> findByNome(String nome, Pageable pageable) {

        final Pageable effective;
        if (pageable == null || pageable.isUnpaged()) {
            effective = Pageable.unpaged();
        } else {
            effective = PageRequest.of(
                    Math.max(0, pageable.getPageNumber()),
                    Math.min(pageable.getPageSize(), MAX_PAGE_SIZE),
                    pageable.getSort()
            );
        }

        Page<Secretaria> page=Page.empty();

        if (nome != null) {
            page = secretariaRepo.findByNome(nome, effective);
            if(page.isEmpty()){
                throw new ObjectNotFoundException("Nenhuma secretaria encontrada com o nome: " + nome);
            }
        }

        return SecretariaMapper.toDtoPage(page);
    }

    /**
     * Busca lista completa (sem paginação) com filtros
     * Reutiliza a lógica acima passando Pageable.unpaged()
     */
    @Transactional(readOnly = true)
    public List<SecretariaDTO> findByNome(String nome) {
        return findByNome(nome, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public SecretariaDTO findById(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id é obrigatório");
        }

        return secretariaRepo.findById(Long.valueOf(id))
                .map(SecretariaMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Secretaria não encontrada: id=" + id));
    }

    //Create
    @Transactional
    public SecretariaDTO create(SecretariaDTO secretariaDTO) {


        if (secretariaDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da secretaria são obrigatórios");
        }

        if (secretariaDTO.getPrefeituraId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Prefeitura é obrigatório");
        }

        Prefeitura prefeitura = prefeituraRepo.findById(Long.valueOf(secretariaDTO.getPrefeituraId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Prefeitura não encontrado: id=" + secretariaDTO.getPrefeituraId())
                );

        secretariaDTO.setId(null);
        Secretaria secretaria;
        try{
            secretaria = SecretariaMapper.toEntity(secretariaDTO, prefeitura);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return SecretariaMapper.toDto(secretariaRepo.save(secretaria));
    }

    //Update
    @Transactional
    public SecretariaDTO update(Long id, SecretariaDTO secretariaDTO) {

        if (secretariaDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do secretaria são obrigatórios");
        }

        if (secretariaDTO.getPrefeituraId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Prefeitura é obrigatório");
        }

        Prefeitura prefeitura = prefeituraRepo.findById(Long.valueOf(secretariaDTO.getPrefeituraId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Prefeitura não encontrado: id=" + secretariaDTO.getPrefeituraId())
                );

        Secretaria secretaria = secretariaRepo.findById(Long.valueOf(secretariaDTO.getId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Secretaria não encontrado: id=" + id));

        secretariaDTO.setId(id);
        try{
            secretaria = SecretariaMapper.toEntity(secretariaDTO, prefeitura);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return SecretariaMapper.toDto(secretariaRepo.save(secretaria));
    }

    //Delete
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");
        }

        Secretaria secretaria = secretariaRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Secretaria não encontrada: id=" + id));

        secretariaRepo.delete(secretaria);
    }

}