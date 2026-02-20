package com.stock.backend.stock.service;

import com.stock.backend.stock.entity.Stock;
import com.stock.backend.stock.repository.StockRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StockCrearService {

    private final StockRepository repository;

    public Stock guardar(Stock nuevo){
        return repository.save(nuevo);
    }
}
