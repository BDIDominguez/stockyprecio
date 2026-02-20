package com.stock.backend.stock.service;

import com.stock.backend.stock.entity.Stock;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class StockFacadeService {

    private final StockConsultaService consultaService;
    private final StockModificarService modificarService;
    private final StockCrearService crearService;

    public Optional<Stock> consultar(Long codigo, Long sucursal){
        return consultaService.consultar(codigo,sucursal);
    }

    public Stock sumar(Double cantidad, Long sucursal, Long codigo){
        return modificarService.sumar(cantidad, sucursal, codigo);
    }

    public Stock restar(Double cantidad, Long sucursal, Long codigo){
        return modificarService.restar(cantidad, sucursal, codigo);
    }

    public Stock guardar(Stock nuevo){
        return crearService.guardar(nuevo);
    }

}
