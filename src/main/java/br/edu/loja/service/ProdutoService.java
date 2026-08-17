package br.edu.loja.service;

import br.edu.loja.domain.Produto;
import br.edu.loja.dto.ProdutoRequest;
import br.edu.loja.exception.RecursoNaoEncontradoException;
import br.edu.loja.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository repo;
    public ProdutoService(ProdutoRepository repo) { this.repo = repo; }

    @Transactional
    public Produto criar(ProdutoRequest r) {
        Produto p = new Produto(r.nome(), r.descricao(), r.preco(), r.estoque());
        if (Boolean.FALSE.equals(r.ativo())) p.atualizar(r.nome(), r.descricao(), r.preco(), r.estoque(), false);
        return repo.save(p);
    }

    @Transactional(readOnly = true)
    public List<Produto> listar(boolean apenasAtivos) { return apenasAtivos ? repo.findByAtivoTrue() : repo.findAll(); }

    @Transactional(readOnly = true)
    public Produto buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
    }

    @Transactional
    public Produto atualizar(Long id, ProdutoRequest r) {
        Produto p = buscar(id);
        p.atualizar(r.nome(), r.descricao(), r.preco(), r.estoque(), r.ativo() == null ? p.getAtivo() : r.ativo());
        return p;
    }

    @Transactional
    public void excluir(Long id) {
        Produto p = buscar(id);
        p.atualizar(p.getNome(), p.getDescricao(), p.getPreco(), p.getEstoque(), false);
    }
}
