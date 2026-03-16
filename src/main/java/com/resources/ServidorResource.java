package com.resources;

import com.domains.dtos.ServidorDTO;
import com.services.ServidorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/servidores")
public class ServidorResource {

    private final ServidorService service;

    public ServidorResource(ServidorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ServidorDTO>> list(
            @RequestParam(required = false) Integer departamentoId,
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {

        Page<ServidorDTO> page =
                (departamentoId != null)
                        ? service.findAllByDepartamento(departamentoId, pageable)
                        : (nome != null)
                            ? service.findAllByNome(nome, pageable)
                            : service.findAll(pageable);

        return ResponseEntity.ok(page);
    }


    @GetMapping("/all")
    public ResponseEntity<List<ServidorDTO>> listAll(
            @RequestParam(required = false) Integer departamentoId,
            @RequestParam(required = false) String nome) {

        List<ServidorDTO> body =
                (departamentoId != null)
                        ? service.findAllByDepartamento(departamentoId)
                        : (nome != null)
                            ? service.findAllByNome(nome)
                            : service.findAll();

        return ResponseEntity.ok(body);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ServidorDTO> findById(@PathVariable Long id) {
        ServidorDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    public ResponseEntity<ServidorDTO> create(
            @RequestBody @Validated(ServidorDTO.Create.class) ServidorDTO dto
    ) {
        ServidorDTO created = service.create(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServidorDTO> update(
            @PathVariable Long id,
            @RequestBody @Validated(ServidorDTO.Update.class) ServidorDTO dto
    ) {
        dto.setId(id);
        ServidorDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/efetivos")
    public ResponseEntity<List<ServidorDTO>> listarServidoresEfetivos() {
        List<ServidorDTO> servidores = service.listarServidoresEfetivos();
        return ResponseEntity.ok(servidores);
    }

    @GetMapping("/comissionados")
    public ResponseEntity<List<ServidorDTO>> listarServidoresComissionados() {
        List<ServidorDTO> servidores = service.listarServidoresComissionados();
        return ResponseEntity.ok(servidores);
    }
}
