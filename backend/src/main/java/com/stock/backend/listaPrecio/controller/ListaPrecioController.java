package com.stock.backend.listaPrecio.controller;

import com.stock.backend.listaPrecio.dto.ListaPrecioDTO;
import com.stock.backend.listaPrecio.dto.ListaPrecioNuevaDTO;
import com.stock.backend.listaPrecio.entity.ListaPrecio;
import com.stock.backend.listaPrecio.mapper.ListaPrecioMapper;
import com.stock.backend.listaPrecio.mapper.ListaPrecioNuevaMapper;
import com.stock.backend.listaPrecio.service.ListaPrecioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas-precios")
@AllArgsConstructor
@Validated
public class ListaPrecioController {
    private final ListaPrecioService service;
    private final ListaPrecioMapper listaPrecioMapper;
    private final ListaPrecioNuevaMapper listaPrecioNuevaMapper;

    @GetMapping("")
    public ResponseEntity<List<ListaPrecioDTO>> consultarTodos(@RequestParam(required = false) Boolean activo) {
        List<ListaPrecioDTO> respuesta = service.consultarTodos(activo).stream()
                .map(listaPrecioMapper::toDto)
                .toList();
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ListaPrecioDTO> consultarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(listaPrecioMapper.toDto(service.consultarPorCodigo(codigo)));
    }

    @PostMapping("")
    public ResponseEntity<ListaPrecioDTO> crear(@Valid @RequestBody ListaPrecioNuevaDTO dto) {
        ListaPrecio creada = service.crear(listaPrecioNuevaMapper.toEntidad(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(listaPrecioMapper.toDto(creada));
    }

    @PutMapping("/codigo/{codigo}")
    public ResponseEntity<ListaPrecioDTO> modificar(
            @PathVariable String codigo,
            @Valid @RequestBody ListaPrecioNuevaDTO dto) {
        ListaPrecio actualizada = service.modificar(codigo, listaPrecioNuevaMapper.toEntidad(dto));
        return ResponseEntity.ok(listaPrecioMapper.toDto(actualizada));
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
