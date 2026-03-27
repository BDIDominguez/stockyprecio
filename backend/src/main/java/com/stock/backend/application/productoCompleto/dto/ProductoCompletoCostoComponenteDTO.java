package com.stock.backend.application.productoCompleto.dto;

import java.math.BigDecimal;

public record ProductoCompletoCostoComponenteDTO(
        Long componente,
        String nombreComponente,
        String etapaAplicacion,
        String resultadoEtapa,
        String tipoValorAplicado,
        BigDecimal valorAplicado,
        BigDecimal baseCalculo,
        BigDecimal importeCalculado,
        BigDecimal subtotalResultante
) {
}
