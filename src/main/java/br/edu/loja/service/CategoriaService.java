package br.edu.loja.service;

import br.edu.loja.domain.Categoria;
import br.edu.loja.dto.CategoriaRequest;
import br.edu.loja.exception.*;
import br.edu.loja.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) { this.repo = repo; }

    @Transactional
    public Categoria criar(CategoriaRequest r) {
        if (repo.existsByNomeIgnoreCase(r.nome())) throw new RegraNegocioException("Já existe uma categoria com esse nome");
        Categoria categoria = new Categoria(r.nome(), r.descricao());
        if (Boolean.FALSE.equals(r.ativa())) categoria.atualizar(r.nome(), r.descricao(), false);
        return repo.save(categoria);
    }

    @Transactional(readOnly = true)
    public List<Categoria> listar(boolean apenasAtivas) {
        return apenasAtivas ? repo.findByAtivaTrueOrderByNomeAsc() : repo.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada"));
    }

    @Transactional
    public Categoria atualizar(Long id, CategoriaRequest r) {
        Categoria categoria = buscar(id);
        if (!categoria.getNome().equalsIgnoreCase(r.nome()) && repo.existsByNomeIgnoreCase(r.nome())) {
            throw new RegraNegocioException("Já existe uma categoria com esse nome");
        }
        categoria.atualizar(r.nome(), r.descricao(), r.ativa() == null ? categoria.getAtiva() : r.ativa());
        return categoria;
    }

    @Transactional
    public void desativar(Long id) {
        Categoria categoria = buscar(id);
        categoria.atualizar(categoria.getNome(), categoria.getDescricao(), false);
    }
}
