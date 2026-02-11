package com.stock.backend.service.stock;

import com.stock.backend.entity.Stock;
import com.stock.backend.repository.StockRepository;
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
