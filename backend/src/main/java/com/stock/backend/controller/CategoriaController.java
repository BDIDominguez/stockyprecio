package com.stock.backend.controller;

import com.stock.backend.entity.Categoria;
import com.stock.backend.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping("")
    public ResponseEntity<List<Categoria>> consultarCategorias(){
        List<Categoria> lista = service.consultaTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Categoria> consultarCategoriaPorNombre(@PathVariable String nombre){
        Categoria categoria = service.consultarPorNombre(nombre);
        return ResponseEntity.ok(categoria);
    }

    @PostMapping("")
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria categoria){
        Categoria respuesta = service.crearCategoria(categoria);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> modificarCategoria(@PathVariable Long id, @RequestBody Categoria categoria){
        Optional<Categoria> existe = service.consultarPorId(id);
        if (existe.isPresent()){
            Categoria actual = existe.get();
            actual.actualizar(categoria);
            Categoria respuesta = service.modificarCategoria(actual);
            return ResponseEntity.ok(respuesta);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Categoria> eliminarCategoria(@PathVariable Long id){
        Optional<Categoria> existe = service.consultarPorId(id);
        if (existe.isPresent()){
            Categoria actual = existe.get();
            actual.setActivo(false);
            Categoria respuesta = service.modificarCategoria(actual);
            return ResponseEntity.ok(respuesta);
        }
        return ResponseEntity.notFound().build();
    }
}
