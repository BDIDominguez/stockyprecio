package com.stock.backend.producto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductoNuevoDTO(
        @NotNull(message = "Codigo es obligatorio")
        @Positive(message = "El codigo debe ser mayor a cero")
        Long codigo,

        @NotBlank(message = "Nombre es obligatorio")
        @Size(max = 200, message = "Nombre no puede exceder 200 caracteres")
        String nombre,

        @Size(max = 1000, message = "Descripcion muy larga")
        String descripcion,

        @Positive(message = "La categoria debe ser mayor a cero")
        Long categoria,

        @Positive(message = "El proveedor debe ser mayor a cero")
        Long proveedor,

        Boolean manejaStock,
        Boolean activo
) {
}
