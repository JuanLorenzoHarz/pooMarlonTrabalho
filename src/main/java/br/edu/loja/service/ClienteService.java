package br.edu.loja.service;

import br.edu.loja.domain.Cliente;
import br.edu.loja.dto.*;
import br.edu.loja.exception.*;
import br.edu.loja.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository repo;
    public ClienteService(ClienteRepository repo) { this.repo = repo; }

    @Transactional
    public Cliente criar(ClienteRequest r) {
        if (repo.existsByCpf(r.cpf())) throw new RegraNegocioException("CPF já cadastrado");
        if (repo.existsByEmail(r.email())) throw new RegraNegocioException("Email já cadastrado");
        return repo.save(new Cliente(r.nome(), r.cpf(), r.email(), r.endereco()));
    }

    @Transactional(readOnly = true)
    public List<Cliente> listar() { return repo.findAll(); }

    @Transactional(readOnly = true)
    public Cliente buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));
    }

    @Transactional
    public Cliente atualizar(Long id, ClienteUpdateRequest r) {
        Cliente c = buscar(id);
        if (repo.existsByEmailAndIdNot(r.email(), id)) throw new RegraNegocioException("Email já cadastrado");
        c.atualizarDados(r.nome(), r.email(), r.endereco());
        return c;
    }

    @Transactional
    public void excluir(Long id) { repo.delete(buscar(id)); }
}
