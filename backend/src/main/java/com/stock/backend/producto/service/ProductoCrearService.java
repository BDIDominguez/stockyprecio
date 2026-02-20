package com.stock.backend.producto.service;

import com.stock.backend.producto.entity.Producto;
import com.stock.backend.producto.exception.ProductoExistenteException;
import com.stock.backend.producto.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductoCrearService {

    private final ProductoRepository repository;

    public Producto nuevo(Producto producto){
        repository.findByCodigo(producto.getCodigo())
                .ifPresent(p -> {
                    throw new ProductoExistenteException(
                            "Ya existe un producto con el código " + producto.getCodigo()
                    );
                });
        return repository.save(producto);
    }
}
