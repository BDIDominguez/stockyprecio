package com.stock.backend.producto.mapper;

import com.stock.backend.producto.dto.ProductoNuevoDTO;
import com.stock.backend.producto.entity.Producto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductoNuevoMapper {
    ProductoNuevoDTO toDto(Producto producto);
    Producto toEntidad(ProductoNuevoDTO dto);
}
