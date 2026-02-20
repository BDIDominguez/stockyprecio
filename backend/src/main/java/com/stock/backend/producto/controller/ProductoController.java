package com.stock.backend.producto.controller;

import com.stock.backend.producto.dto.ProductoDTO;
import com.stock.backend.producto.dto.ProductoNuevoDTO;
import com.stock.backend.producto.entity.Producto;
import com.stock.backend.producto.mapper.ProductoMapper;
import com.stock.backend.producto.mapper.ProductoNuevoMapper;
import com.stock.backend.producto.service.ProductoFacadeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@AllArgsConstructor
public class ProductoController {

    //private ProductoService servicio;
    private final ProductoFacadeService servicio;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listarProductos(){
        List<Producto> lista = servicio.consultarTodosActivos();
        List<ProductoDTO> dtos = lista.stream().map(ProductoMapper::toDto).toList();
        return  ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> productoPorId(@PathVariable Long id){
        Optional<Producto> prod = servicio.consultarPorId(id);
        if (prod.isPresent()){
            ProductoDTO respuesta = ProductoMapper.toDto(prod.get());
            return ResponseEntity.ok(respuesta);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoDTO> productoPorCodigo(@PathVariable Long codigo){
        Optional<Producto> prod = servicio.consultarPorCodigo(codigo);
        if (prod.isPresent()){
            ProductoDTO respuesta = ProductoMapper.toDto(prod.get());
            return ResponseEntity.ok(respuesta);
        }
        return ResponseEntity.notFound().build();
    }

    /*
    @PostMapping("") // se necesita crear un orquestador que use tanto StockService y ProdcutoService todabia no existe pero pronto
    public ResponseEntity<ProductoDTO> crearProcducto(@Validated @RequestBody ProductoNuevoDTO producto){
        Producto nuevo = servicio.nuevoProducto(producto.convertir(), producto.sucursal());
        ProductoDTO respuesta = new ProductoDTO(nuevo);
        return ResponseEntity.ok(respuesta);
    } */

    @PutMapping("/id/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto (@PathVariable Long id,@Validated @RequestBody ProductoNuevoDTO producto){
        Producto respuesta = servicio.actualizar(id, ProductoNuevoMapper.toEntidad(producto));
        return ResponseEntity.ok(ProductoMapper.toDto(respuesta));
    }
    /*
    @GetMapping("/completo/{codigo}/sucursal/{sucursal}")  // Esto Pertenece a otro lado no aqui
    public ResponseEntity<ProductoCompletoDTO> productoCompleto(@PathVariable Long codigo, @PathVariable Long sucursal){
        System.out.println("SE LLAMO AL PROCESO COMPLETO PARA EL CODIGO: " + codigo + " PARA SUCURSAL " + sucursal);
        return ResponseEntity.ok(servicio.productoCompletoPorCodigo(codigo, sucursal));
    }
    */
}