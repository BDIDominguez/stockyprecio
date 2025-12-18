package com.stock.backend.service.movimientotipo;

import com.stock.backend.entity.MovimientoTipo;
import com.stock.backend.repository.MovimientoTipoRepository;
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
