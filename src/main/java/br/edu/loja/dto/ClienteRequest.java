package br.edu.loja.dto;

import jakarta.validation.constraints.*;

public record ClienteRequest(
        @NotBlank @Size(max = 120) String nome,
        @NotBlank @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos") String cpf,
        @NotBlank @Email String email,
        @NotBlank @Size(max = 220) String endereco
) {}
