package com.stock.backend.producto.service;

import com.stock.backend.producto.entity.Producto;
import com.stock.backend.producto.exception.ProductoExistenteException;
import com.stock.backend.producto.exception.ProductoInexistenteException;
import com.stock.backend.producto.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class ProductoModificarService {
    private final ProductoRepository repository;

    public Producto actualizarPorCodigo(Long codigo, Producto nuevo){
        Producto existe = repository.findByCodigo(codigo)
                .orElseThrow(() -> new ProductoInexistenteException("No existe producto con codigo: " + codigo));

        if (nuevo.getCodigo() != null && !Objects.equals(nuevo.getCodigo(), existe.getCodigo())) {
            repository.findByCodigo(nuevo.getCodigo()).ifPresent(producto -> {
                throw new ProductoExistenteException("Ya existe un producto con el codigo " + nuevo.getCodigo());
            });
        }

        existe.actualizarCampos(nuevo);
        return repository.save(existe);
    }
}
