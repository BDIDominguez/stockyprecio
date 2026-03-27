package com.stock.backend.application.productoCompleto.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductoCompletoCostoDTO(
        BigDecimal costoBase,
        BigDecimal neto1,
        BigDecimal neto2,
        BigDecimal costoFinal,
        String moneda,
        List<ProductoCompletoCostoComponenteDTO> componentes
) {
}
