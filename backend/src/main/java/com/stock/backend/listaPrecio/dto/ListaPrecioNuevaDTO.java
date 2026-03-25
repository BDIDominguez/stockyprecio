package com.stock.backend.listaPrecio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ListaPrecioNuevaDTO(
        @NotBlank(message = "Codigo es obligatorio")
        @Size(max = 30, message = "Codigo no puede exceder 30 caracteres")
        String codigo,

        @NotBlank(message = "Nombre es obligatorio")
        @Size(max = 100, message = "Nombre no puede exceder 100 caracteres")
        String nombre,

        @Size(max = 500, message = "Descripcion no puede exceder 500 caracteres")
        String descripcion,

        Boolean activo
) {
}
