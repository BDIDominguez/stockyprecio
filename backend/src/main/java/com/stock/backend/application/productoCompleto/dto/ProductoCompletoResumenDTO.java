package com.stock.backend.application.productoCompleto.dto;

import com.stock.backend.producto.dto.ProductoDTO;
import com.stock.backend.stock.dto.StockDTO;

import java.math.BigDecimal;
import java.util.List;

public record ProductoCompletoResumenDTO(
        ProductoDTO producto,
        BigDecimal costoFinal,
        String moneda,
        StockDTO stock,
        List<ProductoCompletoPrecioDTO> precios
) {
}
