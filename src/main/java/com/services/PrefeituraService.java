package com.services;

import com.domains.Prefeitura;
import com.domains.Secretaria;
import com.domains.dtos.PrefeituraDTO;
import com.mappers.PrefeituraMapper;
import com.repositories.PrefeituraRepository;
import com.services.exceptions.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.hibernate.dialect.SybaseASEDialect.MAX_PAGE_SIZE;

@Service
public class PrefeituraService {

    private final PrefeituraRepository prefeituraRepo;

    // Injeção por construtor (Spring injeta automaticamente se houver só um construtor público)
    public PrefeituraService(PrefeituraRepository prefeituraRepo) {
        this.prefeituraRepo = prefeituraRepo;
    }

    @Transactional(readOnly = true)
    public List<PrefeituraDTO> findAll(){
        //retorna uma lista de ProdutoDTO
        return PrefeituraMapper.toDtoList(prefeituraRepo.findAll());
    }

    @Transactional(readOnly = true)
    public Page<PrefeituraDTO> findByNome(String nome, Pageable pageable) {

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

        Page<Prefeitura> page=Page.empty();

        if (nome != null) {
            page = prefeituraRepo.findByNome(nome, effective);
            if(page.isEmpty()){
                throw new ObjectNotFoundException("Nenhum prefeitura encontrada com o nome: " + nome);
            }
        }

        return PrefeituraMapper.toDtoPage(page);
    }

    /**
     * Busca lista completa (sem paginação) com filtros
     * Reutiliza a lógica acima passando Pageable.unpaged()
     */
    @Transactional(readOnly = true)
    public List<PrefeituraDTO> findByNome(String nome) {
        return findByNome(nome, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public PrefeituraDTO findById(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id é obrigatório");
        }

        return prefeituraRepo.findById(Long.valueOf(id))
                .map(PrefeituraMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Prefeitura não encontrada: id=" + id));
    }

    //Create
    @Transactional
    public PrefeituraDTO create(PrefeituraDTO prefeituraDTO) {


        if (prefeituraDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da prefeitura são obrigatórios");
        }

        prefeituraDTO.setId(null);
        Prefeitura prefeitura;
        try{
            prefeitura = PrefeituraMapper.toEntity(prefeituraDTO);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return PrefeituraMapper.toDto(prefeituraRepo.save(prefeitura));
    }

    //Update
    @Transactional
    public PrefeituraDTO update(Long id, PrefeituraDTO prefeituraDTO) {

        if (prefeituraDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do prefeitura são obrigatórios");
        }

        Prefeitura prefeitura = prefeituraRepo.findById(Long.valueOf(prefeituraDTO.getId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Prefeitura não encontrado: id=" + id));

        prefeituraDTO.setId(id);
        try{
            prefeitura = PrefeituraMapper.toEntity(prefeituraDTO);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return PrefeituraMapper.toDto(prefeituraRepo.save(prefeitura));
    }

    //Delete
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");
        }

        Prefeitura prefeitura = prefeituraRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Prefeitura não encontrada: id=" + id));

        prefeituraRepo.delete(prefeitura);
    }

}
