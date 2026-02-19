package com.stock.backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.backend.dto.ProductoCompletoDTO;
import com.stock.backend.entity.Producto;
import com.stock.backend.service.casodeuso.CrearProductoCompleto;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@AllArgsConstructor
public class CrearProductoCompletoController {

    private final CrearProductoCompleto servicio;

    @PostMapping("/nuevo")
    public ProductoCompletoDTO crear(@RequestBody Producto nuevo){
        
        return servicio.crear(nuevo, 1L);
    }

    
}
