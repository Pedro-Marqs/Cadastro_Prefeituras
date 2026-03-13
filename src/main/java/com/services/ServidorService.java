package com.services;

import com.domains.Servidor;
import com.domains.Departamento;
import com.domains.dtos.ServidorDTO;
import com.domains.enums.Provimento;
import com.mappers.ServidorMapper;
import com.repositories.ServidorRepository;
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

@Service
public class ServidorService {

    private static final int MAX_PAGE_SIZE = 200; // limite de segurança

    private final ServidorRepository servidorRepo;
    private final DepartamentoRepository departamentoRepo;

    public ServidorService(ServidorRepository servidorRepo,
                              DepartamentoRepository departamentoRepo) {
        this.servidorRepo = servidorRepo;
        this.departamentoRepo = departamentoRepo;
    }


    private Provimento provimentoFromEfetivo(Boolean efetivo) {
        Provimento[] valores = Provimento.values();
        if (valores.length < 2) {
            throw new IllegalStateException("Enum Provimento precisa ter pelo menos 2 valores para mapear EFETIVO/COMISSIONADO.");
        }
        return Boolean.TRUE.equals(efetivo) ? valores[0] : valores[1];
    }


    private Provimento provimentoComissionado() {
        Provimento[] valores = Provimento.values();
        if (valores.length < 2) {
            throw new IllegalStateException("Enum Provimento precisa ter pelo menos 2 valores para mapear COMISSIONADO.");
        }
        return valores[1];
    }


    /** Não paginado, sem filtro */
    @Transactional(readOnly = true)
    public List<ServidorDTO> findAll() {
        return ServidorMapper.toDtoList(servidorRepo.findAll());
    }

    /** Paginado, sem filtro */
    @Transactional(readOnly = true)
    public Page<ServidorDTO> findAll(Pageable pageable) {
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

        Page<Servidor> page = servidorRepo.findAll(effective);
        return ServidorMapper.toDtoPage(page);
    }

    /** Paginado, filtrando por usuário (se ainda usar) */
    @Transactional(readOnly = true)
    public Page<ServidorDTO> findAllByDepartamento(Integer departamentoId, Pageable pageable) {
        if (departamentoId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "departamentoId é obrigatório");
        }

        if (!departamentoRepo.existsById(Long.valueOf(departamentoId))) {
            throw new ObjectNotFoundException("Departamento não encontrado: id=" + departamentoId);
        }

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

        Page<Servidor> page = servidorRepo.findByDepartamento_Id(departamentoId, effective);
        return ServidorMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por usuário */
    @Transactional(readOnly = true)
    public List<ServidorDTO> findAllByDepartamento(Integer departamentoId) {
        return findAllByDepartamento(departamentoId, Pageable.unpaged()).getContent();
    }

    /** Paginado, filtrando por efetivo (mapeado para Provimento via helper) */
    @Transactional(readOnly = true)
    public Page<ServidorDTO> findAllByEfetivo(Boolean efetivo, Pageable pageable) {
        if (efetivo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parâmetro 'efetivo' é obrigatório");
        }

        Provimento provimentoDesejado = provimentoFromEfetivo(efetivo);

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

        Page<Servidor> page = servidorRepo.findByProvimento(provimentoDesejado, effective);
        return ServidorMapper.toDtoPage(page);
    }

    /** Não paginado, filtrando por efetivo */
    @Transactional(readOnly = true)
    public List<ServidorDTO> findAllByEfetivo(Boolean efetivo) {
        if (efetivo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parâmetro 'efetivo' é obrigatório");
        }

        Provimento provimentoDesejado = provimentoFromEfetivo(efetivo);

        return ServidorMapper.toDtoList(
                servidorRepo.findByProvimento(provimentoDesejado)
        );
    }

    @Transactional(readOnly = true)
    public ServidorDTO findById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id do Servidor é obrigatório");
        }

        return servidorRepo.findById(id)
                .map(ServidorMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Servidor não encontrado: id=" + id));
    }


    @Transactional
    public ServidorDTO create(ServidorDTO dto) {

        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do Servidor são obrigatórios");
        }

        if (dto.getDepartamentoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do usuário é obrigatório");
        }

        Integer departamentoId = dto.getDepartamentoId();
        Departamento departamento = departamentoRepo.findById(Long.valueOf(departamentoId))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Usuário não encontrado: id=" + departamentoId)
                );

        dto.setId(null);

        if (dto.getProvimento() == null) {
            dto.setProvimento(0); // o mapper converte 0 -> Provimento.values()[0]
        }

        Servidor servidor;
        try {
            servidor = ServidorMapper.toEntity(dto, departamento);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        servidor = servidorRepo.save(servidor);
        return ServidorMapper.toDto(servidor);
    }


    @Transactional
    public ServidorDTO update(Long id, ServidorDTO dto) {

        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do Servidor são obrigatórios");
        }

        if (dto.getDepartamentoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do usuário é obrigatório");
        }

        // busca servidor existente
        Servidor existente = servidorRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Servidor não encontrado: id=" + id));

        Integer departamentoId = dto.getDepartamentoId();
        Departamento departamento = departamentoRepo.findById(Long.valueOf(departamentoId))
                .orElseThrow(() ->
                        new ObjectNotFoundException("Usuário não encontrado: id=" + departamentoId)
                );

        existente.setNome(dto.getNome());
        existente.setCPF(dto.getCPF());
        existente.setSalario(dto.getSalario());
        existente.setMatricula(dto.getMatricula());
        existente.setDepartamento(departamento);


        Servidor atualizado = servidorRepo.save(existente);
        return ServidorMapper.toDto(atualizado);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");
        }

        Servidor servidor = servidorRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Servidor não encontrado: id=" + id));


        servidor.setProvimento(provimentoComissionado());
        servidorRepo.save(servidor);
    }
}