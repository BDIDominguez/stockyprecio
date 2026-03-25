package com.stock.backend.producto.service;

import com.stock.backend.common.exception.CodigoRepetidoException;
import com.stock.backend.common.exception.OperacionNoValidaExeption;
import com.stock.backend.common.exception.RecursoNoEncontradoException;
import com.stock.backend.impuesto.repository.ImpuestoRepository;
import com.stock.backend.listaPrecio.repository.ListaPrecioRepository;
import com.stock.backend.producto.dto.*;
import com.stock.backend.producto.entity.*;
import com.stock.backend.producto.mapper.ProductoMapper;
import com.stock.backend.producto.mapper.ProductoNuevoMapper;
import com.stock.backend.producto.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ProductoComercialService {
    private final ProductoRepository productoRepository;
    private final ProductoCostoRepository productoCostoRepository;
    private final ProductoPrecioRepository productoPrecioRepository;
    private final ProductoImpuestoRepository productoImpuestoRepository;
    private final CodigoDeBarraRepository codigoDeBarraRepository;
    private final ProductoActualizacionRepository productoActualizacionRepository;
    private final ProductoActualizacionDatoRepository productoActualizacionDatoRepository;
    private final ProductoActualizacionCostoRepository productoActualizacionCostoRepository;
    private final ProductoActualizacionImpuestoRepository productoActualizacionImpuestoRepository;
    private final ProductoActualizacionPrecioRepository productoActualizacionPrecioRepository;
    private final ImpuestoRepository impuestoRepository;
    private final ListaPrecioRepository listaPrecioRepository;
    private final ProductoMapper productoMapper;
    private final ProductoNuevoMapper productoNuevoMapper;

    @Transactional
    public ProductoComercialDTO crear(ProductoComercialNuevoDTO dto) {
        validarAlta(dto);

        Producto producto = productoNuevoMapper.toEntidad(dto.producto());
        producto = productoRepository.save(producto);

        productoCostoRepository.save(ProductoCosto.builder()
                .codigoProducto(producto.getCodigo())
                .costo(dto.costo())
                .moneda(normalizarTexto(dto.moneda()))
                .build());

        guardarCodigosBarra(producto.getCodigo(), dto.codigosBarra());
        guardarImpuestos(producto.getCodigo(), dto.impuestos());
        guardarPrecios(producto.getCodigo(), dto.precios());

        return consultarPorCodigo(producto.getCodigo());
    }

    public ProductoComercialDTO consultarPorCodigo(Long codigo) {
        Producto producto = productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe producto con codigo: " + codigo));

        ProductoCosto costo = productoCostoRepository.findByCodigoProducto(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe costo vigente para el producto: " + codigo));

        List<String> codigosBarra = codigoDeBarraRepository.findAllByCodigoProducto(codigo).stream()
                .map(CodigoDeBarra::getBarra)
                .toList();

        List<String> impuestos = productoImpuestoRepository
                .findAllByCodigoProductoAndActivoTrueOrderByOrdenAplicacionAsc(codigo).stream()
                .map(ProductoImpuesto::getImpuesto)
                .toList();

        List<ProductoPrecioComercialDTO> precios = productoPrecioRepository.findAllByCodigoProductoAndActivoTrue(codigo)
                .stream()
                .map(precio -> new ProductoPrecioComercialDTO(
                        precio.getListaPrecio(),
                        precio.getModoCalculo(),
                        precio.getCostoBase(),
                        precio.getMargenPorcentaje(),
                        precio.getPrecioManual(),
                        precio.getSubtotalAntesImpuestos(),
                        precio.getImporteImpuestos(),
                        precio.getPrecioFinal()
                ))
                .toList();

        return new ProductoComercialDTO(
                productoMapper.toDto(producto),
                costo.getCosto(),
                costo.getMoneda(),
                codigosBarra,
                impuestos,
                precios
        );
    }

    @Transactional
    public ProductoComercialDTO modificar(Long codigo, ProductoComercialModificarDTO dto) {
        validarModificacion(codigo, dto);

        Producto producto = productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe producto con codigo: " + codigo));

        registrarActualizacionAplicada(codigo, dto);

        producto.actualizar(Producto.builder()
                .nombre(dto.producto().nombre())
                .descripcion(dto.producto().descripcion())
                .categoria(dto.producto().categoria())
                .proveedor(dto.producto().proveedor())
                .manejaStock(dto.producto().manejaStock())
                .activo(dto.producto().activo())
                .build());
        productoRepository.save(producto);

        ProductoCosto costo = productoCostoRepository.findByCodigoProducto(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe costo vigente para el producto: " + codigo));
        costo.actualizar(ProductoCosto.builder()
                .costo(dto.costo())
                .moneda(normalizarTexto(dto.moneda()))
                .build());
        productoCostoRepository.save(costo);

        reemplazarCodigosBarra(codigo, dto.codigosBarra());
        reemplazarImpuestos(codigo, dto.impuestos());
        reemplazarPrecios(codigo, dto.precios());

        return consultarPorCodigo(codigo);
    }

    private void validarAlta(ProductoComercialNuevoDTO dto) {
        Long codigo = dto.producto().codigo();
        productoRepository.findByCodigo(codigo).ifPresent(existe -> {
            throw new CodigoRepetidoException("Ya existe un producto con codigo: " + codigo);
        });
        validarComunes(dto.costo(), dto.codigosBarra(), dto.impuestos(), dto.precios(), Collections.emptySet());
    }

    private void validarModificacion(Long codigo, ProductoComercialModificarDTO dto) {
        productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe producto con codigo: " + codigo));

        Set<String> propias = codigoDeBarraRepository.findAllByCodigoProducto(codigo).stream()
                .map(CodigoDeBarra::getBarra)
                .collect(java.util.stream.Collectors.toSet());

        validarComunes(dto.costo(), dto.codigosBarra(), dto.impuestos(), dto.precios(), propias);
    }

    private void validarComunes(
            BigDecimal costo,
            List<String> barras,
            List<String> impuestos,
            List<ProductoPrecioComercialNuevoDTO> precios,
            Set<String> barrasPropias
    ) {
        if (costo == null) {
            throw new OperacionNoValidaExeption("El costo es obligatorio.");
        }

        validarBarras(barras, barrasPropias);
        validarImpuestos(impuestos);
        validarPrecios(precios);
    }

    private void validarBarras(List<String> barras, Set<String> barrasPropias) {
        Set<String> normalizadas = new HashSet<>();
        for (String barra : normalizarBarras(barras)) {
            if (!normalizadas.add(barra)) {
                throw new OperacionNoValidaExeption("Hay codigos de barra repetidos en la solicitud.");
            }
            codigoDeBarraRepository.findByBarra(barra).ifPresent(existe -> {
                if (!barrasPropias.contains(existe.getBarra())) {
                    throw new CodigoRepetidoException("Ya existe un codigo de barra: " + barra);
                }
            });
        }
    }

    private void validarImpuestos(List<String> impuestos) {
        if (impuestos == null) {
            return;
        }
        Set<String> codigos = new HashSet<>();
        for (String impuestoCodigo : impuestos) {
            String codigo = normalizarTexto(impuestoCodigo);
            if (codigo == null) {
                throw new OperacionNoValidaExeption("Los impuestos deben informar codigo.");
            }
            if (!codigos.add(codigo)) {
                throw new OperacionNoValidaExeption("Hay impuestos repetidos en la solicitud.");
            }
            impuestoRepository.findByCodigo(codigo)
                    .orElseThrow(() -> new RecursoNoEncontradoException("No existe impuesto con codigo: " + codigo));
        }
    }

    private void validarPrecios(List<ProductoPrecioComercialNuevoDTO> precios) {
        Set<String> listas = new HashSet<>();
        for (ProductoPrecioComercialNuevoDTO precio : precios) {
            String listaCodigo = normalizarTexto(precio.listaPrecio());
            if (listaCodigo == null) {
                throw new OperacionNoValidaExeption("La lista de precio debe informar codigo.");
            }
            if (!listas.add(listaCodigo)) {
                throw new OperacionNoValidaExeption("Hay listas de precios repetidas en la solicitud.");
            }
            listaPrecioRepository.findByCodigo(listaCodigo)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe lista de precio con codigo: " + listaCodigo));

            String modo = normalizarTexto(precio.modoCalculo());
            if (!"FIJO".equalsIgnoreCase(modo) && !"MARGEN".equalsIgnoreCase(modo)) {
                throw new OperacionNoValidaExeption("El modo de calculo debe ser FIJO o MARGEN.");
            }
            if ("FIJO".equalsIgnoreCase(modo) && precio.precioManual() == null) {
                throw new OperacionNoValidaExeption("Si el modo es FIJO debe informar precioManual.");
            }
            if ("MARGEN".equalsIgnoreCase(modo) && precio.margenPorcentaje() == null) {
                throw new OperacionNoValidaExeption("Si el modo es MARGEN debe informar margenPorcentaje.");
            }
        }
    }

    private void guardarCodigosBarra(Long codigoProducto, List<String> barras) {
        for (String barra : normalizarBarras(barras)) {
            codigoDeBarraRepository.save(CodigoDeBarra.builder()
                    .codigoProducto(codigoProducto)
                    .barra(barra)
                    .build());
        }
    }

    private void reemplazarCodigosBarra(Long codigoProducto, List<String> barras) {
        codigoDeBarraRepository.deleteAll(codigoDeBarraRepository.findAllByCodigoProducto(codigoProducto));
        guardarCodigosBarra(codigoProducto, barras);
    }

    private void guardarImpuestos(Long codigoProducto, List<String> impuestos) {
        if (impuestos == null) {
            return;
        }
        int orden = 1;
        for (String impuestoCodigo : impuestos) {
            productoImpuestoRepository.save(ProductoImpuesto.builder()
                    .codigoProducto(codigoProducto)
                    .impuesto(normalizarTexto(impuestoCodigo))
                    .ordenAplicacion(orden++)
                    .build());
        }
    }

    private void reemplazarImpuestos(Long codigoProducto, List<String> impuestos) {
        productoImpuestoRepository.deleteAll(productoImpuestoRepository.findAllByCodigoProducto(codigoProducto));
        guardarImpuestos(codigoProducto, impuestos);
    }

    private void guardarPrecios(Long codigoProducto, List<ProductoPrecioComercialNuevoDTO> precios) {
        for (ProductoPrecioComercialNuevoDTO precio : precios) {
            productoPrecioRepository.save(ProductoPrecio.builder()
                    .codigoProducto(codigoProducto)
                    .listaPrecio(normalizarTexto(precio.listaPrecio()))
                    .modoCalculo(normalizarTexto(precio.modoCalculo()))
                    .costoBase(precio.costoBase())
                    .margenPorcentaje(precio.margenPorcentaje())
                    .precioManual(precio.precioManual())
                    .subtotalAntesImpuestos(precio.subtotalAntesImpuestos())
                    .importeImpuestos(precio.importeImpuestos())
                    .precioFinal(precio.precioFinal())
                    .build());
        }
    }

    private void reemplazarPrecios(Long codigoProducto, List<ProductoPrecioComercialNuevoDTO> precios) {
        productoPrecioRepository.deleteAll(productoPrecioRepository.findAllByCodigoProducto(codigoProducto));
        guardarPrecios(codigoProducto, precios);
    }

    private void registrarActualizacionAplicada(Long codigoProducto, ProductoComercialModificarDTO dto) {
        ProductoActualizacion actualizacion = productoActualizacionRepository.save(ProductoActualizacion.builder()
                .codigoProducto(codigoProducto)
                .estado("APLICADO")
                .esProgramada(false)
                .fechaAplicacion(LocalDateTime.now())
                .motivo("Actualizacion inmediata de producto comercial")
                .build());

        productoActualizacionDatoRepository.save(ProductoActualizacionDato.builder()
                .actualizacion(actualizacion.getId())
                .nombre(dto.producto().nombre())
                .descripcion(dto.producto().descripcion())
                .categoria(dto.producto().categoria())
                .proveedor(dto.producto().proveedor())
                .activo(dto.producto().activo())
                .build());

        productoActualizacionCostoRepository.save(ProductoActualizacionCosto.builder()
                .actualizacion(actualizacion.getId())
                .costo(dto.costo())
                .moneda(normalizarTexto(dto.moneda()))
                .build());

        if (dto.impuestos() != null) {
            int orden = 1;
            for (String impuestoCodigo : dto.impuestos()) {
                productoActualizacionImpuestoRepository.save(ProductoActualizacionImpuesto.builder()
                        .actualizacion(actualizacion.getId())
                        .impuesto(normalizarTexto(impuestoCodigo))
                        .ordenAplicacion(orden++)
                        .build());
            }
        }

        for (ProductoPrecioComercialNuevoDTO precio : dto.precios()) {
            productoActualizacionPrecioRepository.save(ProductoActualizacionPrecio.builder()
                    .actualizacion(actualizacion.getId())
                    .listaPrecio(normalizarTexto(precio.listaPrecio()))
                    .modoCalculo(normalizarTexto(precio.modoCalculo()))
                    .costoBase(precio.costoBase())
                    .margenPorcentaje(precio.margenPorcentaje())
                    .precioManual(precio.precioManual())
                    .subtotalAntesImpuestos(precio.subtotalAntesImpuestos())
                    .importeImpuestos(precio.importeImpuestos())
                    .precioFinal(precio.precioFinal())
                    .build());
        }
    }

    private List<String> normalizarBarras(List<String> barras) {
        if (barras == null) {
            return Collections.emptyList();
        }
        return barras.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(valor -> !valor.isEmpty())
                .toList();
    }

    private String normalizarTexto(String valor) {
        if (valor == null) {
            return null;
        }
        String normalizado = valor.trim();
        return normalizado.isEmpty() ? null : normalizado;
    }
}
