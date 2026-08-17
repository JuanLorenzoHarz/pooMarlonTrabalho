package br.edu.loja.controller;

import br.edu.loja.domain.CupomDesconto;
import br.edu.loja.dto.CupomRequest;
import br.edu.loja.service.CupomDescontoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cupons")
public class CupomController {
    private final CupomDescontoService service;
    public CupomController(CupomDescontoService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<CupomDesconto> criar(@Valid @RequestBody CupomRequest r) {
        CupomDesconto c = service.criar(r);
        return ResponseEntity.created(URI.create("/api/cupons/" + c.getId())).body(c);
    }

    @GetMapping public List<CupomDesconto> listar() { return service.listar(); }
    @GetMapping("/{id}") public CupomDesconto buscar(@PathVariable Long id) { return service.buscar(id); }

    @PatchMapping("/{id}")
    public CupomDesconto atualizarControle(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime validade,
            @RequestParam(required = false) Integer limiteUsos) {
        return service.atualizarControle(id, ativo, validade, limiteUsos);
    }
}
