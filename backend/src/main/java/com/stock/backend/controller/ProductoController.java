package com.stock.backend.controller;

import com.stock.backend.dto.ProductoCompletoDTO;
import com.stock.backend.dto.ProductoDTO;
import com.stock.backend.dto.ProductoNuevoDTO;
import com.stock.backend.entity.Producto;
import com.stock.backend.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService servicio;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarProductos(){
        List<Producto> lista = servicio.listarTodos();
        List<ProductoDTO> dtos = lista.stream().map(ProductoDTO::new).toList();
        return  ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> productoPorId(@PathVariable Long id){
        Optional<Producto> prod = servicio.buscarPorId(id);
        if (prod.isPresent()){
            ProductoDTO respuesta = new ProductoDTO(prod.get());
            return ResponseEntity.ok(respuesta);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoDTO> productoPorCodigo(@PathVariable Long codigo){
        Optional prod = servicio.buscarPorCodigo(codigo);
        if (prod.isPresent()){
            ProductoDTO respuesta = new ProductoDTO((Producto) prod.get());
            return ResponseEntity.ok(respuesta);
        }
        return ResponseEntity.notFound().build();
    }

    /*
    @PostMapping("")
    public ResponseEntity<ProductoDTO> crearProcducto(@Validated @RequestBody ProductoNuevoDTO producto){
        Producto nuevo = servicio.nuevoProducto(producto.convertir(), producto.sucursal());
        ProductoDTO respuesta = new ProductoDTO(nuevo);
        return ResponseEntity.ok(respuesta);
    } */

    /*
    @PutMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoDTO> actualizarProducto (@PathVariable Long codigo,@Validated @RequestBody ProductoNuevoDTO producto){
        Optional<Producto> prod = servicio.buscarPorCodigo(codigo);
        if (prod.isPresent()){
            Producto actual = prod.get();
            actual.actualizarCampos(producto.convertir());
            Producto actualizado = servicio.nuevoProducto(actual, producto.sucursal());
            return ResponseEntity.ok(new ProductoDTO(actualizado));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/completo/{codigo}/sucursal/{sucursal}")
    public ResponseEntity<ProductoCompletoDTO> productoCompleto(@PathVariable Long codigo, @PathVariable Long sucursal){
        System.out.println("SE LLAMO AL PROCESO COMPLETO PARA EL CODIGO: " + codigo + " PARA SUCURSAL " + sucursal);
        return ResponseEntity.ok(servicio.productoCompletoPorCodigo(codigo, sucursal));
    }
    */
}