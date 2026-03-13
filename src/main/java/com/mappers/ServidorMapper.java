package com.mappers;

import com.domains.Departamento;
import com.domains.Servidor;
import com.domains.dtos.ServidorDTO;
import com.domains.enums.Provimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper manual (sem frameworks) para Servidor.
 * - Entity -> DTO: enum Provimento vira int (0/1) e Departamento vira departamentoId.
 * - DTO -> Entity: int (0/1) vira enum Provimento; departamentoId vira Departamento (via resolver).
 * - NÃO seta valorEstoque na Entity (é calculado no domínio).
 */
public class ServidorMapper {

    private ServidorMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static ServidorDTO toDto(Servidor e) {
        if (e == null) return null;

        // idServidor (Long) -> Long do DTO
        Long idDto = e.getId();

        Integer departamentoId = Math.toIntExact((e.getDepartamento() == null) ? null : e.getDepartamento().getId());
        int provimentoInt = (e.getProvimento() == null) ? 0 : e.getProvimento().getId();

        return new ServidorDTO(
                idDto,
                e.getNome(),
                e.getCPF(),
                e.getSalario(),
                e.getMatricula(),
                departamentoId,
                provimentoInt
        );
    }

    /** Converte uma coleção de Entities em lista de DTOs. */
    public static List<ServidorDTO> toDtoList(Collection<Servidor> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(ServidorMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Converte Page<Entity> em Page<DTO> preservando a paginação. */
    public static Page<ServidorDTO> toDtoPage(Page<Servidor> page) {
        List<ServidorDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    /* ======================= DTO -> Entity ======================= */

    /**
     * Cria uma nova Entity a partir do DTO, usando o Departamento já carregado.
     * Não seta valorEstoque (é calculado na Entity/serviço).
     */
    public static Servidor toEntity(ServidorDTO dto, Departamento departamento) {
        if (dto == null) return null;

        Servidor e = new Servidor();

        // idServidor do DTO (Long) -> Long da Entity
        e.setId(dto.getId());

        e.setNome(trim(dto.getNome()));
        e.setCPF(dto.getCPF());
        e.setSalario(dto.getSalario());
        e.setMatricula(dto.getMatricula());
        e.setDepartamento(departamento); // pode ser null se DTO não trouxer departamento
        e.setProvimento(Provimento.toEnum(dto.getProvimento())); // int -> enum

        return e;
    }

    /**
     * Cria uma nova Entity a partir do DTO, resolvendo o Departamento via função (repo).
     * Ex.: toEntity(dto, departamentoRepo::getReferenceById) ou findById(...).orElseThrow(...)
     */
    public static Servidor toEntity(ServidorDTO dto, Function<Integer, Departamento> departamentoResolver) {
        if (dto == null) return null;
        Departamento departamento = (dto.getDepartamentoId() == null) ? null : departamentoResolver.apply(dto.getDepartamentoId());
        return toEntity(dto, departamento);
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * usando o Departamento já carregado. Não altera o id do target.
     * NÃO seta valorEstoque (é calculado no domínio).
     */
    public static void copyToEntity(ServidorDTO dto, Servidor target, Departamento departamento) {
        if (dto == null || target == null) return;

        target.setNome(trim(dto.getNome()));
        target.setCPF(trim(dto.getCPF()));
        target.setSalario(dto.getSalario());
        target.setMatricula(trim(dto.getMatricula()));
        target.setDepartamento(departamento);
        target.setProvimento(Provimento.toEnum(dto.getProvimento()));
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * resolvendo o Departamento via função. Não altera o id do target.
     */
    public static void copyToEntity(ServidorDTO dto, Servidor target, Function<Integer, Departamento> departamentoResolver) {
        if (dto == null || target == null) return;
        Departamento departamento = (dto.getDepartamentoId() == null) ? null : departamentoResolver.apply(dto.getDepartamentoId());
        copyToEntity(dto, target, departamento);
    }

    /* ======================= Helpers ======================= */

    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }

}