package br.edu.loja.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PedidoDescontoTest {
    @Test
    void totalConsideraDescontoEFrete() {
        Cliente c = new Cliente("Juan", "12345678901", "juan@example.com", "Rua A");
        Produto p = new Produto("Mouse", "", new BigDecimal("100.00"), 10);
        Pedido pedido = new Pedido(c, new BigDecimal("20.00"));
        pedido.adicionarItem(new ItemPedido(pedido, p, 2, p.getPreco()));
        pedido.aplicarDesconto("POO10", new BigDecimal("20.00"));
        assertEquals(new BigDecimal("200.00"), pedido.calcularTotal());
    }
}
