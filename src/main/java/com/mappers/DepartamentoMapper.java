package com.mappers;

import com.domains.Secretaria;
import com.domains.Departamento;
import com.domains.dtos.DepartamentoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper manual (sem frameworks) para Departamento.
 * - Entity → DTO: enum Status vira int (0/1) e Secretaria vira secretariaId.
 * - DTO → Entity: int (0/1) vira enum Status; secretariaId vira Secretaria (via resolver).
 * - NÃO seta valorEstoque na Entity (é calculado no domínio).
 */
public class DepartamentoMapper {

    private DepartamentoMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static DepartamentoDTO toDto(Departamento e) {
        if (e == null) return null;

        // idDepartamento (Long) -> Long do DTO
        Long idDto = e.getId();

        Integer secretariaId = Math.toIntExact((e.getSecretaria() == null) ? null : e.getSecretaria().getId());

        return new DepartamentoDTO(
                idDto,
                e.getNome(),
                secretariaId
        );
    }

    /** Converte uma coleção de Entities em lista de DTOs. */
    public static List<DepartamentoDTO> toDtoList(Collection<Departamento> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(DepartamentoMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Converte Page<Entity> em Page<DTO> preservando a paginação. */
    public static Page<DepartamentoDTO> toDtoPage(Page<Departamento> page) {
        List<DepartamentoDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    /* ======================= DTO -> Entity ======================= */

    /**
     * Cria uma nova Entity a partir do DTO, usando o Secretaria já carregado.
     */
    public static Departamento toEntity(DepartamentoDTO dto, Secretaria secretaria) {
        if (dto == null) return null;

        Departamento e = new Departamento();

        // idDepartamento do DTO (Long) -> Long da Entity
        e.setId(dto.getId());

        e.setNome(trim(dto.getNome()));
        e.setSecretaria(secretaria); // pode ser null se DTO não trouxer secretaria

        return e;
    }

    /**
     * Cria uma nova Entity a partir do DTO, resolvendo o Secretaria via função (repo).
     * Ex.: toEntity(dto, secretariaRepo::getReferenceById) ou findById(...).orElseThrow(...)
     */
    public static Departamento toEntity(DepartamentoDTO dto, Function<Integer, Secretaria> secretariaResolver) {
        if (dto == null) return null;
        Secretaria secretaria = (dto.getSecretariaId() == null) ? null : secretariaResolver.apply(dto.getSecretariaId());
        return toEntity(dto, secretaria);
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * usando o Secretaria já carregado. Não altera o id do target.
     */
    public static void copyToEntity(DepartamentoDTO dto, Departamento target, Secretaria secretaria) {
        if (dto == null || target == null) return;

        target.setNome(trim(dto.getNome()));
        target.setSecretaria(secretaria);
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * resolvendo o Secretaria via função. Não altera o id do target.
     */
    public static void copyToEntity(DepartamentoDTO dto, Departamento target, Function<Integer, Secretaria> secretariaResolver) {
        if (dto == null || target == null) return;
        Secretaria secretaria = (dto.getSecretariaId() == null) ? null : secretariaResolver.apply(dto.getSecretariaId());
        copyToEntity(dto, target, secretaria);
    }

    /* ======================= Helpers ======================= */

    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }

}