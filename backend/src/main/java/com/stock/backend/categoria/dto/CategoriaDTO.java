package com.stock.backend.categoria.dto;

import java.time.LocalDateTime;

public record CategoriaDTO(
        Long codigo,
        String nombre,
        String descripcion,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
){
}
