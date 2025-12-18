package com.stock.backend.mapper;

import com.stock.backend.dto.ProductoCompletoDTO;
import com.stock.backend.entity.Categoria;
import com.stock.backend.entity.Producto;
import com.stock.backend.entity.Proveedor;
import com.stock.backend.entity.Stock;

public class ProductoCompletoMapper {


    public static ProductoCompletoDTO toDto(Producto producto, Categoria categoria, Proveedor proveedor, Stock stock) {
        return new ProductoCompletoDTO(producto.getCodigo(), producto.getNombre(), producto.getDescripcion(), categoria, proveedor, stock, producto.getManejaStock(), producto.getActivo(), producto.getFechaCreacion(), producto.getFechaModificacion());
    }



}
