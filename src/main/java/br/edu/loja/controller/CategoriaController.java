package br.edu.loja.controller;

import br.edu.loja.domain.Categoria;
import br.edu.loja.dto.CategoriaRequest;
import br.edu.loja.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService service;
    public CategoriaController(CategoriaService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Categoria> criar(@Valid @RequestBody CategoriaRequest r) {
        Categoria c = service.criar(r);
        return ResponseEntity.created(URI.create("/api/categorias/" + c.getId())).body(c);
    }

    @GetMapping public List<Categoria> listar(@RequestParam(defaultValue = "true") boolean apenasAtivas) { return service.listar(apenasAtivas); }
    @GetMapping("/{id}") public Categoria buscar(@PathVariable Long id) { return service.buscar(id); }
    @PutMapping("/{id}") public Categoria atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest r) { return service.atualizar(id, r); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void desativar(@PathVariable Long id) { service.desativar(id); }
}
