package com.stock.backend.controller;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.backend.dto.ProductoCompletoDTO;
import com.stock.backend.service.casodeuso.CrearProductoCompleto;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@AllArgsConstructor
public class CrearProductoCompletoController {

    private final CrearProductoCompleto servicio;

    @PutMapping("/nuevo/{nuevo}")
    public ProductoCompletoDTO crear(@RequestBody ProductoCompletoDTO nuevo){
        
        return null;
    }

    
}
