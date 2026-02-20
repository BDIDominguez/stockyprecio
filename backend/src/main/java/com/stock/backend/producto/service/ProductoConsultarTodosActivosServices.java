package com.stock.backend.producto.service;

import com.stock.backend.producto.entity.Producto;
import com.stock.backend.producto.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductoConsultarTodosActivosServices {
    private final ProductoRepository repository;

    public List<Producto> consultarTodos(){
        return repository.findAllByActivoTrue();
    }
}
