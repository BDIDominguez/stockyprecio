package com.stock.backend.producto.service;

import com.stock.backend.producto.entity.Producto;
import com.stock.backend.producto.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductoConsultaPorCodigoService {
    private final ProductoRepository repository;

    public Optional<Producto> consultar(Long codigo){
        return repository.findByCodigo(codigo);
    }

    public Long siguienteCodigo() {
        Producto producto = repository.findTopByOrderByCodigoDesc();
        if (producto == null || producto.getCodigo() == null) {
            return 1L;
        }
        return producto.getCodigo() + 1;
    }
}
