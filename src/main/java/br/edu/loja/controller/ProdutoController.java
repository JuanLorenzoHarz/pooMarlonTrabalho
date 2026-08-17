package br.edu.loja.controller;

import br.edu.loja.domain.Produto;
import br.edu.loja.dto.ProdutoRequest;
import br.edu.loja.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoService service;
    public ProdutoController(ProdutoService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Produto> criar(@Valid @RequestBody ProdutoRequest r) {
        Produto produto = service.criar(r);
        return ResponseEntity.created(URI.create("/api/produtos/" + produto.getId())).body(produto);
    }

    @GetMapping public List<Produto> listar(@RequestParam(defaultValue = "true") boolean apenasAtivos) { return service.listar(apenasAtivos); }

    @GetMapping("/busca")
    public List<Produto> buscarFiltrado(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @RequestParam(required = false) Boolean emEstoque,
            @RequestParam(defaultValue = "true") Boolean ativo,
            @RequestParam(defaultValue = "nome") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direcao) {
        return service.buscarFiltrado(termo, categoriaId, precoMin, precoMax, emEstoque, ativo, ordenarPor, direcao);
    }

    @GetMapping("/{id}") public Produto buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}") public Produto atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest r) { return service.atualizar(id, r); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id) { service.excluir(id); }
}
