package com.stock.backend.producto.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductoPrecioComercialNuevoDTO(
        @NotBlank(message = "La lista de precio es obligatoria")
        String listaPrecio,

        @NotBlank(message = "El modo de calculo es obligatorio")
        String modoCalculo,

        @NotNull(message = "El costo base es obligatorio")
        @DecimalMin(value = "0.0000", inclusive = true, message = "El costo base no puede ser negativo")
        BigDecimal costoBase,

        @DecimalMin(value = "0.0000", inclusive = true, message = "El margen no puede ser negativo")
        BigDecimal margenPorcentaje,

        @DecimalMin(value = "0.0000", inclusive = true, message = "El precio manual no puede ser negativo")
        BigDecimal precioManual,

        @NotNull(message = "El subtotal antes de impuestos es obligatorio")
        @DecimalMin(value = "0.0000", inclusive = true, message = "El subtotal no puede ser negativo")
        BigDecimal subtotalAntesImpuestos,

        @NotNull(message = "El importe de impuestos es obligatorio")
        @DecimalMin(value = "0.0000", inclusive = true, message = "El importe de impuestos no puede ser negativo")
        BigDecimal importeImpuestos,

        @NotNull(message = "El precio final es obligatorio")
        @DecimalMin(value = "0.00", inclusive = true, message = "El precio final no puede ser negativo")
        BigDecimal precioFinal
) {
}
