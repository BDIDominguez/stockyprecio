package com.stock.backend.mapper;

import com.stock.backend.dto.ProductoDTO;
import com.stock.backend.entity.Producto;

public class ProductoMapper {

    public static ProductoDTO toDto(Producto prod){
        return new ProductoDTO(
                prod.getId(),
                prod.getCodigo(),
                prod.getNombre(),
                prod.getDescripcion(),
                prod.getCategoria(),
                prod.getProveedor(),
                prod.getStockMinimo(),
                prod.getManejaStock(),
                prod.getActivo(),
                prod.getFechaCreacion(),
                prod.getFechaModificacion(),
                prod.getTipoIva());
    }

    public static Producto toEntidad(ProductoDTO prod){
        return new Producto(
                prod.id(),
                prod.codigo(),
                prod.nombre(),
                prod.descripcion(),
                prod.categoria(),
                prod.proveedor(),
                prod.stockMinimo(),
                prod.manejaStock(),
                prod.activo(),
                prod.fechaCreacion(),
                prod.fechaModificacion(),
                prod.tipoIva());
    }
}
