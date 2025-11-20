package com.stock.backend.service;

import com.stock.backend.entity.MovimientoTipo;
import com.stock.backend.repository.MovimientoTipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimientoTipoService {

    @Autowired
    private MovimientoTipoRepository repository;


    public List<MovimientoTipo> listarTodos() {
        return repository.findAll();
    }

    public MovimientoTipo nuevoMovimiento(MovimientoTipo nuevo) {
        System.out.println("Recibido en el Servicio " + nuevo.toString());
        MovimientoTipo respuesta = repository.save(nuevo);
        System.out.println("respuesta del repository " + respuesta);
        return respuesta;
    }
}
