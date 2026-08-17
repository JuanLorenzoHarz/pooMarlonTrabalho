package br.edu.loja.dto;

import jakarta.validation.constraints.*;

public record ItemRequest(
        @NotNull Long produtoId,
        @NotNull @Min(1) Integer quantidade
) {}
