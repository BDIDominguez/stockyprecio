package com.stock.backend.sucursal.controller;

import com.stock.backend.sucursal.dto.SucursalDTO;
import com.stock.backend.sucursal.entity.Sucursal;
import com.stock.backend.sucursal.mapper.SucursalMapper;
import com.stock.backend.sucursal.service.SucursalService;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sucursal")
@AllArgsConstructor
@Validated
public class SucursalController {

    private final SucursalService service;
    private final SucursalMapper mapper;

    @GetMapping("")
    public ResponseEntity<Page<SucursalDTO>> consultar(
            @RequestParam(required = false) @Size(min = 2, message = "debe ingresar al menos 2 caracteres") String nombre,
            @RequestParam(defaultValue = "true") Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sort){

        Page<SucursalDTO> resultado;
        if (nombre != null) {
            resultado = service.consultarPorNombreIgnoreCase(nombre, page, size, sort).map(mapper::toDto);
        } else {
            resultado = service.consultarTodas(activo, page, size, sort).map(mapper::toDto);
        }
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<SucursalDTO> consultarPorProductoSucursal(@PathVariable long codigo){
        Optional<Sucursal> respuesta = service.consultarPorSucursal(codigo);
        if (respuesta.isPresent()){
            return ResponseEntity.ok(mapper.toDto(respuesta.get()));
        }
        return ResponseEntity.notFound().build();
    }
}
