package com.services;

import com.domains.*;
import com.domains.dtos.TelefoneDTO;
import com.mappers.TelefoneMapper;
import com.repositories.ServidorRepository;
import com.repositories.TelefoneRepository;
import com.services.exceptions.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TelefoneService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final TelefoneRepository telefoneRepo;
    private final ServidorRepository servidorRepo;

    public TelefoneService(TelefoneRepository telefoneRepo,
                           ServidorRepository servidorRepo) {
        this.telefoneRepo = telefoneRepo;
        this.servidorRepo = servidorRepo;
    }

    /* =================== READ =================== */

    /** Não paginado, sem filtro */
    @Transactional(readOnly = true)
    public List<TelefoneDTO> findAll() {
        return TelefoneMapper.toDtoList(telefoneRepo.findAll());
    }

    /** Paginado, sem filtro (real, no banco) */
    @Transactional(readOnly = true)
    public Page<TelefoneDTO> findAll(Pageable pageable) {
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

        Page<Telefone> page = telefoneRepo.findAll(effective);
        return TelefoneMapper.toDtoPage(page);
    }

    /** Paginado, filtrando por servidor */
    @Transactional(readOnly = true)
    public Page<TelefoneDTO> findAllByServidor(Integer servidorId, Pageable pageable) {
        if (servidorId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "servidorId é obrigatório");
        }

        // valida existência da servidor para erro claro
        if (!servidorRepo.existsById(Long.valueOf(servidorId))) {
            throw new ObjectNotFoundException("Servidor não encontrado: id=" + servidorId);
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

        Page<Telefone> page = telefoneRepo.findByServidor_Id(servidorId, effective);
        return TelefoneMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por servidor (reaproveita o paginado com unpaged) */
    @Transactional(readOnly = true)
    public List<TelefoneDTO> findAllByServidor(Integer servidorId) {
        return findAllByServidor(servidorId, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public TelefoneDTO findById(Integer id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id é obrigatório");
        }

        return telefoneRepo.findById(Long.valueOf(id))
                .map(TelefoneMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Telefone não encontrada: id=" + id));
    }

    //Create
    @Transactional
    public TelefoneDTO create(TelefoneDTO telefoneDTO) {


        if (telefoneDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados da telefone são obrigatórios");
        }

        if (telefoneDTO.getServidorId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Servidor é obrigatório");
        }

        Servidor servidor = servidorRepo.findById(Long.valueOf(telefoneDTO.getServidorId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Servidor não encontrado: id=" + telefoneDTO.getServidorId())
                );

        telefoneDTO.setId(null);
        Telefone telefone;
        try{
            telefone = TelefoneMapper.toEntity(telefoneDTO, servidor);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return TelefoneMapper.toDto(telefoneRepo.save(telefone));
    }

    //Update
    @Transactional
    public TelefoneDTO update(Long id, TelefoneDTO telefoneDTO) {

        if (telefoneDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do telefone são obrigatórios");
        }

        if (telefoneDTO.getServidorId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do Servidor é obrigatório");
        }

        Servidor servidor = servidorRepo.findById(Long.valueOf(telefoneDTO.getServidorId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Servidor não encontrado: id=" + telefoneDTO.getServidorId())
                );

        Telefone telefone = telefoneRepo.findById(Long.valueOf(telefoneDTO.getId()))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Telefone não encontrado: id=" + id));

        telefoneDTO.setId(id);
        try{
            telefone = TelefoneMapper.toEntity(telefoneDTO, servidor);
        } catch (IllegalArgumentException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return TelefoneMapper.toDto(telefoneRepo.save(telefone));
    }

    //Delete
    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");
        }

        Telefone telefone = telefoneRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Telefone não encontrada: id=" + id));

        telefoneRepo.delete(telefone);
    }

}

