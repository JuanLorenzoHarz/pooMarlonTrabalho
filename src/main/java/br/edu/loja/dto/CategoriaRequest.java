package br.edu.loja.dto;

import jakarta.validation.constraints.*;

public record CategoriaRequest(
        @NotBlank @Size(max = 100) String nome,
        @Size(max = 300) String descricao,
        Boolean ativa
) {}
