package com.stock.backend.producto.service;

import com.stock.backend.producto.entity.Producto;
import com.stock.backend.producto.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductoConsultarPorIdService {
    private final ProductoRepository repository;

    public Optional<Producto> consultar(Long id){
        return repository.findById(id);
    }
}
