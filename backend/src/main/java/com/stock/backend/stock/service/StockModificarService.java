package com.stock.backend.stock.service;

import com.stock.backend.stock.entity.Stock;
import com.stock.backend.stock.repository.StockRepository;
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
