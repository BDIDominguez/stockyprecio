package com.stock.backend.impuesto.controller;

import com.stock.backend.impuesto.dto.ImpuestoDTO;
import com.stock.backend.impuesto.dto.ImpuestoNuevoDTO;
import com.stock.backend.impuesto.entity.Impuesto;
import com.stock.backend.impuesto.mapper.ImpuestoMapper;
import com.stock.backend.impuesto.mapper.ImpuestoNuevoMapper;
import com.stock.backend.impuesto.service.ImpuestoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/impuestos")
@AllArgsConstructor
@Validated
public class ImpuestoController {
    private final ImpuestoService service;
    private final ImpuestoMapper impuestoMapper;
    private final ImpuestoNuevoMapper impuestoNuevoMapper;

    @GetMapping("")
    public ResponseEntity<List<ImpuestoDTO>> consultarTodos(@RequestParam(required = false) Boolean activo) {
        List<ImpuestoDTO> respuesta = service.consultarTodos(activo).stream()
                .map(impuestoMapper::toDto)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ImpuestoDTO> consultarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(impuestoMapper.toDto(service.consultarPorCodigo(codigo)));
    }

    @PostMapping("")
    public ResponseEntity<ImpuestoDTO> crear(@Valid @RequestBody ImpuestoNuevoDTO dto) {
        Impuesto creado = service.crear(impuestoNuevoMapper.toEntidad(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(impuestoMapper.toDto(creado));
    }

    @PutMapping("/codigo/{codigo}")
    public ResponseEntity<ImpuestoDTO> modificar(
            @PathVariable String codigo,
            @Valid @RequestBody ImpuestoNuevoDTO dto) {
        Impuesto actualizado = service.modificar(codigo, impuestoNuevoMapper.toEntidad(dto));
        return ResponseEntity.ok(impuestoMapper.toDto(actualizado));
    }

    @DeleteMapping("/codigo/{codigo}")
    public ResponseEntity<Void> desactivar(@PathVariable String codigo) {
        service.desactivar(codigo);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/codigo/{codigo}/activar")
    public ResponseEntity<Void> activar(@PathVariable String codigo) {
        service.activar(codigo);
        return ResponseEntity.noContent().build();
    }
}
