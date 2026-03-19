package com.stock.backend.producto.mapper;

import com.stock.backend.producto.dto.ProductoDTO;
import com.stock.backend.producto.entity.Producto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductoMapper {
    ProductoDTO toDto(Producto producto);
    Producto toEntidad(ProductoDTO dto);
}
