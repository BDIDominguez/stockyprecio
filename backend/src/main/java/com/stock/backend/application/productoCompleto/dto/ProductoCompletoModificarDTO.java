package com.stock.backend.application.productoCompleto.dto;

import com.stock.backend.producto.dto.ProductoModificarDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProductoCompletoModificarDTO(
        @NotNull(message = "Los datos del producto son obligatorios")
        @Valid
        ProductoModificarDTO producto,

        @NotNull(message = "El costo compuesto es obligatorio")
        @Valid
        ProductoCompletoCostoNuevoDTO costo,

        @Positive(message = "La sucursal debe ser mayor a cero")
        Long sucursal,

        @DecimalMin(value = "0.00", inclusive = true, message = "La reserva no puede ser negativa")
        Double reserva,

        List<@Size(max = 100, message = "El codigo de barra no puede exceder 100 caracteres") String> codigosBarra,

        @NotEmpty(message = "Debe informar al menos un precio")
        @Valid
        List<ProductoCompletoPrecioNuevoDTO> precios,

        Boolean confirmarBajoCosto
) {
}
