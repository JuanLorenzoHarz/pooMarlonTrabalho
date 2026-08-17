package br.edu.loja.dto;

import jakarta.validation.constraints.*;

public record ClienteUpdateRequest(
        @NotBlank @Size(max = 120) String nome,
        @NotBlank @Email String email,
        @NotBlank @Size(max = 220) String endereco
) {}
