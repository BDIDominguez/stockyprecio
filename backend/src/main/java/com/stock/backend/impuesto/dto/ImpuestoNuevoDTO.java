package com.stock.backend.impuesto.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ImpuestoNuevoDTO(
        @NotBlank(message = "Codigo es obligatorio")
        @Size(max = 30, message = "Codigo no puede exceder 30 caracteres")
        String codigo,

        @NotBlank(message = "Nombre es obligatorio")
        @Size(max = 100, message = "Nombre no puede exceder 100 caracteres")
        String nombre,

        @Size(max = 500, message = "Descripcion no puede exceder 500 caracteres")
        String descripcion,

        @NotNull(message = "Porcentaje es obligatorio")
        @DecimalMin(value = "0.0000", inclusive = true, message = "Porcentaje no puede ser negativo")
        BigDecimal porcentaje,

        Boolean activo
) {
}
