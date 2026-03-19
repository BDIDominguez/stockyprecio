package com.stock.backend.application.crearProductoCompleto.controller;

import com.stock.backend.application.crearProductoCompleto.service.CrearProductoCompleto;
import com.stock.backend.producto.dto.ProductoCompletoDTO;
import com.stock.backend.producto.dto.ProductoNuevoDTO;
import com.stock.backend.producto.mapper.ProductoNuevoMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productos")
@AllArgsConstructor
public class CrearProductoCompletoController {

    private final CrearProductoCompleto servicio;
    private final ProductoNuevoMapper productoNuevoMapper;

    @PostMapping("/nuevo")
    public ProductoCompletoDTO crear(@RequestBody ProductoNuevoDTO nuevo) {
        return servicio.crear(productoNuevoMapper.toEntidad(nuevo), 1L);
    }
}
