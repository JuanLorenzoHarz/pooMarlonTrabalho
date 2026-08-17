package br.edu.loja.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank @Size(max = 120) String nome,
        @Size(max = 500) String descricao,
        @NotNull @DecimalMin("0.01") BigDecimal preco,
        @NotNull @Min(0) Integer estoque,
        Boolean ativo,
        Long categoriaId
) {}
