package com.stock.backend.sucursal.controller;

import com.stock.backend.sucursal.dto.SucursalDTO;
import com.stock.backend.sucursal.entity.Sucursal;
import com.stock.backend.sucursal.service.SucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sucursal")
public class SucursalController {

    @Autowired
    private SucursalService service;

    @GetMapping("")
    public ResponseEntity<List<SucursalDTO>> listarTodas(){
        return ResponseEntity.ok(service.consultarTodas().stream().map(SucursalDTO::new).toList());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<SucursalDTO> consultarPorProductoSucursal(@PathVariable long codigo){
        Optional<Sucursal> respuesta = service.consultarPorSucursal(codigo);
        if (respuesta.isPresent()){
            return ResponseEntity.ok(new SucursalDTO(respuesta.get()));
        }
        return ResponseEntity.notFound().build();
    }
}
