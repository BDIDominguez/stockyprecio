package com.stock.backend.application.productoCompleto.controller;

import com.stock.backend.application.productoCompleto.dto.ProductoCompletoDTO;
import com.stock.backend.application.productoCompleto.dto.ProductoCompletoModificarDTO;
import com.stock.backend.application.productoCompleto.dto.ProductoCompletoNuevoDTO;
import com.stock.backend.application.productoCompleto.dto.ProductoCompletoResumenDTO;
import com.stock.backend.application.productoCompleto.service.ProductoCompletoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos/completo")
@AllArgsConstructor
@Validated
public class ProductoCompletoController {
    private final ProductoCompletoService service;

    @Operation(summary = "Consultar productos completos")
    @GetMapping("")
    public ResponseEntity<Page<ProductoCompletoResumenDTO>> consultarTodos(
            @RequestParam(required = false) @Positive Long codigo,
            @RequestParam(required = false) @Positive Long sucursal,
            @RequestParam(required = false) @Size(min = 2, message = "debe ingresar al menos 2 caracteres") String nombre,
            @RequestParam(defaultValue = "true") Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sort) {
        return ResponseEntity.ok(service.consultarTodos(codigo, sucursal, nombre, activo, page, size, sort));
    }

    @Operation(summary = "Crear producto completo")
    @PostMapping("")
    public ResponseEntity<ProductoCompletoDTO> crear(@Valid @RequestBody ProductoCompletoNuevoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Consultar producto completo por codigo")
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoCompletoDTO> consultarPorCodigo(
            @PathVariable @Positive Long codigo,
            @RequestParam(required = false) @Positive Long sucursal) {
        return ResponseEntity.ok(service.consultarPorCodigo(codigo, sucursal));
    }

    @Operation(summary = "Modificar producto completo")
    @PutMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoCompletoDTO> modificar(
            @PathVariable @Positive Long codigo,
            @Valid @RequestBody ProductoCompletoModificarDTO dto) {
        return ResponseEntity.ok(service.modificar(codigo, dto));
    }
}
