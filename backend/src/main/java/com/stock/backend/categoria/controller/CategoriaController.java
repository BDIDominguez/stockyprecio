package com.stock.backend.categoria.controller;

import com.stock.backend.categoria.dto.CategoriaDTO;
import com.stock.backend.categoria.dto.CategoriaNuevaDTO;
import com.stock.backend.categoria.entity.Categoria;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.categoria.mapper.CategoriaMapper;
import com.stock.backend.categoria.mapper.CategoriaNuevaMapper;
import com.stock.backend.categoria.service.CategoriaFacadeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@AllArgsConstructor
public class CategoriaController {

    private final CategoriaFacadeService service;

    @GetMapping("")
    public ResponseEntity<List<CategoriaDTO>> consultarCategorias(){
        List<CategoriaDTO> lista = service.consultarTodos().stream().map(CategoriaMapper::toDto).toList();
        return ResponseEntity.ok(lista);
    }
    @GetMapping("/activos")
    public ResponseEntity<List<CategoriaDTO>> consultarCategoriasActivas(){
        List<CategoriaDTO> lista = service.consultarTodosActivos().stream().map(CategoriaMapper::toDto).toList();
        return ResponseEntity.ok(lista);
    }
    @GetMapping("/inactivos")
    public ResponseEntity<List<CategoriaDTO>> consultarCategoriasInactivas(){
        List<CategoriaDTO> lista = service.consultarTodosInactivos().stream().map(CategoriaMapper::toDto).toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<CategoriaDTO> consultarCategoriaPorNombre(@PathVariable String nombre){
        CategoriaDTO categoria = CategoriaMapper.toDto(service.buscarPorNombre(nombre).
                orElseThrow(() -> new RecursoNoEncontradoException("no se encontro una categoria con el nombre: " + nombre)));
        return ResponseEntity.ok(categoria);
    }

    @PostMapping("")
    public ResponseEntity<CategoriaDTO> crearCategoria(@Validated @RequestBody CategoriaNuevaDTO categoria){
        Categoria respuesta = service.crear(CategoriaNuevaMapper.toEntidad(categoria));
        return ResponseEntity.ok(CategoriaMapper.toDto(respuesta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> modificarCategoria(@PathVariable Long id, @RequestBody Categoria categoria){
        Categoria existe = service.modificar(categoria,id);
        return ResponseEntity.ok(CategoriaMapper.toDto(existe));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoriaDTO> eliminarCategoria(@PathVariable Long id){
        Categoria respuesta = service.eliminar(id);
        return ResponseEntity.ok(CategoriaMapper.toDto(respuesta));
    }
}
