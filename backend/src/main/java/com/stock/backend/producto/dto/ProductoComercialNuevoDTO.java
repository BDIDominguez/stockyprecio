package com.stock.backend.producto.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record ProductoComercialNuevoDTO(
        @NotNull(message = "Los datos del producto son obligatorios")
        @Valid
        ProductoNuevoDTO producto,

        @NotNull(message = "El costo es obligatorio")
        @DecimalMin(value = "0.0000", inclusive = true, message = "El costo no puede ser negativo")
        BigDecimal costo,

        @Size(max = 20, message = "La moneda no puede exceder 20 caracteres")
        String moneda,

        List<@Size(max = 100, message = "El codigo de barra no puede exceder 100 caracteres") String> codigosBarra,

        List<String> impuestos,

        @NotEmpty(message = "Debe informar al menos un precio")
        @Valid
        List<ProductoPrecioComercialNuevoDTO> precios
) {
}
