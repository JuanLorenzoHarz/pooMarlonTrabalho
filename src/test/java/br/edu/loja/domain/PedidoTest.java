package br.edu.loja.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {
    @Test
    void deveCalcularTotalComFrete() {
        Cliente cliente = new Cliente("Juan", "12345678901", "juan@example.com", "Rua A");
        Produto produto = new Produto("Mouse", "", new BigDecimal("100.00"), 10);
        Pedido pedido = new Pedido(cliente, new BigDecimal("20.00"));
        pedido.adicionarItem(new ItemPedido(pedido, produto, 2, produto.getPreco()));
        assertEquals(new BigDecimal("220.00"), pedido.calcularTotal());
    }

    @Test
    void deveReduzirEstoque() {
        Produto produto = new Produto("Teclado", "", new BigDecimal("200.00"), 5);
        produto.reduzirEstoque(2);
        assertEquals(3, produto.getEstoque());
    }
}
