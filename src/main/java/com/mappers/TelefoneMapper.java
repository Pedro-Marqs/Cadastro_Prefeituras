package com.mappers;

import com.domains.Servidor;
import com.domains.Telefone;
import com.domains.dtos.TelefoneDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapper manual (sem frameworks) para Telefone.
 * - Entity → DTO: enum Status vira int (0/1) e Servidor vira servidorId.
 * - DTO → Entity: int (0/1) vira enum Status; servidorId vira Servidor (via resolver).
 * - NÃO seta valorEstoque na Entity (é calculado no domínio).
 */
public class TelefoneMapper {

    private TelefoneMapper() {}

    /* ======================= Entity -> DTO ======================= */

    /** Converte uma Entity em DTO. */
    public static TelefoneDTO toDto(Telefone e) {
        if (e == null) return null;

        // idTelefone (Long) -> Long do DTO
        Long idDto = e.getId();

        Integer servidorId = Math.toIntExact((e.getServidor() == null) ? null : e.getServidor().getId());

        return new TelefoneDTO(
                idDto,
                e.getDDD(),
                e.getNumero(),
                servidorId
        );
    }

    /** Converte uma coleção de Entities em lista de DTOs. */
    public static List<TelefoneDTO> toDtoList(Collection<Telefone> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(TelefoneMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Converte Page<Entity> em Page<DTO> preservando a paginação. */
    public static Page<TelefoneDTO> toDtoPage(Page<Telefone> page) {
        List<TelefoneDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    /* ======================= DTO -> Entity ======================= */

    /**
     * Cria uma nova Entity a partir do DTO, usando o Servidor já carregado.
     */
    public static Telefone toEntity(TelefoneDTO dto, Servidor servidor) {
        if (dto == null) return null;

        Telefone e = new Telefone();

        // idTelefone do DTO (Long) -> Long da Entity
        e.setId(dto.getId());

        e.setDDD(trim(dto.getDDD()));
        e.setNumero(trim(dto.getNumero()));
        e.setServidor(servidor); // pode ser null se DTO não trouxer servidor

        return e;
    }

    /**
     * Cria uma nova Entity a partir do DTO, resolvendo o Servidor via função (repo).
     * Ex.: toEntity(dto, servidorRepo::getReferenceById) ou findById(...).orElseThrow(...)
     */
    public static Telefone toEntity(TelefoneDTO dto, Function<Integer, Servidor> servidorResolver) {
        if (dto == null) return null;
        Servidor servidor = (dto.getServidorId() == null) ? null : servidorResolver.apply(dto.getServidorId());
        return toEntity(dto, servidor);
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * usando o Servidor já carregado. Não altera o id do target.
     */
    public static void copyToEntity(TelefoneDTO dto, Telefone target, Servidor servidor) {
        if (dto == null || target == null) return;

        target.setDDD(trim(dto.getDDD()));
        target.setNumero(trim(dto.getNumero()));
        target.setServidor(servidor);
    }

    /**
     * Atualiza uma Entity existente a partir do DTO (PUT completo),
     * resolvendo o Servidor via função. Não altera o id do target.
     */
    public static void copyToEntity(TelefoneDTO dto, Telefone target, Function<Integer, Servidor> servidorResolver) {
        if (dto == null || target == null) return;
        Servidor servidor = (dto.getServidorId() == null) ? null : servidorResolver.apply(dto.getServidorId());
        copyToEntity(dto, target, servidor);
    }

    /* ======================= Helpers ======================= */

    private static String trim(String s) {
        return (s == null) ? null : s.trim();
    }

}
