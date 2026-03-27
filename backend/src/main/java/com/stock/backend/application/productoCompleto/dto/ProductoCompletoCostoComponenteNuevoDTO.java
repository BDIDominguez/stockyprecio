package com.stock.backend.application.productoCompleto.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductoCompletoCostoComponenteNuevoDTO(
        @NotNull(message = "El componente es obligatorio")
        @Positive(message = "El componente debe ser mayor a cero")
        Long componente,

        @NotNull(message = "El valor es obligatorio")
        BigDecimal valorAplicado,

        @NotNull(message = "La base de calculo es obligatoria")
        BigDecimal baseCalculo,

        @NotNull(message = "El importe calculado es obligatorio")
        BigDecimal importeCalculado,

        @NotNull(message = "El subtotal resultante es obligatorio")
        BigDecimal subtotalResultante
) {
}
