package com.stock.backend.movimientoTipo.service;

import com.stock.backend.movimientoTipo.entity.MovimientoTipo;
import com.stock.backend.movimientoTipo.repository.MovimientoTipoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MovimientoTipoConsultaTodosService {
    private final MovimientoTipoRepository repository;

    public List<MovimientoTipo> consultarTodos(){
        return repository.findAll();
    }
}
