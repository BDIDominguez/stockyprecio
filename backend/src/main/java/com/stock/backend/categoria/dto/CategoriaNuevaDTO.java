package com.stock.backend.categoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CategoriaNuevaDTO(
        @NotNull(message = "Código es obligatorio.")
        Long codigo,
        @NotBlank(message = "Nombre es obligarorio.")
        String nombre,
        String descripcion
){
}
