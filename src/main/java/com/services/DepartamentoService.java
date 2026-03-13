package com.services;

import com.domains.*;
import com.domains.dtos.DepartamentoDTO;
import com.domains.dtos.DepartamentoDTO;
import com.mappers.DepartamentoMapper;
import com.mappers.DepartamentoMapper;
import com.repositories.SecretariaRepository;
import com.repositories.DepartamentoRepository;
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
public class DepartamentoService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final DepartamentoRepository departamentoRepo;
    private final SecretariaRepository secretariaRepo;

    public DepartamentoService(DepartamentoRepository departamentoRepo,
                             SecretariaRepository secretariaRepo) {
        this.departamentoRepo = departamentoRepo;
        this.secretariaRepo = secretariaRepo;
    }

    /* =================== READ =================== */

    /** Não Paginado */
    @Transactional(readOnly = true)
    public List<DepartamentoDTO> findAll(){
        //retorna uma lista de DepartamentoDTO
        return DepartamentoMapper.toDtoList(departamentoRepo.findAll());
    }

    /** Paginado */
    @Transactional(readOnly = true)
    public Page<DepartamentoDTO> findAll(Pageable pageable) {
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

        Page<Departamento> page = departamentoRepo.findAll(effective);
        return DepartamentoMapper.toDtoPage(page);
    }

    /** Paginado, filtrando por secretaria */
    @Transactional(readOnly = true)
    public Page<DepartamentoDTO> findAllBySecretaria(Long secretariaId, Pageable pageable) {
        if (secretariaId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "secretariaId é obrigatório");
        }

        // valida existência da secretaria para erro claro
        if (!secretariaRepo.existsById(Long.valueOf(secretariaId))) {
            throw new ObjectNotFoundException("Secretaria não encontrado: id=" + secretariaId);
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

        Page<Departamento> page = departamentoRepo.findBySecretaria_Id(secretariaId, effective);
        return DepartamentoMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por secretaria (reaproveita o paginado com unpaged) */
    @Transactional(readOnly = true)
    public List<DepartamentoDTO> findAllBySecretaria(Long secretariaId) {
        return findAllBySecretaria(secretariaId, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public Page<DepartamentoDTO> findByNome(String nome, Pageable pageable) {

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

        Page<Departamento> page=Page.empty();

        if (nome != null) {
            page = departamentoRepo.findByNome(nome, effective);
            if(page.isEmpty()){
                throw new ObjectNotFoundException("Nenhum departamento encontrada com o nome: " + nome);
            }
        }

        return DepartamentoMapper.toDtoPage(page);
    }

    /**
     * Busca lista completa (sem paginação) com filtros
     * Reutiliza a lógica acima passando Pageable.unpaged()
     */
    @Transactional(readOnly = true)
    public List<DepartamentoDTO> findByNome(String nome) {
        return findByNome(nome, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public DepartamentoDTO findById(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id é obrigatório");
        }

        return departamentoRepo.findById(Long.valueOf(id))
                .map(DepartamentoMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Departamento não encontrada: id=" + id));
    }

    //Create
    @Transactional
    public DepartamentoDTO create(DepartamentoDTO departamentoDTO) {


        if (departamentoDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da departamento são obrigatórios");
        }

        if (departamentoDTO.getSecretariaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Secretaria é obrigatório");
        }

        Secretaria secretaria = secretariaRepo.findById(Long.valueOf(departamentoDTO.getSecretariaId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Secretaria não encontrado: id=" + departamentoDTO.getSecretariaId())
                );

        departamentoDTO.setId(null);
        Departamento departamento;
        try{
            departamento = DepartamentoMapper.toEntity(departamentoDTO, secretaria);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return DepartamentoMapper.toDto(departamentoRepo.save(departamento));
    }

    //Update
    @Transactional
    public DepartamentoDTO update(Long id, DepartamentoDTO departamentoDTO) {

        if (departamentoDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do departamento são obrigatórios");
        }

        if (departamentoDTO.getSecretariaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id da Secretaria é obrigatório");
        }

        Secretaria secretaria = secretariaRepo.findById(Long.valueOf(departamentoDTO.getSecretariaId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Secretaria não encontrado: id=" + departamentoDTO.getSecretariaId())
                );

        Departamento departamento = departamentoRepo.findById(Long.valueOf(departamentoDTO.getId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Departamento não encontrado: id=" + id));

        departamentoDTO.setId(id);
        try{
            departamento = DepartamentoMapper.toEntity(departamentoDTO, secretaria);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return DepartamentoMapper.toDto(departamentoRepo.save(departamento));
    }

    //Delete
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");
        }

        Departamento departamento = departamentoRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Departamento não encontrada: id=" + id));

        departamentoRepo.delete(departamento);
    }

}