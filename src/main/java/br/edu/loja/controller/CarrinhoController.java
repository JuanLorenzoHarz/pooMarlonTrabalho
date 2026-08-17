package br.edu.loja.controller;

import br.edu.loja.domain.Carrinho;
import br.edu.loja.dto.ItemRequest;
import br.edu.loja.service.CarrinhoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes/{clienteId}/carrinho")
public class CarrinhoController {
    private final CarrinhoService service;
    public CarrinhoController(CarrinhoService service) { this.service = service; }

    @GetMapping public Carrinho obter(@PathVariable Long clienteId) { return service.obterOuCriar(clienteId); }
    @PostMapping("/itens") public Carrinho adicionar(@PathVariable Long clienteId, @Valid @RequestBody ItemRequest r) { return service.adicionar(clienteId, r); }
    @DeleteMapping("/itens/{produtoId}") public Carrinho remover(@PathVariable Long clienteId, @PathVariable Long produtoId) { return service.remover(clienteId, produtoId); }
    @DeleteMapping public Carrinho limpar(@PathVariable Long clienteId) { return service.limpar(clienteId); }
}
