package com.stock.backend.controller;

import com.stock.backend.entity.MovimientoTipo;
import com.stock.backend.service.MovimientoTipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientotipo")
public class MovimientoTipoController {

    @Autowired
    private MovimientoTipoService service;

    @GetMapping
    public ResponseEntity<List<MovimientoTipo>> listarMovimientos(){
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping()
    public ResponseEntity<MovimientoTipo> crearMovimiento(@RequestBody MovimientoTipo nuevo){
        System.out.println("Recibido en el controller " + nuevo.toString());
        return ResponseEntity.ok(service.nuevoMovimiento(nuevo));
    }
}
