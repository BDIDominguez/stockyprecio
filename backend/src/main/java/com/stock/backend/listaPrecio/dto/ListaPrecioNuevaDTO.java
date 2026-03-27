package com.stock.backend.listaPrecio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ListaPrecioNuevaDTO(
        @NotNull(message = "Codigo es obligatorio")
        @Positive(message = "Codigo debe ser mayor a cero")
        Long codigo,

        @NotBlank(message = "Nombre es obligatorio")
        @Size(max = 100, message = "Nombre no puede exceder 100 caracteres")
        String nombre,

        @Size(max = 500, message = "Descripcion no puede exceder 500 caracteres")
        String descripcion,

        Boolean activo
) {
}
