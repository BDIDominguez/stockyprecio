package com.stock.backend.service.stock;

import com.stock.backend.entity.Stock;
import com.stock.backend.repository.StockRepository;
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
