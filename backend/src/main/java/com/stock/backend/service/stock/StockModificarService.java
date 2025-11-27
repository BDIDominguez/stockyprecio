package com.stock.backend.service.stock;

import com.stock.backend.entity.Stock;
import com.stock.backend.repository.StockRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StockModificarService {

    private final StockRepository repository;

    public Stock sumar(Double cantidad, Long codigo, Long sucursal){
        Stock actual = repository.findByCodigoAndSucursal(codigo, sucursal).orElseThrow(() ->
                new RuntimeException("Error grave no esiste registro para el codigo " + codigo + " para la sucursal " + sucursal));
        actual.sumar(cantidad);
        return repository.save(actual);
    }

    public Stock restar(Double cantidad, Long codigo, Long sucursal){
        Stock actual = repository.findByCodigoAndSucursal(codigo, sucursal).orElseThrow(() ->
                new RuntimeException("Error grave no esiste registro para el codigo " + codigo + " para la sucursal " + sucursal));
        actual.restar(cantidad);
        return repository.save(actual);
    }

}
