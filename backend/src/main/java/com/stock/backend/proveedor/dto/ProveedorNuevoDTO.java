package com.stock.backend.proveedor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProveedorNuevoDTO(
        @NotNull(message = "El codigo es obligatorio")
        @Positive(message = "El codigo debe ser mayor a cero")
        Long codigo,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 200, message = "El nombre no puede superar 200 caracteres")
        String nombre,

        @Size(max = 100, message = "El contacto no puede superar 100 caracteres")
        String contacto,

        @Size(max = 50, message = "El telefono no puede superar 50 caracteres")
        String telefono,

        @Email(message = "El email debe ser valido")
        @Size(max = 100, message = "El email no puede superar 100 caracteres")
        String email,

        @Size(max = 500, message = "La direccion no puede superar 500 caracteres")
        String direccion
) {
}
