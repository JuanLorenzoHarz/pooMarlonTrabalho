package br.edu.loja.controller;

import br.edu.loja.domain.Pedido;
import br.edu.loja.dto.VendaResumo;
import br.edu.loja.service.VendaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {
    private final VendaService service;
    public VendaController(VendaService service) { this.service = service; }

    @GetMapping
    public List<Pedido> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return service.listar(inicio, fim);
    }

    @GetMapping("/resumo")
    public VendaResumo resumo(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return service.resumo(inicio, fim);
    }
}
