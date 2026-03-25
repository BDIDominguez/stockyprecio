package com.stock.backend.producto.dto;

import java.math.BigDecimal;

public record ProductoPrecioComercialDTO(
        String listaPrecio,
        String modoCalculo,
        BigDecimal costoBase,
        BigDecimal margenPorcentaje,
        BigDecimal precioManual,
        BigDecimal subtotalAntesImpuestos,
        BigDecimal importeImpuestos,
        BigDecimal precioFinal
) {
}
