package com.stock.backend.application.productoCompleto.dto;

import com.stock.backend.producto.dto.ProductoDTO;
import com.stock.backend.stock.dto.StockDTO;

import java.util.List;

public record ProductoCompletoDTO(
        ProductoDTO producto,
        ProductoCompletoCostoDTO costo,
        List<String> codigosBarra,
        StockDTO stock,
        List<ProductoCompletoPrecioDTO> precios
) {
}
