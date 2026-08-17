package br.edu.loja.service;

import br.edu.loja.domain.*;
import br.edu.loja.dto.ItemRequest;
import br.edu.loja.exception.*;
import br.edu.loja.repository.CarrinhoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarrinhoService {
    private final CarrinhoRepository repo;
    private final ClienteService clientes;
    private final ProdutoService produtos;

    public CarrinhoService(CarrinhoRepository repo, ClienteService clientes, ProdutoService produtos) {
        this.repo = repo;
        this.clientes = clientes;
        this.produtos = produtos;
    }

    @Transactional
    public Carrinho obterOuCriar(Long clienteId) {
        return repo.findByClienteId(clienteId)
                .orElseGet(() -> repo.save(new Carrinho(clientes.buscar(clienteId))));
    }

    @Transactional
    public Carrinho adicionar(Long clienteId, ItemRequest r) {
        Carrinho c = obterOuCriar(clienteId);
        Produto p = produtos.buscar(r.produtoId());
        if (!p.getAtivo()) throw new RegraNegocioException("Produto inativo");
        if (p.getEstoque() < r.quantidade()) throw new RegraNegocioException("Quantidade maior que estoque disponível");

        for (ItemCarrinho item : c.getItens()) {
            if (item.getProduto().getId().equals(p.getId())) {
                int novaQuantidade = item.getQuantidade() + r.quantidade();
                if (p.getEstoque() < novaQuantidade) throw new RegraNegocioException("Quantidade maior que estoque disponível");
                item.alterarQuantidade(novaQuantidade);
                return c;
            }
        }

        c.getItens().add(new ItemCarrinho(c, p, r.quantidade()));
        return c;
    }

    @Transactional
    public Carrinho remover(Long clienteId, Long produtoId) {
        Carrinho c = obterOuCriar(clienteId);
        boolean removeu = c.getItens().removeIf(i -> i.getProduto().getId().equals(produtoId));
        if (!removeu) throw new RecursoNaoEncontradoException("Produto não encontrado no carrinho");
        return c;
    }

    @Transactional
    public Carrinho limpar(Long clienteId) {
        Carrinho c = obterOuCriar(clienteId);
        c.getItens().clear();
        return c;
    }
}
