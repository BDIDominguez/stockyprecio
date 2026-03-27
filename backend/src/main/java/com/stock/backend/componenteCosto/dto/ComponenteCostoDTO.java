package com.stock.backend.componenteCosto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ComponenteCostoDTO(
        Long codigo,
        String nombre,
        String descripcion,
        String tipoComponente,
        String tipoValor,
        BigDecimal valorDefecto,
        String etapaAplicacion,
        Boolean editableEnProducto,
        Boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
) {
}
