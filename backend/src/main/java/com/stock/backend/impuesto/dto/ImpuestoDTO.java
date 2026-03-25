package com.stock.backend.impuesto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ImpuestoDTO(
        Long id,
        String codigo,
        String nombre,
        String descripcion,
        BigDecimal porcentaje,
        Boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {
}
