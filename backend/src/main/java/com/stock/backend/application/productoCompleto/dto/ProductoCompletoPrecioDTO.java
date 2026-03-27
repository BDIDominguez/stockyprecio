package com.stock.backend.application.productoCompleto.dto;

import java.math.BigDecimal;

public record ProductoCompletoPrecioDTO(
        Long listaPrecio,
        BigDecimal costoFinalReferencia,
        BigDecimal margenPorcentaje,
        BigDecimal precioVenta
) {
}
