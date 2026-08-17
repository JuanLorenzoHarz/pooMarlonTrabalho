package br.edu.loja.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record PedidoRequest(
        @NotNull Long clienteId,
        @NotEmpty List<@Valid ItemRequest> itens,
        @NotNull @DecimalMin("0.00") BigDecimal frete
) {}
