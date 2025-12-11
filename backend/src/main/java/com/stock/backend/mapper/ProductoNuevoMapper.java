package com.stock.backend.mapper;

import com.stock.backend.dto.ProductoNuevoDTO;
import com.stock.backend.entity.Producto;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class ProductoNuevoMapper {
    public static ProductoNuevoDTO toDto(Producto prod){
        return new ProductoNuevoDTO(
                prod.getCodigo(),
                prod.getNombre(),
                prod.getDescripcion(),
                prod.getCategoria(),
                prod.getProveedor(),
                prod.getStockMinimo(),
                prod.getManejaStock(),
                prod.getActivo()
        );
    }

    public static Producto toEntidad(ProductoNuevoDTO prod){
        return new Producto(-1,
                prod.codigo(),
                prod.nombre(),
                prod.descripcion(),
                prod.categoriaId(),
                prod.proveedorId(),
                prod.stockMinimo(),
                prod.manejaStock(),
                prod.activo(),
                LocalDateTime.now(),
                LocalDateTime.now());
    }
}
