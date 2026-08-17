package br.edu.loja.service;

import br.edu.loja.domain.*;
import br.edu.loja.dto.ProdutoRequest;
import br.edu.loja.exception.RecursoNaoEncontradoException;
import br.edu.loja.repository.ProdutoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository repo;
    private final CategoriaService categorias;

    public ProdutoService(ProdutoRepository repo, CategoriaService categorias) {
        this.repo = repo;
        this.categorias = categorias;
    }

    @Transactional
    public Produto criar(ProdutoRequest r) {
        Categoria categoria = r.categoriaId() == null ? null : categorias.buscar(r.categoriaId());
        Produto p = new Produto(r.nome(), r.descricao(), r.preco(), r.estoque(), categoria);
        if (Boolean.FALSE.equals(r.ativo())) p.atualizar(r.nome(), r.descricao(), r.preco(), r.estoque(), false, categoria);
        return repo.save(p);
    }

    @Transactional(readOnly = true)
    public List<Produto> listar(boolean apenasAtivos) { return apenasAtivos ? repo.findByAtivoTrue() : repo.findAll(); }

    @Transactional(readOnly = true)
    public Produto buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarFiltrado(String termo, Long categoriaId, BigDecimal precoMin, BigDecimal precoMax,
                                        Boolean emEstoque, Boolean ativo, String ordenarPor, String direcao) {
        Specification<Produto> spec = (root, query, cb) -> cb.conjunction();

        if (termo != null && !termo.isBlank()) {
            String like = "%" + termo.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.<String>get("nome")), like),
                    cb.like(cb.lower(root.<String>get("descricao")), like)
            ));
        }
        if (categoriaId != null) spec = spec.and((root, query, cb) -> cb.equal(root.get("categoria").get("id"), categoriaId));
        if (precoMin != null) spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.<BigDecimal>get("preco"), precoMin));
        if (precoMax != null) spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.<BigDecimal>get("preco"), precoMax));
        if (Boolean.TRUE.equals(emEstoque)) spec = spec.and((root, query, cb) -> cb.greaterThan(root.<Integer>get("estoque"), 0));
        if (ativo != null) spec = spec.and((root, query, cb) -> cb.equal(root.<Boolean>get("ativo"), ativo));

        String campo = switch (ordenarPor == null ? "nome" : ordenarPor) {
            case "preco" -> "preco";
            case "estoque" -> "estoque";
            case "id" -> "id";
            default -> "nome";
        };
        Sort.Direction dir = "desc".equalsIgnoreCase(direcao) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return repo.findAll(spec, Sort.by(dir, campo));
    }

    @Transactional
    public Produto atualizar(Long id, ProdutoRequest r) {
        Produto p = buscar(id);
        Categoria categoria = r.categoriaId() == null ? p.getCategoria() : categorias.buscar(r.categoriaId());
        p.atualizar(r.nome(), r.descricao(), r.preco(), r.estoque(), r.ativo() == null ? p.getAtivo() : r.ativo(), categoria);
        return p;
    }

    @Transactional
    public void excluir(Long id) {
        Produto p = buscar(id);
        p.atualizar(p.getNome(), p.getDescricao(), p.getPreco(), p.getEstoque(), false, p.getCategoria());
    }
}
