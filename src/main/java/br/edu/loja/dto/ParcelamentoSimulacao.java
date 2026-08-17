package br.edu.loja.dto;

import br.edu.loja.domain.FormaPagamento;
import java.math.BigDecimal;

public record ParcelamentoSimulacao(
        FormaPagamento forma,
        int parcelas,
        BigDecimal valorBase,
        BigDecimal taxaJurosMensal,
        BigDecimal valorJuros,
        BigDecimal valorFinal,
        BigDecimal valorParcela
) {}
