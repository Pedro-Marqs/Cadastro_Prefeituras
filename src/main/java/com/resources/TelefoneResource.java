package com.resources;

import com.domains.dtos.TelefoneDTO;
import com.services.TelefoneService;
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
@RequestMapping("/api/telefones")
public class TelefoneResource {

    private final TelefoneService service;

    public TelefoneResource(TelefoneService service) {
        this.service = service;
    }

    // GET paginado; filtro por servidor opcional (?servidorId=)
    @GetMapping
    public ResponseEntity<Page<TelefoneDTO>> list(
            @RequestParam(required = false) Integer servidorId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {

        Page<TelefoneDTO> page = (servidorId != null)
                ? service.findAllByServidor(servidorId, pageable) // paginado + filtro
                : service.findAll(pageable);                    // paginado sem filtro (real no DB)

        return ResponseEntity.ok(page);
    }

    // GET não paginado; filtro por servidor opcional (?servidorId=)
    @GetMapping("/all")
    public ResponseEntity<List<TelefoneDTO>> listAll(
            @RequestParam(required = false) Integer servidorId) {

        List<TelefoneDTO> body = (servidorId != null)
                ? service.findAllByServidor(servidorId) // não paginado + filtro
                : service.findAll();                  // não paginado sem filtro

        return ResponseEntity.ok(body);
    }

    // GET por id (uma telefone específica)
    @GetMapping("/{id}")
    public ResponseEntity<TelefoneDTO> findById(@PathVariable Integer id) {
        TelefoneDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<TelefoneDTO> create(
            @RequestBody @Validated(TelefoneDTO.Create.class) TelefoneDTO dto
    ) {
        TelefoneDTO created = service.create(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TelefoneDTO> update(@PathVariable Long id,
                                                @RequestBody @Validated(TelefoneDTO.Update.class) TelefoneDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
