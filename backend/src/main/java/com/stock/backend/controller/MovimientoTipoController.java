package com.stock.backend.controller;

import com.stock.backend.entity.MovimientoTipo;
import com.stock.backend.service.MovimientoTipoService;
import com.stock.backend.service.tipomovimiento.MovimientoTipoOrquestadorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientotipo")
@AllArgsConstructor
public class MovimientoTipoController {

    private final MovimientoTipoOrquestadorService service;

    @GetMapping
    public ResponseEntity<List<MovimientoTipo>> listarMovimientos(){
        return ResponseEntity.ok(service.consultaTodos());
    }

    @PutMapping()
    public ResponseEntity<MovimientoTipo> crearMovimiento(@RequestBody MovimientoTipo nuevo){
        System.out.println("Recibido en el controller " + nuevo.toString());
        return ResponseEntity.ok(service.nuevo(nuevo));
    }

    @GetMapping("/siglas/{siglas}")
    public ResponseEntity<MovimientoTipo> consultarMovimiento(@PathVariable String siglas){
        return ResponseEntity.ok(service.consultarPorSiglas(siglas));
    }

    @PutMapping("/desactivar/{moviento}")
    public ResponseEntity<MovimientoTipo> desactivarMovimiento(@RequestBody MovimientoTipo movimiento){
        return ResponseEntity.ok(service.desactivar(movimiento));
    }
    @PutMapping("/activar/{moviento}")
    public ResponseEntity<MovimientoTipo> activarMovimiento(@RequestBody MovimientoTipo movimiento){
        return ResponseEntity.ok(service.activar(movimiento));
    }
}
