package com.stock.backend.service.producto;

import com.stock.backend.entity.Producto;
import com.stock.backend.exception.producto.ProductoExistenteException;
import com.stock.backend.repository.ProductoRepository;
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
