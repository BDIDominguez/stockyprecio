package com.stock.backend.movimientoTipo.service;

import com.stock.backend.movimientoTipo.entity.MovimientoTipo;
import com.stock.backend.movimientoTipo.exception.MovimientosTipoInexistente;
import com.stock.backend.movimientoTipo.repository.MovimientoTipoRepository;
import org.springframework.stereotype.Service;

@Service
public class MovimientoTipoConsultaPorSiglasService {
    private final MovimientoTipoRepository repository;

    public MovimientoTipoConsultaPorSiglasService(MovimientoTipoRepository repository) {
        this.repository = repository;
    }

    public MovimientoTipo consulta(String siglas){
        MovimientoTipo respuesta = repository.findBySiglas(siglas).orElseThrow(() -> new MovimientosTipoInexistente("No existen un movimiento con las siglas = "+siglas));
        return respuesta;
    }
}
