package br.edu.loja.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CupomDescontoTest {
    @Test
    void deveCalcularDescontoPercentual() {
        CupomDesconto cupom = new CupomDesconto("POO10", TipoDesconto.PERCENTUAL, new BigDecimal("10"), BigDecimal.ZERO, LocalDateTime.now().plusDays(1), 10, true);
        assertEquals(new BigDecimal("20.00"), cupom.calcularDesconto(new BigDecimal("200.00")));
    }

    @Test
    void deveRespeitarValorMinimo() {
        CupomDesconto cupom = new CupomDesconto("MINIMO", TipoDesconto.VALOR_FIXO, new BigDecimal("20"), new BigDecimal("100"), null, null, true);
        assertFalse(cupom.podeUsar(new BigDecimal("99.99"), LocalDateTime.now()));
    }
}
