package br.edu.loja.service;

import br.edu.loja.domain.FormaPagamento;
import br.edu.loja.exception.RegraNegocioException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PoliticaParcelamentoService {
    public record Calculo(BigDecimal valorFinal, BigDecimal taxaMensal) {}

    public Calculo calcular(BigDecimal valorBase, FormaPagamento forma, int parcelas) {
        if (parcelas < 1 || parcelas > 12) throw new RegraNegocioException("Número de parcelas deve ficar entre 1 e 12");
        if (forma != FormaPagamento.CARTAO_CREDITO && parcelas != 1) {
            throw new RegraNegocioException("Somente cartão de crédito aceita parcelamento");
        }

        BigDecimal taxa = taxaMensal(parcelas, forma);
        if (taxa.signum() == 0) return new Calculo(valorBase.setScale(2, RoundingMode.HALF_UP), taxa);

        BigDecimal fator = BigDecimal.ONE.add(taxa.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP));
        BigDecimal total = valorBase;
        for (int i = 0; i < parcelas; i++) total = total.multiply(fator);
        return new Calculo(total.setScale(2, RoundingMode.HALF_UP), taxa);
    }

    private BigDecimal taxaMensal(int parcelas, FormaPagamento forma) {
        if (forma != FormaPagamento.CARTAO_CREDITO || parcelas <= 3) return BigDecimal.ZERO.setScale(2);
        if (parcelas <= 6) return new BigDecimal("1.50");
        return new BigDecimal("2.00");
    }
}
