package br.edu.loja.dto;

import br.edu.loja.domain.TipoDesconto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CupomRequest(
        @NotBlank @Size(max = 40) String codigo,
        @NotNull TipoDesconto tipo,
        @NotNull @DecimalMin("0.01") BigDecimal valor,
        @DecimalMin("0.00") BigDecimal minimoPedido,
        LocalDateTime validade,
        @Min(1) Integer limiteUsos,
        Boolean ativo
) {}
