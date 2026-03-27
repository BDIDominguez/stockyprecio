package com.stock.backend.componenteCosto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ComponenteCostoNuevoDTO(
        @NotNull(message = "Codigo es obligatorio")
        @Positive(message = "Codigo debe ser mayor a cero")
        Long codigo,

        @NotBlank(message = "Nombre es obligatorio")
        @Size(max = 100, message = "Nombre no puede exceder 100 caracteres")
        String nombre,

        @Size(max = 500, message = "Descripcion no puede exceder 500 caracteres")
        String descripcion,

        @NotBlank(message = "Tipo de componente es obligatorio")
        @Size(max = 30, message = "Tipo de componente no puede exceder 30 caracteres")
        String tipoComponente,

        @NotBlank(message = "Tipo de valor es obligatorio")
        @Size(max = 20, message = "Tipo de valor no puede exceder 20 caracteres")
        String tipoValor,

        @NotNull(message = "Valor por defecto es obligatorio")
        BigDecimal valorDefecto,

        @NotBlank(message = "La etapa de aplicacion es obligatoria")
        @Size(max = 40, message = "La etapa de aplicacion no puede exceder 40 caracteres")
        String etapaAplicacion,

        @NotNull(message = "Debe indicar si es editable en producto")
        Boolean editableEnProducto,

        Boolean activo
) {
}
