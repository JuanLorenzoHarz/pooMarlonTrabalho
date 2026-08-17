package br.edu.loja.dto;

import br.edu.loja.domain.FormaPagamento;
import jakarta.validation.constraints.*;

public record PagamentoRequest(
        @NotNull FormaPagamento forma,
        @Min(1) @Max(12) Integer parcelas
) {
    public int parcelasOuPadrao() { return parcelas == null ? 1 : parcelas; }
}
