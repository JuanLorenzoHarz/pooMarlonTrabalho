package br.edu.loja.service;

import br.edu.loja.domain.*;
import br.edu.loja.dto.VendaResumo;
import br.edu.loja.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService {
    private static final List<StatusPedido> STATUS_DE_VENDA = List.of(
            StatusPedido.PAGO, StatusPedido.EM_PREPARACAO, StatusPedido.ENVIADO, StatusPedido.ENTREGUE
    );

    private final PedidoRepository pedidos;
    public VendaService(PedidoRepository pedidos) { this.pedidos = pedidos; }

    @Transactional(readOnly = true)
    public List<Pedido> listar(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio != null && fim != null) return pedidos.findByStatusInAndCriadoEmBetweenOrderByCriadoEmDesc(STATUS_DE_VENDA, inicio, fim);
        return pedidos.findByStatusInOrderByCriadoEmDesc(STATUS_DE_VENDA);
    }

    @Transactional(readOnly = true)
    public VendaResumo resumo(LocalDateTime inicio, LocalDateTime fim) {
        List<Pedido> vendas = listar(inicio, fim);
        BigDecimal faturamento = vendas.stream().map(Pedido::calcularTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        long itens = vendas.stream().flatMap(p -> p.getItens().stream()).mapToLong(ItemPedido::getQuantidade).sum();
        BigDecimal ticket = vendas.isEmpty() ? BigDecimal.ZERO : faturamento.divide(BigDecimal.valueOf(vendas.size()), 2, RoundingMode.HALF_UP);
        return new VendaResumo(vendas.size(), itens, faturamento.setScale(2, RoundingMode.HALF_UP), ticket);
    }
}
