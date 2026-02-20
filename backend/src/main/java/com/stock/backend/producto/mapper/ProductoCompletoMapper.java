package com.stock.backend.producto.mapper;

import com.stock.backend.producto.dto.ProductoCompletoDTO;
import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.producto.entity.Producto;
import com.stock.backend.proveedor.entity.Proveedor;
import com.stock.backend.stock.entity.Stock;

public class ProductoCompletoMapper {


    public static ProductoCompletoDTO toDto(Producto producto, Categoria categoria, Proveedor proveedor, Stock stock) {
        return new ProductoCompletoDTO(producto.getCodigo(), producto.getNombre(), producto.getDescripcion(), categoria, proveedor, stock, producto.getManejaStock(), producto.getActivo(), producto.getFechaCreacion(), producto.getFechaModificacion());
    }



}
