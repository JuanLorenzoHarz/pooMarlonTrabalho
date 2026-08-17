package br.edu.loja.service;

import br.edu.loja.domain.FormaPagamento;
import br.edu.loja.exception.RegraNegocioException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class PoliticaParcelamentoServiceTest {
    private final PoliticaParcelamentoService service = new PoliticaParcelamentoService();

    @Test
    void tresParcelasNoCreditoNaoTemJuros() {
        var calculo = service.calcular(new BigDecimal("300.00"), FormaPagamento.CARTAO_CREDITO, 3);
        assertEquals(new BigDecimal("300.00"), calculo.valorFinal());
        assertEquals(new BigDecimal("0.00"), calculo.taxaMensal());
    }

    @Test
    void seisParcelasPossuemJuros() {
        var calculo = service.calcular(new BigDecimal("300.00"), FormaPagamento.CARTAO_CREDITO, 6);
        assertTrue(calculo.valorFinal().compareTo(new BigDecimal("300.00")) > 0);
        assertEquals(new BigDecimal("1.50"), calculo.taxaMensal());
    }

    @Test
    void pixNaoPodeSerParcelado() {
        assertThrows(RegraNegocioException.class, () -> service.calcular(new BigDecimal("100.00"), FormaPagamento.PIX, 2));
    }
}
