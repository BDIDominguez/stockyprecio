package com.stock.backend.application.productoCompleto.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductoCompletoPrecioNuevoDTO(
        @NotNull(message = "La lista de precio es obligatoria")
        @Positive(message = "La lista de precio debe ser mayor a cero")
        Long listaPrecio,

        @NotNull(message = "El margen es obligatorio")
        BigDecimal margenPorcentaje,

        @NotNull(message = "El precio de venta es obligatorio")
        @DecimalMin(value = "0.00", inclusive = true, message = "El precio de venta no puede ser negativo")
        BigDecimal precioVenta
) {
}
