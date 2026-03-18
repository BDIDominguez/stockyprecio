package com.stock.backend.proveedor.dto;

import com.stock.backend.proveedor.entity.Proveedor;
import jakarta.validation.constraints.*;

public record ProveedorDTO(

        @NotNull(message = "El Codigo es obligatorio")
        @Size(message = "El código no puede superar 50 caracteres")
        Long codigo,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 200, message = "El nombre no puede superar 200 caracteres")
        String nombre,

        @Size(max = 100, message = "El contacto no puede superar 100 caracteres")
        String contacto,

        @Size(max = 50, message = "El teléfono no puede superar 50 caracteres")
        String telefono,

        @Email(message = "El email debe ser válido")
        @Size(max = 100, message = "El email no puede superar 100 caracteres")
        String email,

        @Size(max = 500, message = "La dirección no puede superar 500 caracteres")
        String direccion,

        Boolean activo
){}
