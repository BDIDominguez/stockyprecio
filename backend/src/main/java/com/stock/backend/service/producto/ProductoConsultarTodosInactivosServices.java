package com.stock.backend.service.producto;

import com.stock.backend.entity.Producto;
import com.stock.backend.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductoConsultarTodosInactivosServices {
    private final ProductoRepository repository;

    public List<Producto> consultarTodos(){
        return repository.findAllByActivoFalse();
    }
}
