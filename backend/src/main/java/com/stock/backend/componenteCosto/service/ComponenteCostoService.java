package com.stock.backend.componenteCosto.service;

import com.stock.backend.common.exception.CodigoRepetidoException;
import com.stock.backend.common.exception.OperacionNoValidaExeption;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.common.exception.RecursoEnUsoException;
import com.stock.backend.componenteCosto.entity.ComponenteCosto;
import com.stock.backend.componenteCosto.repository.ComponenteCostoRepository;
import com.stock.backend.producto.repository.ProductoComponenteCostoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ComponenteCostoService {
    private static final String ETAPA_1 = "ETAPA_1";
    private static final String ETAPA_2 = "ETAPA_2";
    private static final String ETAPA_FINAL = "ETAPA_FINAL";
    private static final String TIPO_PORCENTAJE = "PORCENTAJE";
    private static final String TIPO_FIJO = "FIJO";

    private final ComponenteCostoRepository repository;
    private final ProductoComponenteCostoRepository productoComponenteCostoRepository;

    public Page<ComponenteCosto> consultarTodos(Boolean activo, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return repository.findByActivo(activo, pageable);
    }

    public Page<ComponenteCosto> buscarPorNombreIgnoreCase(String nombre, Boolean activo, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        return repository.findByNombreContainingIgnoreCaseAndActivo(nombre, activo, pageable);
    }

    public Optional<ComponenteCosto> buscarPorCodigo(Long codigo) {
        return repository.findByCodigo(codigo);
    }

    public ComponenteCosto consultarPorCodigo(Long codigo) {
        return repository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe componente de costo con codigo: " + codigo));
    }

    public ComponenteCosto crear(ComponenteCosto datos) {
        repository.findByCodigo(datos.getCodigo()).ifPresent(existe -> {
            throw new CodigoRepetidoException("Ya existe un componente de costo con codigo: " + datos.getCodigo());
        });
        normalizarYValidar(datos);
        return repository.save(datos);
    }

    public ComponenteCosto modificar(Long codigo, ComponenteCosto datos) {
        ComponenteCosto existe = consultarPorCodigo(codigo);
        if (datos.getCodigo() != null && !datos.getCodigo().equals(existe.getCodigo())) {
            repository.findByCodigo(datos.getCodigo()).ifPresent(otro -> {
                throw new CodigoRepetidoException("Ya existe un componente de costo con codigo: " + datos.getCodigo());
            });
        }
        existe.actualizar(datos);
        normalizarYValidar(existe);
        return repository.save(existe);
    }

    public void desactivar(Long codigo) {
        ComponenteCosto existe = consultarPorCodigo(codigo);
        if (productoComponenteCostoRepository.existsByComponente(codigo)) {
            throw new RecursoEnUsoException(
                    "No se puede desactivar el componente " + codigo + " porque esta en uso por productos activos."
            );
        }
        existe.setActivo(false);
        repository.save(existe);
    }

    public void activar(Long codigo) {
        ComponenteCosto existe = consultarPorCodigo(codigo);
        existe.setActivo(true);
        repository.save(existe);
    }

    public Long siguienteCodigo() {
        ComponenteCosto componenteCosto = repository.findTopByOrderByCodigoDesc();
        if (componenteCosto == null || componenteCosto.getCodigo() == null) {
            return 1L;
        }
        return componenteCosto.getCodigo() + 1;
    }

    private void normalizarYValidar(ComponenteCosto componente) {
        componente.setNombre(normalizarObligatorio(componente.getNombre(), "nombre"));
        componente.setDescripcion(normalizarOpcional(componente.getDescripcion()));
        componente.setTipoComponente(normalizarObligatorio(componente.getTipoComponente(), "tipoComponente"));
        componente.setTipoValor(normalizarTipoValor(componente.getTipoValor()));
        componente.setEtapaAplicacion(normalizarEtapa(componente.getEtapaAplicacion()));
        if (componente.getValorDefecto() == null) {
            throw new OperacionNoValidaExeption("El valor por defecto es obligatorio.");
        }
        componente.setValorDefecto(componente.getValorDefecto().setScale(4, java.math.RoundingMode.HALF_UP));
        if (componente.getEditableEnProducto() == null) {
            throw new OperacionNoValidaExeption("Debe indicar si el componente es editable en producto.");
        }
        if (componente.getActivo() == null) {
            componente.setActivo(true);
        }
    }

    private String normalizarTipoValor(String tipoValor) {
        String normalizado = normalizarObligatorio(tipoValor, "tipoValor").toUpperCase();
        if (!TIPO_PORCENTAJE.equals(normalizado) && !TIPO_FIJO.equals(normalizado)) {
            throw new OperacionNoValidaExeption("Tipo de valor no soportado: " + tipoValor);
        }
        return normalizado;
    }

    private String normalizarEtapa(String etapaAplicacion) {
        String normalizada = normalizarObligatorio(etapaAplicacion, "etapaAplicacion").toUpperCase();
        if (!ETAPA_1.equals(normalizada) && !ETAPA_2.equals(normalizada) && !ETAPA_FINAL.equals(normalizada)) {
            throw new OperacionNoValidaExeption("Etapa de aplicacion no soportada: " + etapaAplicacion);
        }
        return normalizada;
    }

    private String normalizarObligatorio(String valor, String campo) {
        String normalizado = normalizarOpcional(valor);
        if (normalizado == null) {
            throw new OperacionNoValidaExeption("El campo " + campo + " es obligatorio.");
        }
        return normalizado;
    }

    private String normalizarOpcional(String valor) {
        if (valor == null) {
            return null;
        }
        String normalizado = valor.trim();
        return normalizado.isEmpty() ? null : normalizado;
    }
}
