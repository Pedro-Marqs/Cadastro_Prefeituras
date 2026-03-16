package com.resources;

import com.domains.dtos.PrefeituraDTO;
import com.domains.dtos.SecretariaDTO;
import com.services.PrefeituraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/prefeitura")
public class PrefeituraResource {
    private final PrefeituraService service;

    public PrefeituraResource(PrefeituraService service) {
        this.service = service;
    }

    // GET paginado;
    @GetMapping
    public ResponseEntity<Page<PrefeituraDTO>> list(
            @RequestParam(required = false) String cidade,
            @PageableDefault(size = 20, sort = "cidade") Pageable pageable) {

        Page<PrefeituraDTO> page = (cidade != null)
                ? service.findByCidade(cidade, pageable) // paginado + filtro
                : service.findAll(pageable);             // paginado sem filtro (real no DB)

        return ResponseEntity.ok(page);
    }

    // GET não paginado;
    @GetMapping("/all")
    public ResponseEntity<List<PrefeituraDTO>> listAll(
            @RequestParam(required = false) String cidade) {

        List<PrefeituraDTO> body = (cidade != null)
                ? service.findByCidade(cidade) // não paginado + filtro
                : service.findAll();           // não paginado

        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrefeituraDTO> findById(@PathVariable Integer id) {
        PrefeituraDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PrefeituraDTO> create(
            @RequestBody @Validated(PrefeituraDTO.Create.class) PrefeituraDTO dto) {

        PrefeituraDTO created = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrefeituraDTO> update(@PathVariable Long id,
                                              @RequestBody @Validated(PrefeituraDTO.Update.class) PrefeituraDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
