package br.edu.loja.dto;

import br.edu.loja.domain.FormaPagamento;
import jakarta.validation.constraints.NotNull;

public record PagamentoRequest(@NotNull FormaPagamento forma) {}
