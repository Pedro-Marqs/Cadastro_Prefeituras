package com.mappers;

import com.domains.Prefeitura;
import com.domains.dtos.PrefeituraDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PrefeituraMapper {

    private PrefeituraMapper() {}

    /** Converte uma Entity em DTO. */
    public static PrefeituraDTO toDto(Prefeitura e) {
        if (e == null) return null;
        return new PrefeituraDTO(
                e.getId(),
                e.getCidade(),
                e.getCNPJ(),
                e.getFundadaEm()
        );
    }

    /** Cria uma nova Entity a partir do DTO (respeita id do DTO se presente). */
    public static Prefeitura toEntity(PrefeituraDTO dto) {
        if (dto == null) return null;
        Prefeitura e = new Prefeitura();
        e.setId(dto.getId()); // se null, JPA gera; se não, usado no update
        e.setCidade(dto.getCidade() == null ? null : dto.getCidade().trim());
        e.setCNPJ(dto.getCNPJ() == null ? null : dto.getCNPJ().trim());
        e.setFundadaEm(dto.getFundadaEm() == null ? null : dto.getFundadaEm());
        return e;
    }

    /**
     * Copia dados do DTO para uma Entity existente (PUT “completo”).
     * Não altera o id da entidade alvo.
     */
    public static void copyToEntity(PrefeituraDTO dto, Prefeitura target) {
        if (dto == null || target == null) return;
        target.setCidade(dto.getCidade() == null ? null : dto.getCidade().trim());
        target.setCNPJ(dto.getCNPJ() == null ? null : dto.getCNPJ().trim());
        target.setFundadaEm(dto.getFundadaEm() == null ? null : dto.getFundadaEm());
    }

    /** Converte uma coleção de Entities em lista de DTOs. */
    public static List<PrefeituraDTO> toDtoList(Collection<Prefeitura> entities) {
        if (entities == null) return List.of();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(PrefeituraMapper::toDto)
                .collect(Collectors.toList());
    }

    /** Converte uma coleção de DTOs em lista de Entities. */
    public static List<Prefeitura> toEntityList(Collection<PrefeituraDTO> dtos) {
        if (dtos == null) return List.of();
        return dtos.stream()
                .filter(Objects::nonNull)
                .map(PrefeituraMapper::toEntity)
                .collect(Collectors.toList());
    }

    /** Converte Page<Entity> em Page<DTO> (preserva paginação). */
    public static Page<PrefeituraDTO> toDtoPage(Page<Prefeitura> page) {
        List<PrefeituraDTO> content = toDtoList(page.getContent());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

}
