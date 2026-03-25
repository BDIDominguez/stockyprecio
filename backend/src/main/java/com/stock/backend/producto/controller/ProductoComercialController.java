package com.stock.backend.producto.controller;

import com.stock.backend.producto.dto.ProductoComercialDTO;
import com.stock.backend.producto.dto.ProductoComercialModificarDTO;
import com.stock.backend.producto.dto.ProductoComercialNuevoDTO;
import com.stock.backend.producto.service.ProductoComercialService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos/comercial")
@AllArgsConstructor
@Validated
public class ProductoComercialController {
    private final ProductoComercialService service;

    @Operation(summary = "Crear producto comercial completo")
    @PostMapping("")
    public ResponseEntity<ProductoComercialDTO> crear(@Valid @RequestBody ProductoComercialNuevoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Consultar producto comercial por codigo")
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoComercialDTO> consultarPorCodigo(@PathVariable @Positive Long codigo) {
        return ResponseEntity.ok(service.consultarPorCodigo(codigo));
    }

    @Operation(summary = "Modificar producto comercial completo")
    @PutMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoComercialDTO> modificar(
            @PathVariable @Positive Long codigo,
            @Valid @RequestBody ProductoComercialModificarDTO dto) {
        return ResponseEntity.ok(service.modificar(codigo, dto));
    }
}
