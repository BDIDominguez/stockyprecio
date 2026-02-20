package com.stock.backend.stock.service;

import com.stock.backend.stock.entity.Stock;
import com.stock.backend.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository repository;


    public List<Stock> consultarTodos() {
        return repository.findAll();
    }

    public Optional<Stock> consultarPorId(Long id) {
        return repository.findById(id);
    }

    public Stock buscarPorCodigoySucursal(Long codigo, Long sucursal) {
        Optional<Stock> respuesta = repository.findByCodigoAndSucursal(codigo, sucursal);
        if (respuesta.isPresent()){
            return null;
        }
        return respuesta.get();
    }

    public Stock crearStockNuevo(Stock stock) {
        return repository.save(stock);
    }
}
