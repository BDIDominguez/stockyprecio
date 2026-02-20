package com.stock.backend.proveedor.controller;

import com.stock.backend.proveedor.dto.ProveedorDTO;
import com.stock.backend.proveedor.entity.Proveedor;
import com.stock.backend.proveedor.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService servicio;

    @GetMapping("")
    public ResponseEntity<List<Proveedor>> consutlarProveedores(){
        return ResponseEntity.ok(servicio.consultarTodos());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Proveedor> consultarProveedorPorNombre(@PathVariable String nombre){
        Proveedor respuesta = servicio.consultarPorNombre(nombre);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("")
    public ResponseEntity<Proveedor> crearProveedor(@Valid @RequestBody ProveedorDTO nuevo){
        Proveedor respuesta = servicio.crearProveedor(nuevo.toEntity());
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Proveedor> eliminarProveedor(@PathVariable Long id){
        Optional<Proveedor> existe = servicio.consultarPorID(id);
        if (existe.isPresent()){
            Proveedor existente = existe.get();
            existente.setActivo(false);
            servicio.eliminaProveedor(existente);
            return ResponseEntity.ok(existente);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizarProveedor(@PathVariable Long id, @Valid @RequestBody ProveedorDTO actualizado){
        Optional<Proveedor> existe = servicio.consultarPorID(id);
        if (existe.isPresent()){
            Proveedor actual = existe.get();
            actual.actualizar(actualizado.toEntity());
            servicio.actualizarProveedor(actual);
            return ResponseEntity.ok(actual);
        }
        return ResponseEntity.notFound().build();
    }

}
