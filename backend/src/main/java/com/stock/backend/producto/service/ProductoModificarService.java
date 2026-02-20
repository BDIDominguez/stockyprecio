package com.stock.backend.producto.service;

import com.stock.backend.producto.entity.Producto;
import com.stock.backend.producto.exception.ProductoInexistenteException;
import com.stock.backend.producto.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductoModificarService {
    private final ProductoRepository repository;

    public Producto actualizar(Long id, Producto nuevo){
        Producto existe = repository.findById(nuevo.getId())
                .orElseThrow(() -> new ProductoInexistenteException("No existe producto con ID: " + nuevo.getId()));
        existe.actualizarCampos(nuevo);
        return repository.save(existe);
    }
}
