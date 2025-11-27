package com.stock.backend.service.stock;

import com.stock.backend.entity.Stock;
import com.stock.backend.repository.StockRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StockConsultaService {

    public final StockRepository repository;

    public Optional<Stock> consultar(String codigo){
        return repository.findByCodigo(codigo);
    }
}
