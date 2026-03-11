package com.resources;

import com.domains.dtos.PrefeituraDTO;
import com.services.PrefeituraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prefeitura")
public class PrefeituraResource {
    private final PrefeituraService service;

    public PrefeituraResource(PrefeituraService service) {
        this.service = service;
    }

    // GET não paginado (simples e direto)
    @GetMapping("/all")
    public ResponseEntity<List<PrefeituraDTO>> listAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // GET "paginado" embrulhado (usa findAll e monta PageImpl)
    @GetMapping
    public ResponseEntity<Page<PrefeituraDTO>> list(
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        List<PrefeituraDTO> all = service.findAll();
        Page<PrefeituraDTO> page = new PageImpl<>(all, pageable, all.size());
        return ResponseEntity.ok(page);
    }
}
