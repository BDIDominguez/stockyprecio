package com.stock.backend.application.productoCompleto.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record ProductoCompletoCostoNuevoDTO(
        @NotNull(message = "El costo base es obligatorio")
        @DecimalMin(value = "0.0000", inclusive = true, message = "El costo base no puede ser negativo")
        BigDecimal costoBase,

        @NotNull(message = "El neto1 es obligatorio")
        @DecimalMin(value = "0.0000", inclusive = true, message = "El neto1 no puede ser negativo")
        BigDecimal neto1,

        @NotNull(message = "El neto2 es obligatorio")
        @DecimalMin(value = "0.0000", inclusive = true, message = "El neto2 no puede ser negativo")
        BigDecimal neto2,

        @NotNull(message = "El costo final es obligatorio")
        @DecimalMin(value = "0.0000", inclusive = true, message = "El costo final no puede ser negativo")
        BigDecimal costoFinal,

        @Size(max = 20, message = "La moneda no puede exceder 20 caracteres")
        String moneda,

        @Valid
        List<ProductoCompletoCostoComponenteNuevoDTO> componentes
) {
}
