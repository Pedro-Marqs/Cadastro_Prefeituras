package com.mappers;

import com.domains.Prefeitura;
import com.domains.Secretaria;
import com.domains.dtos.SecretariaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper manual (sem frameworks) para Secretaria.
 * - Entity → DTO: enum Status vira int (0/1) e Prefeitura vira prefeituraId.
 * - DTO → Entity: int (0/1) vira enum Status; prefeituraId vira Prefeitura (via resolver).
 * - NÃO seta valorEstoque na Entity (é calculado no domínio).
 */
public class SecretariaMapper {

    private SecretariaMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static SecretariaDTO toDto(Secretaria e) {
        if (e == null) return null;

        // idSecretaria (Long) -> Long do DTO
        Long idDto = e.getId();

        Integer prefeituraId = Math.toIntExact((e.getPrefeitura() == null) ? null : e.getPrefeitura().getId());

        return new SecretariaDTO(
                idDto,
                e.getNome(),
                prefeituraId
        );
    }

    /** Converte uma coleção de Entities em lista de DTOs. */
    public static List<SecretariaDTO> toDtoList(Collection<Secretaria> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(SecretariaMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Converte Page<Entity> em Page<DTO> preservando a paginação. */
    public static Page<SecretariaDTO> toDtoPage(Page<Secretaria> page) {
        List<SecretariaDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    /* ======================= DTO -> Entity ======================= */

    /**
     * Cria uma nova Entity a partir do DTO, usando o Prefeitura já carregado.
     */
    public static Secretaria toEntity(SecretariaDTO dto, Prefeitura prefeitura) {
        if (dto == null) return null;

        Secretaria e = new Secretaria();

        // idSecretaria do DTO (Long) -> Long da Entity
        e.setId(dto.getId());

        e.setNome(trim(dto.getNome()));
        e.setPrefeitura(prefeitura); // pode ser null se DTO não trouxer prefeitura

        return e;
    }

    /**
     * Cria uma nova Entity a partir do DTO, resolvendo o Prefeitura via função (repo).
     * Ex.: toEntity(dto, prefeituraRepo::getReferenceById) ou findById(...).orElseThrow(...)
     */
    public static Secretaria toEntity(SecretariaDTO dto, Function<Integer, Prefeitura> prefeituraResolver) {
        if (dto == null) return null;
        Prefeitura prefeitura = (dto.getPrefeituraId() == null) ? null : prefeituraResolver.apply(dto.getPrefeituraId());
        return toEntity(dto, prefeitura);
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * usando o Prefeitura já carregado. Não altera o id do target.
     */
    public static void copyToEntity(SecretariaDTO dto, Secretaria target, Prefeitura prefeitura) {
        if (dto == null || target == null) return;

        target.setNome(trim(dto.getNome()));
        target.setPrefeitura(prefeitura);
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * resolvendo o Prefeitura via função. Não altera o id do target.
     */
    public static void copyToEntity(SecretariaDTO dto, Secretaria target, Function<Integer, Prefeitura> prefeituraResolver) {
        if (dto == null || target == null) return;
        Prefeitura prefeitura = (dto.getPrefeituraId() == null) ? null : prefeituraResolver.apply(dto.getPrefeituraId());
        copyToEntity(dto, target, prefeitura);
    }

    /* ======================= Helpers ======================= */

    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }

}