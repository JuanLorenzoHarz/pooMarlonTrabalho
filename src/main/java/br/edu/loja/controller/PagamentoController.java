package br.edu.loja.controller;

import br.edu.loja.domain.*;
import br.edu.loja.dto.*;
import br.edu.loja.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/pedidos/{pedidoId}/pagamento")
public class PagamentoController {
    private final PagamentoService service;
    public PagamentoController(PagamentoService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Pagamento> criar(@PathVariable Long pedidoId, @Valid @RequestBody PagamentoRequest r) {
        Pagamento p = service.criar(pedidoId, r);
        return ResponseEntity.created(URI.create("/api/pedidos/" + pedidoId + "/pagamento")).body(p);
    }

    @GetMapping public Pagamento buscar(@PathVariable Long pedidoId) { return service.buscarPorPedido(pedidoId); }

    @GetMapping("/simulacao")
    public ParcelamentoSimulacao simular(@PathVariable Long pedidoId,
                                         @RequestParam FormaPagamento forma,
                                         @RequestParam(defaultValue = "1") int parcelas) {
        return service.simular(pedidoId, forma, parcelas);
    }

    @PostMapping("/aprovacao") public Pagamento aprovar(@PathVariable Long pedidoId) { return service.aprovar(pedidoId); }
    @PostMapping("/recusa") public Pagamento recusar(@PathVariable Long pedidoId) { return service.recusar(pedidoId); }
    @PostMapping("/reembolso") public Pagamento reembolsar(@PathVariable Long pedidoId) { return service.reembolsar(pedidoId); }
}
