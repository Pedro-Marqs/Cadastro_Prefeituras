package com.resources;

import com.domains.dtos.SecretariaDTO;
import com.services.SecretariaService;
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
@RequestMapping("/api/secretarias")
public class SecretariaResource {

    private final SecretariaService service;

    public SecretariaResource(SecretariaService service) {
        this.service = service;
    }

    // GET paginado; filtro por prefeitura opcional (?prefeituraId=)
    @GetMapping
    public ResponseEntity<Page<SecretariaDTO>> list(
            @RequestParam(required = false) Long prefeituraId,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {

        Page<SecretariaDTO> page = (prefeituraId != null)
                ? service.findAllByPrefeitura(prefeituraId, pageable) // paginado + filtro
                : service.findAll(pageable);                    // paginado sem filtro (real no DB)

        return ResponseEntity.ok(page);
    }

    // GET não paginado; filtro por prefeitura opcional (?prefeituraId=)
    @GetMapping("/all")
    public ResponseEntity<List<SecretariaDTO>> listAll(
            @RequestParam(required = false) Long prefeituraId) {

        List<SecretariaDTO> body = (prefeituraId != null)
                ? service.findAllByPrefeitura(prefeituraId) // não paginado + filtro
                : service.findAll();                  // não paginado sem filtro

        return ResponseEntity.ok(body);
    }

    // GET paginado por prefeitura via path /prefeitura/{prefeituraId}
    @GetMapping("/prefeitura/{prefeituraId}")
    public ResponseEntity<Page<SecretariaDTO>> findAllByPrefeitura(
            @PathVariable Long prefeituraId,
            Pageable pageable
    ) {
        Page<SecretariaDTO> page = service.findAllByPrefeitura(prefeituraId, pageable);
        return ResponseEntity.ok(page);
    }

    // GET por id (uma secretaria específica)
    @GetMapping("/{id}")
    public ResponseEntity<SecretariaDTO> findById(@PathVariable Integer id) {
        SecretariaDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<SecretariaDTO> create(
            @RequestBody @Validated(SecretariaDTO.Create.class) SecretariaDTO dto
    ) {
        SecretariaDTO created = service.create(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SecretariaDTO> update(@PathVariable Long id,
                                                   @RequestBody @Validated(SecretariaDTO.Update.class) SecretariaDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
