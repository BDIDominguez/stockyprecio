package com.stock.backend.dto;

import com.stock.backend.entity.Producto;
import com.stock.backend.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public record ProductoDTO(
        Long codigo,
        String nombre,
        String descripcion,
        Long categoria,
        Long proveedor,
        Double stockMinimo ,
        Boolean manejaStock ,
        Boolean activo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaModificacion
        ) {
    public ProductoDTO (Producto producto) {
        this(
            producto.getCodigo(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getCategoria(),
                producto.getProveedor(),
                producto.getStockMinimo(),
                producto.getManejaStock(),
                producto.getActivo(),
                producto.getFechaCreacion(),
                producto.getFechaModificacion()
        );
    }

}
