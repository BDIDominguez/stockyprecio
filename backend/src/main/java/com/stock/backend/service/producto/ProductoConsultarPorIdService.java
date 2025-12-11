package com.stock.backend.service.producto;

import com.stock.backend.entity.Producto;
import com.stock.backend.repository.ProductoRepository;
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
