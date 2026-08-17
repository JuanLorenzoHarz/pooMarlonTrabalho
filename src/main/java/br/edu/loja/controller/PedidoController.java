package br.edu.loja.controller;

import br.edu.loja.domain.*;
import br.edu.loja.dto.PedidoRequest;
import br.edu.loja.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService service;
    public PedidoController(PedidoService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Pedido> criar(@Valid @RequestBody PedidoRequest r) {
        Pedido pedido = service.criar(r);
        return ResponseEntity.created(URI.create("/api/pedidos/" + pedido.getId())).body(pedido);
    }

    @GetMapping public List<Pedido> listar(@RequestParam(required = false) Long clienteId) { return service.listar(clienteId); }
    @GetMapping("/{id}") public Pedido buscar(@PathVariable Long id) { return service.buscar(id); }
    @PatchMapping("/{id}/status") public Pedido status(@PathVariable Long id, @RequestParam StatusPedido status) { return service.alterarStatus(id, status); }
    @PostMapping("/{id}/cancelamento") public Pedido cancelar(@PathVariable Long id) { return service.cancelar(id); }
}
