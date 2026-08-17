package br.edu.loja.service;

import br.edu.loja.domain.*;
import br.edu.loja.dto.*;
import br.edu.loja.exception.*;
import br.edu.loja.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository repo;
    private final ClienteService clientes;
    private final ProdutoService produtos;

    public PedidoService(PedidoRepository repo, ClienteService clientes, ProdutoService produtos) {
        this.repo = repo;
        this.clientes = clientes;
        this.produtos = produtos;
    }

    @Transactional
    public Pedido criar(PedidoRequest r) {
        Cliente cliente = clientes.buscar(r.clienteId());
        Pedido pedido = new Pedido(cliente, r.frete());

        for (ItemRequest item : r.itens()) {
            Produto produto = produtos.buscar(item.produtoId());
            if (!produto.getAtivo()) throw new RegraNegocioException("Produto " + produto.getId() + " está inativo");
            if (produto.getEstoque() < item.quantidade()) throw new RegraNegocioException("Estoque insuficiente para " + produto.getNome());
            produto.reduzirEstoque(item.quantidade());
            pedido.adicionarItem(new ItemPedido(pedido, produto, item.quantidade(), produto.getPreco()));
        }

        return repo.save(pedido);
    }

    @Transactional(readOnly = true)
    public Pedido buscar(Long id) {
        return repo.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Pedido> listar(Long clienteId) {
        return clienteId == null ? repo.findAllByOrderByCriadoEmDesc() : repo.findByClienteIdOrderByCriadoEmDesc(clienteId);
    }

    @Transactional
    public Pedido alterarStatus(Long id, StatusPedido status) {
        Pedido pedido = buscar(id);
        pedido.alterarStatus(status);
        return pedido;
    }

    @Transactional
    public Pedido cancelar(Long id) {
        Pedido pedido = buscar(id);
        if (pedido.getStatus() != StatusPedido.CANCELADO) {
            pedido.cancelar();
            pedido.getItens().forEach(i -> i.getProduto().reporEstoque(i.getQuantidade()));
        }
        return pedido;
    }
}
