package com.resources;

import com.domains.dtos.DepartamentoDTO;
import com.services.DepartamentoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.time.LocalDate;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoResource {

    private final DepartamentoService service;

    public DepartamentoResource(DepartamentoService service) {
        this.service = service;
    }

    // GET paginado; filtro por secretaria opcional (?secretariaId=)
    @GetMapping
    public ResponseEntity<Page<DepartamentoDTO>> list(
            @RequestParam(required = false) Long secretariaId,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {

        Page<DepartamentoDTO> page = (secretariaId != null)
                ? service.findAllBySecretaria(secretariaId, pageable) // paginado + filtro
                : service.findAll(pageable);                    // paginado sem filtro (real no DB)

        return ResponseEntity.ok(page);
    }

    // GET não paginado; filtro por secretaria opcional (?secretariaId=)
    @GetMapping("/all")
    public ResponseEntity<List<DepartamentoDTO>> listAll(
            @RequestParam(required = false) Long secretariaId) {

        List<DepartamentoDTO> body = (secretariaId != null)
                ? service.findAllBySecretaria(secretariaId) // não paginado + filtro
                : service.findAll();                  // não paginado sem filtro

        return ResponseEntity.ok(body);
    }

    // GET paginado por secretaria via path /secretaria/{secretariaId}
    @GetMapping("/secretaria/{secretariaId}")
    public ResponseEntity<Page<DepartamentoDTO>> findAllBySecretaria(
            @PathVariable Long secretariaId,
            Pageable pageable
    ) {
        Page<DepartamentoDTO> page = service.findAllBySecretaria(secretariaId, pageable);
        return ResponseEntity.ok(page);
    }

    // GET por id (uma departamento específica)
    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoDTO> findById(@PathVariable Integer id) {
        DepartamentoDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<DepartamentoDTO> create(
            @RequestBody @Validated(DepartamentoDTO.Create.class) DepartamentoDTO dto
    ) {
        DepartamentoDTO created = service.create(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoDTO> update(@PathVariable Long id,
                                                @RequestBody @Validated(DepartamentoDTO.Update.class) DepartamentoDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}