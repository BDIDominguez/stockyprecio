package com.stock.backend.stock.service;

import com.stock.backend.stock.entity.Stock;
import com.stock.backend.stock.repository.StockRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StockConsultaService {

    public final StockRepository repository;

    public Optional<Stock> consultar(Long codigo, Long sucursal){
        return repository.findByCodigoAndSucursal(codigo,sucursal);
    }
}
