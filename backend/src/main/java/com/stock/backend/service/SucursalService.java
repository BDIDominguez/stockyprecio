package com.stock.backend.service;

import com.stock.backend.entity.Sucursal;
import com.stock.backend.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SucursalService {

    @Autowired
    private SucursalRepository repository;


    public List<Sucursal> consultarTodas() {
        return repository.findAll();
    }

    public Optional<Sucursal> consultarPorSucursal(Long sid) {
        return repository.findByCodigo(sid);
    }
}
