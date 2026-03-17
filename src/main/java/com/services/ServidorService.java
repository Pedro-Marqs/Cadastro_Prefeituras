package com.services;

import com.domains.Departamento;
import com.domains.Servidor;
import com.domains.dtos.ServidorDTO;
import com.domains.enums.Provimento;
import com.mappers.ServidorMapper;
import com.repositories.DepartamentoRepository;
import com.repositories.ServidorRepository;
import com.services.exceptions.ObjectNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServidorService {

    private static final int MAX_PAGE_SIZE = 200;

    private final ServidorRepository servidorRepo;
    private final DepartamentoRepository departamentoRepo;

    public ServidorService(ServidorRepository servidorRepo,
                                 DepartamentoRepository departamentoRepo) {
        this.servidorRepo = servidorRepo;
        this.departamentoRepo = departamentoRepo;
    }



    @Transactional(readOnly = true)
    public List<ServidorDTO> findAll() {
        return ServidorMapper.toDtoList(servidorRepo.findAll());
    }

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

    @Transactional(readOnly = true)
    public Page<ServidorDTO> findAllByDepartamento(Integer departamentoId, Pageable pageable) {
        if (departamentoId == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "departamentoId é obrigatório");

        if (!departamentoRepo.existsById(departamentoId.longValue()))
            throw new ObjectNotFoundException("Departamento não encontrado: id=" + departamentoId);

        final Pageable effective =
                (pageable == null || pageable.isUnpaged())
                        ? Pageable.unpaged()
                        : PageRequest.of(
                        Math.max(0, pageable.getPageNumber()),
                        Math.min(pageable.getPageSize(), MAX_PAGE_SIZE),
                        pageable.getSort()
                );

        Page<Servidor> page =
                servidorRepo.findByDepartamento_Id(departamentoId, effective);

        return ServidorMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public List<ServidorDTO> findAllByDepartamento(Integer departamentoId) {
        return findAllByDepartamento(departamentoId, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public Page<ServidorDTO> findAllByNome(String nome, Pageable pageable) {
        if (nome == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nome é obrigatório");


        final Pageable effective =
                (pageable == null || pageable.isUnpaged())
                        ? Pageable.unpaged()
                        : PageRequest.of(
                        Math.max(0, pageable.getPageNumber()),
                        Math.min(pageable.getPageSize(), MAX_PAGE_SIZE),
                        pageable.getSort()
                );

        Page<Servidor> page =
                servidorRepo.findByNome(nome, effective);

        if(page.getTotalPages() == 0)
            throw new ObjectNotFoundException("Servidor " + nome + " não encontrado");


        return ServidorMapper.toDtoPage(page);
    }

    @Transactional(readOnly = true)
    public List<ServidorDTO> findAllByNome(String nome) {
        return findAllByNome(nome, Pageable.unpaged()).getContent();
    }

    @Transactional(readOnly = true)
    public ServidorDTO findById(Long id) {
        if (id == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "id é obrigatório");

        return servidorRepo.findById(id)
                .map(ServidorMapper::toDto)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Servidor não encontrado: id=" + id));
    }

    @Transactional
    public ServidorDTO create(ServidorDTO dto) {
        if (dto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados são obrigatórios");

        if (dto.getDepartamentoId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do departamento é obrigatório");

        Departamento departamento = departamentoRepo.findById(dto.getDepartamentoId().longValue())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Departamento não encontrado: id=" + dto.getDepartamentoId()));

        dto.setId(null);
        Servidor entidade = ServidorMapper.toEntity(dto, departamento);

        return ServidorMapper.toDto(servidorRepo.save(entidade));
    }

    @Transactional
    public ServidorDTO update(Long id, ServidorDTO dto) {
        if (dto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados são obrigatórios");

        if (dto.getDepartamentoId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id do departamento é obrigatório");

        servidorRepo.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Servidor não encontrado: id=" + id));

        Departamento departamento = departamentoRepo.findById(dto.getDepartamentoId().longValue())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Departamento não encontrado: id=" + dto.getDepartamentoId()));

        dto.setId(id);
        Servidor entidade = ServidorMapper.toEntity(dto, departamento);

        return ServidorMapper.toDto(servidorRepo.save(entidade));
    }

    @Transactional
    public void delete(Long id) {
        if (id == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id é obrigatório");

        Servidor mov = servidorRepo.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Servidor não encontrado: id=" + id));

        servidorRepo.delete(mov);
    }

    @Transactional(readOnly = true)
    public List<ServidorDTO> listarServidoresEfetivos() {
        List<Servidor> lista =
                servidorRepo.findByProvimento(Provimento.EFETIVO);

        return ServidorMapper.toDtoList(lista);
    }


    @Transactional(readOnly = true)
    public List<ServidorDTO> listarServidoresComissionados() {
        List<Servidor> lista =
                servidorRepo.findByProvimento(Provimento.COMISSIONADO);

        return ServidorMapper.toDtoList(lista);
    }
}