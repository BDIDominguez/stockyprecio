package com.stock.backend.categoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaNuevaDTO(
        @NotNull(message = "Código es obligatorio.")
        Long codigo,
        @NotBlank(message = "Nombre es obligatorio.")
        String nombre,
        String descripcion
){
}
