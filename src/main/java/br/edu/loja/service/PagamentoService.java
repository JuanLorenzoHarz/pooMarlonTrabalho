package br.edu.loja.service;

import br.edu.loja.domain.*;
import br.edu.loja.dto.PagamentoRequest;
import br.edu.loja.exception.*;
import br.edu.loja.repository.PagamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagamentoService {
    private final PagamentoRepository repo;
    private final PedidoService pedidos;

    public PagamentoService(PagamentoRepository repo, PedidoService pedidos) {
        this.repo = repo;
        this.pedidos = pedidos;
    }

    @Transactional
    public Pagamento criar(Long pedidoId, PagamentoRequest r) {
        Pedido pedido = pedidos.buscar(pedidoId);
        if (pedido.getStatus() == StatusPedido.CANCELADO) throw new RegraNegocioException("Pedido cancelado não pode ser pago");
        if (repo.findByPedidoId(pedidoId).isPresent()) throw new RegraNegocioException("Pedido já possui pagamento");
        return repo.save(new Pagamento(pedido, pedido.calcularTotal(), r.forma()));
    }

    @Transactional(readOnly = true)
    public Pagamento buscarPorPedido(Long pedidoId) {
        return repo.findByPedidoId(pedidoId).orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado"));
    }

    @Transactional
    public Pagamento aprovar(Long pedidoId) {
        Pagamento pagamento = buscarPorPedido(pedidoId);
        pagamento.aprovar();
        Pedido pedido = pedidos.buscar(pedidoId);
        if (pedido.getStatus() == StatusPedido.CRIADO) pedido.alterarStatus(StatusPedido.PAGO);
        return pagamento;
    }

    @Transactional
    public Pagamento recusar(Long pedidoId) {
        Pagamento pagamento = buscarPorPedido(pedidoId);
        pagamento.recusar();
        return pagamento;
    }

    @Transactional
    public Pagamento reembolsar(Long pedidoId) {
        Pagamento pagamento = buscarPorPedido(pedidoId);
        pagamento.reembolsar();
        pedidos.cancelar(pedidoId);
        return pagamento;
    }
}
