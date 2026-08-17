package br.edu.loja.dto;

import java.math.BigDecimal;

public record VendaResumo(
        long quantidadeVendas,
        long itensVendidos,
        BigDecimal faturamento,
        BigDecimal ticketMedio
) {}
