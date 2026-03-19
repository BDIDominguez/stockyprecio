package com.stock.backend.sucursal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SucursalNuevaDTO(
        @NotNull(message = "El codigo es obligatorio")
        @Positive(message = "El codigo debe ser mayor a cero")
        Long codigo,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 200, message = "El nombre no puede superar 200 caracteres")
        String nombre,

        @Size(max = 255, message = "La direccion no puede superar 255 caracteres")
        String direccion,

        @Size(max = 50, message = "El telefono no puede superar 50 caracteres")
        String telefono,

        @Size(max = 100, message = "El encargado no puede superar 100 caracteres")
        String encargado
) {
}
