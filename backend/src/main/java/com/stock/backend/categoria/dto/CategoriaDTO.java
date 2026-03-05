package com.stock.backend.categoria.dto;

import java.time.LocalDateTime;

public record CategoriaDTO(
        Long codigo,
        String nombre,
        String descripcion,
        Boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
){
}
