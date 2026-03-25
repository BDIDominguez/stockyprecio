package com.stock.backend.producto.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductoComercialDTO(
        ProductoDTO producto,
        BigDecimal costo,
        String moneda,
        List<String> codigosBarra,
        List<String> impuestos,
        List<ProductoPrecioComercialDTO> precios
) {
}
