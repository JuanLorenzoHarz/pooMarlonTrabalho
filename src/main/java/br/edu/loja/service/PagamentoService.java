package br.edu.loja.service;

import br.edu.loja.domain.*;
import br.edu.loja.dto.*;
import br.edu.loja.exception.*;
import br.edu.loja.repository.PagamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PagamentoService {
    private final PagamentoRepository repo;
    private final PedidoService pedidos;
    private final PoliticaParcelamentoService parcelamento;

    public PagamentoService(PagamentoRepository repo, PedidoService pedidos, PoliticaParcelamentoService parcelamento) {
        this.repo = repo;
        this.pedidos = pedidos;
        this.parcelamento = parcelamento;
    }

    @Transactional
    public Pagamento criar(Long pedidoId, PagamentoRequest r) {
        Pedido pedido = pedidos.buscar(pedidoId);
        if (pedido.getStatus() == StatusPedido.CANCELADO) throw new RegraNegocioException("Pedido cancelado não pode ser pago");
        if (repo.findByPedidoId(pedidoId).isPresent()) throw new RegraNegocioException("Pedido já possui pagamento");

        int parcelas = r.parcelasOuPadrao();
        PoliticaParcelamentoService.Calculo calculo = parcelamento.calcular(pedido.calcularTotal(), r.forma(), parcelas);
        return repo.save(new Pagamento(pedido, pedido.calcularTotal(), calculo.valorFinal(), r.forma(), parcelas, calculo.taxaMensal()));
    }

    @Transactional(readOnly = true)
    public ParcelamentoSimulacao simular(Long pedidoId, FormaPagamento forma, int parcelas) {
        Pedido pedido = pedidos.buscar(pedidoId);
        BigDecimal base = pedido.calcularTotal();
        PoliticaParcelamentoService.Calculo calculo = parcelamento.calcular(base, forma, parcelas);
        BigDecimal juros = calculo.valorFinal().subtract(base).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorParcela = calculo.valorFinal().divide(BigDecimal.valueOf(parcelas), 2, RoundingMode.HALF_UP);
        return new ParcelamentoSimulacao(forma, parcelas, base, calculo.taxaMensal(), juros, calculo.valorFinal(), valorParcela);
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

    @Transactional public Pagamento recusar(Long pedidoId) { Pagamento pagamento = buscarPorPedido(pedidoId); pagamento.recusar(); return pagamento; }

    @Transactional
    public Pagamento reembolsar(Long pedidoId) {
        Pagamento pagamento = buscarPorPedido(pedidoId);
        pagamento.reembolsar();
        pedidos.cancelar(pedidoId);
        return pagamento;
    }
}
