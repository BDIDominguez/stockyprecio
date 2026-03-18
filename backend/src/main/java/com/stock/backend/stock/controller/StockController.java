package com.stock.backend.stock.controller;

import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.stock.dto.StockDTO;
import com.stock.backend.stock.entity.Stock;
import com.stock.backend.stock.mapper.StockMapper;
import com.stock.backend.stock.service.StockService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stock")
@AllArgsConstructor
public class StockController {


    private final StockService service;
    private final StockMapper mapper;

    @GetMapping("")
    public ResponseEntity<StockDTO> consultarCodigoSucursal(@RequestParam Long codigo, @RequestParam Long sucursal){
        StockDTO respuesta = mapper.toDto(service.buscarPorCodigoySucursal(codigo, sucursal).orElseThrow(()->
                new RecursoNoEncontradoException("No existe stock para el codigo " + codigo + " de la sucursal " + sucursal)));
        return ResponseEntity.ok(respuesta);
    }

}
