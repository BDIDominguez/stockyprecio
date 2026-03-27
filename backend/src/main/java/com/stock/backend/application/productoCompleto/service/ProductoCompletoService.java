package com.stock.backend.application.productoCompleto.service;

import com.stock.backend.application.productoCompleto.dto.*;
import com.stock.backend.common.exception.*;
import com.stock.backend.componenteCosto.entity.ComponenteCosto;
import com.stock.backend.componenteCosto.repository.ComponenteCostoRepository;
import com.stock.backend.listaPrecio.repository.ListaPrecioRepository;
import com.stock.backend.producto.entity.*;
import com.stock.backend.producto.mapper.ProductoMapper;
import com.stock.backend.producto.mapper.ProductoNuevoMapper;
import com.stock.backend.producto.repository.*;
import com.stock.backend.stock.dto.StockDTO;
import com.stock.backend.stock.mapper.StockMapper;
import com.stock.backend.stock.service.StockService;
import com.stock.backend.sucursal.repository.SucursalRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductoCompletoService {
    private static final BigDecimal CIEN = new BigDecimal("100");
    private static final BigDecimal TOLERANCIA_COSTO = new BigDecimal("0.0001");
    private static final BigDecimal TOLERANCIA_PRECIO = new BigDecimal("0.01");
    private static final String ETAPA_1 = "ETAPA_1";
    private static final String ETAPA_2 = "ETAPA_2";
    private static final String ETAPA_FINAL = "ETAPA_FINAL";
    private static final String RESULTADO_NETO1 = "NETO1";
    private static final String RESULTADO_NETO2 = "NETO2";
    private static final String RESULTADO_COSTO_FINAL = "COSTO_FINAL";
    private static final String TIPO_FIJO = "FIJO";
    private static final String TIPO_PORCENTAJE = "PORCENTAJE";

    private final ProductoRepository productoRepository;
    private final ProductoCostoRepository productoCostoRepository;
    private final ProductoComponenteCostoRepository productoComponenteCostoRepository;
    private final ProductoCostoDetalleRepository productoCostoDetalleRepository;
    private final ProductoPrecioRepository productoPrecioRepository;
    private final CodigoDeBarraRepository codigoDeBarraRepository;
    private final ComponenteCostoRepository componenteCostoRepository;
    private final ListaPrecioRepository listaPrecioRepository;
    private final ProductoMapper productoMapper;
    private final ProductoNuevoMapper productoNuevoMapper;
    private final StockService stockService;
    private final StockMapper stockMapper;
    private final SucursalRepository sucursalRepository;

    public Page<ProductoCompletoResumenDTO> consultarTodos(Long codigo, Long sucursal, String nombre, Boolean activo, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (codigo != null) {
            return productoRepository.findByCodigoContainingAndActivo(String.valueOf(codigo), activo, pageable)
                    .map(producto -> construirResumen(producto, sucursal));
        }
        if (nombre != null) {
            return productoRepository.findByNombreContainingIgnoreCaseAndActivo(nombre, activo, pageable)
                    .map(producto -> construirResumen(producto, sucursal));
        }
        return productoRepository.findByActivo(activo, pageable)
                .map(producto -> construirResumen(producto, sucursal));
    }

    @Transactional
    public ProductoCompletoDTO crear(ProductoCompletoNuevoDTO dto) {
        validarAlta(dto);
        CalculoValidado calculo = recalcularYValidar(dto.costo(), dto.precios(), dto.confirmarBajoCosto());
        Producto producto = productoRepository.save(productoNuevoMapper.toEntidad(dto.producto()));
        guardarCosto(producto.getCodigo(), calculo);
        reemplazarConfiguracionComponentes(producto.getCodigo(), calculo.componentes());
        reemplazarDetallesCosto(producto.getCodigo(), calculo.componentes(), calculo.fechaCalculo());
        guardarCodigosBarra(producto.getCodigo(), dto.codigosBarra());
        reemplazarPrecios(producto.getCodigo(), calculo.precios(), calculo.fechaCalculo());
        crearStocksIniciales(producto.getCodigo(), dto.reservaInicial());
        return consultarPorCodigo(producto.getCodigo(), null);
    }

    public ProductoCompletoDTO consultarPorCodigo(Long codigo, Long sucursal) {
        Producto producto = productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe producto con codigo: " + codigo));
        ProductoCosto costo = productoCostoRepository.findByProducto(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe costo vigente para el producto: " + codigo));
        List<ProductoCostoDetalle> detalles = productoCostoDetalleRepository.findAllByProductoOrderByEtapaAplicacionAscComponenteAsc(codigo);
        Map<Long, String> nombresComponentes = obtenerNombresComponentes(detalles);
        List<String> codigosBarra = codigoDeBarraRepository.findAllByCodigoProducto(codigo).stream().map(CodigoDeBarra::getBarra).toList();
        StockDTO stock = null;
        if (sucursal != null) {
            stock = stockService.buscarPorCodigoYSucursal(codigo, sucursal)
                    .map(stockMapper::toDto)
                    .orElseThrow(() -> new RecursoNoEncontradoException("No existe stock para el codigo " + codigo + " en la sucursal " + sucursal));
        }
        return new ProductoCompletoDTO(productoMapper.toDto(producto), construirCostoDto(costo, detalles, nombresComponentes), codigosBarra, stock, construirPreciosDto(codigo));
    }

    @Transactional
    public ProductoCompletoDTO modificar(Long codigo, ProductoCompletoModificarDTO dto) {
        validarModificacion(codigo, dto);
        CalculoValidado calculo = recalcularYValidar(dto.costo(), dto.precios(), dto.confirmarBajoCosto());
        Producto producto = productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe producto con codigo: " + codigo));
        producto.actualizar(Producto.builder()
                .nombre(dto.producto().nombre())
                .descripcion(dto.producto().descripcion())
                .categoria(dto.producto().categoria())
                .proveedor(dto.producto().proveedor())
                .manejaStock(dto.producto().manejaStock())
                .activo(dto.producto().activo())
                .build());
        productoRepository.save(producto);
        guardarCosto(codigo, calculo);
        reemplazarConfiguracionComponentes(codigo, calculo.componentes());
        reemplazarDetallesCosto(codigo, calculo.componentes(), calculo.fechaCalculo());
        reemplazarCodigosBarra(codigo, dto.codigosBarra());
        reemplazarPrecios(codigo, calculo.precios(), calculo.fechaCalculo());
        actualizarReservaSiCorresponde(codigo, dto);
        return consultarPorCodigo(codigo, dto.sucursal());
    }

    private ProductoCompletoResumenDTO construirResumen(Producto producto, Long sucursal) {
        ProductoCosto costo = productoCostoRepository.findByProducto(producto.getCodigo()).orElse(null);
        StockDTO stock = null;
        if (sucursal != null) {
            stock = stockService.buscarPorCodigoYSucursal(producto.getCodigo(), sucursal).map(stockMapper::toDto).orElse(null);
        }
        return new ProductoCompletoResumenDTO(productoMapper.toDto(producto), costo != null ? costo.getCostoFinal() : null, costo != null ? costo.getMoneda() : null, stock, construirPreciosDto(producto.getCodigo()));
    }

    private void validarAlta(ProductoCompletoNuevoDTO dto) {
        Long codigo = dto.producto().codigo();
        productoRepository.findByCodigo(codigo).ifPresent(existe -> {
            throw new CodigoRepetidoException("Ya existe un producto con codigo: " + codigo);
        });
        validarReservaInicial(dto.reservaInicial());
        validarComunes(dto.costo(), dto.codigosBarra(), dto.precios(), Collections.emptySet());
    }

    private void validarModificacion(Long codigo, ProductoCompletoModificarDTO dto) {
        productoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe producto con codigo: " + codigo));
        Set<String> propias = codigoDeBarraRepository.findAllByCodigoProducto(codigo).stream().map(CodigoDeBarra::getBarra).collect(Collectors.toSet());
        validarComunes(dto.costo(), dto.codigosBarra(), dto.precios(), propias);
    }

    private void validarComunes(ProductoCompletoCostoNuevoDTO costo, List<String> barras, List<ProductoCompletoPrecioNuevoDTO> precios, Set<String> barrasPropias) {
        if (costo == null) {
            throw new OperacionNoValidaExeption("El costo compuesto es obligatorio.");
        }
        validarBarras(barras, barrasPropias);
        validarComponentesCosto(costo.componentes());
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

    private void validarComponentesCosto(List<ProductoCompletoCostoComponenteNuevoDTO> componentes) {
        List<ProductoCompletoCostoComponenteNuevoDTO> lista = componentes == null ? Collections.emptyList() : componentes;
        Set<Long> codigos = new HashSet<>();
        for (ProductoCompletoCostoComponenteNuevoDTO componente : lista) {
            if (!codigos.add(componente.componente())) {
                throw new OperacionNoValidaExeption("Hay componentes repetidos en la solicitud.");
            }
            ComponenteCosto maestro = componenteCostoRepository.findByCodigo(componente.componente())
                    .orElseThrow(() -> new RecursoNoEncontradoException("No existe componente de costo con codigo: " + componente.componente()));
            if (!maestro.isActivo()) {
                throw new OperacionNoValidaExeption("El componente de costo " + componente.componente() + " no esta activo.");
            }
            if (!Boolean.TRUE.equals(maestro.getEditableEnProducto()) && escala4(componente.valorAplicado()).compareTo(escala4(maestro.getValorDefecto())) != 0) {
                throw new OperacionNoValidaExeption("El componente " + maestro.getCodigo() + " no permite editar el valor aplicado.");
            }
        }
    }

    private void validarPrecios(List<ProductoCompletoPrecioNuevoDTO> precios) {
        Set<Long> listas = new HashSet<>();
        for (ProductoCompletoPrecioNuevoDTO precio : precios) {
            Long listaCodigo = precio.listaPrecio();
            if (!listas.add(listaCodigo)) {
                throw new OperacionNoValidaExeption("Hay listas de precios repetidas en la solicitud.");
            }
            listaPrecioRepository.findByCodigo(listaCodigo)
                    .orElseThrow(() -> new RecursoNoEncontradoException("No existe lista de precio con codigo: " + listaCodigo));
        }
    }

    private CalculoValidado recalcularYValidar(ProductoCompletoCostoNuevoDTO costoDto, List<ProductoCompletoPrecioNuevoDTO> preciosDto, Boolean confirmarBajoCosto) {
        CostoValidado costo = recalcularCosto(costoDto);
        List<PrecioValidado> precios = recalcularPrecios(costo.costoFinal(), preciosDto, Boolean.TRUE.equals(confirmarBajoCosto));
        return new CalculoValidado(costo, precios, LocalDateTime.now());
    }

    private CostoValidado recalcularCosto(ProductoCompletoCostoNuevoDTO costoDto) {
        BigDecimal costoBase = escala4(costoDto.costoBase());
        List<ComponenteEntrada> componentes = cargarComponentes(costoDto.componentes());
        EtapaCalculada etapa1 = procesarEtapa(componentes, ETAPA_1, costoBase, RESULTADO_NETO1);
        compararCosto("costo.neto1", costoDto.neto1(), etapa1.resultadoEtapa());
        EtapaCalculada etapa2 = procesarEtapa(componentes, ETAPA_2, etapa1.resultadoEtapa(), RESULTADO_NETO2);
        compararCosto("costo.neto2", costoDto.neto2(), etapa2.resultadoEtapa());
        EtapaCalculada etapaFinal = procesarEtapa(componentes, ETAPA_FINAL, etapa2.resultadoEtapa(), RESULTADO_COSTO_FINAL);
        compararCosto("costo.costoFinal", costoDto.costoFinal(), etapaFinal.resultadoEtapa());
        List<ComponenteValidado> componentesCalculados = new ArrayList<>();
        componentesCalculados.addAll(etapa1.componentes());
        componentesCalculados.addAll(etapa2.componentes());
        componentesCalculados.addAll(etapaFinal.componentes());
        return new CostoValidado(costoBase, etapa1.resultadoEtapa(), etapa2.resultadoEtapa(), etapaFinal.resultadoEtapa(), normalizarMoneda(costoDto.moneda()), componentesCalculados);
    }

    private List<ComponenteEntrada> cargarComponentes(List<ProductoCompletoCostoComponenteNuevoDTO> componentesDto) {
        List<ProductoCompletoCostoComponenteNuevoDTO> origen = componentesDto == null ? List.of() : componentesDto;
        List<ComponenteEntrada> componentes = new ArrayList<>();
        for (ProductoCompletoCostoComponenteNuevoDTO dto : origen) {
            ComponenteCosto maestro = componenteCostoRepository.findByCodigo(dto.componente())
                    .orElseThrow(() -> new RecursoNoEncontradoException("No existe componente de costo con codigo: " + dto.componente()));
            componentes.add(new ComponenteEntrada(maestro, dto));
        }
        return componentes;
    }

    private EtapaCalculada procesarEtapa(List<ComponenteEntrada> componentes, String etapaAplicacion, BigDecimal baseEtapa, String resultadoEtapa) {
        List<ComponenteEntrada> deEtapa = componentes.stream()
                .filter(entrada -> etapaAplicacion.equals(normalizarEtapa(entrada.maestro().getEtapaAplicacion())))
                .sorted((a, b) -> a.maestro().getCodigo().compareTo(b.maestro().getCodigo()))
                .toList();
        BigDecimal acumuladoEtapa = baseEtapa;
        List<ComponenteValidado> calculados = new ArrayList<>();
        for (int i = 0; i < deEtapa.size(); i++) {
            ComponenteEntrada entrada = deEtapa.get(i);
            ComponenteCosto maestro = entrada.maestro();
            ProductoCompletoCostoComponenteNuevoDTO componente = entrada.dto();
            BigDecimal valorAplicado = escala4(componente.valorAplicado());
            String tipoValor = normalizarTipoValor(maestro.getTipoValor());
            if (!Boolean.TRUE.equals(maestro.getEditableEnProducto()) && valorAplicado.compareTo(escala4(maestro.getValorDefecto())) != 0) {
                throw new CalculoInconsistenteException("El componente " + maestro.getCodigo() + " no admite un valor distinto de su valor por defecto.");
            }
            compararCosto("costo.componentes[" + i + "].baseCalculo", componente.baseCalculo(), baseEtapa);
            BigDecimal importeCalculado = TIPO_FIJO.equals(tipoValor) ? valorAplicado : porcentajeDe(baseEtapa, valorAplicado);
            BigDecimal subtotalResultante = escala4(acumuladoEtapa.add(importeCalculado));
            compararCosto("costo.componentes[" + i + "].importeCalculado", componente.importeCalculado(), importeCalculado);
            compararCosto("costo.componentes[" + i + "].subtotalResultante", componente.subtotalResultante(), subtotalResultante);
            calculados.add(new ComponenteValidado(maestro.getCodigo(), maestro.getNombre(), etapaAplicacion, resultadoEtapa, tipoValor, valorAplicado, baseEtapa, importeCalculado, subtotalResultante));
            acumuladoEtapa = subtotalResultante;
        }
        return new EtapaCalculada(acumuladoEtapa, calculados);
    }

    private List<PrecioValidado> recalcularPrecios(BigDecimal costoFinal, List<ProductoCompletoPrecioNuevoDTO> preciosDto, boolean confirmarBajoCosto) {
        List<PrecioValidado> precios = new ArrayList<>();
        for (int i = 0; i < preciosDto.size(); i++) {
            ProductoCompletoPrecioNuevoDTO precio = preciosDto.get(i);
            BigDecimal margen = escala4(precio.margenPorcentaje());
            BigDecimal precioVenta = escala2(precio.precioVenta());
            BigDecimal esperado = costoFinal.signum() == 0
                    ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                    : escala2(costoFinal.multiply(BigDecimal.ONE.add(margen.divide(CIEN, 8, RoundingMode.HALF_UP))));
            compararPrecio("precios[" + i + "].precioVenta", precio.precioVenta(), esperado);
            if (precioVenta.compareTo(costoFinal) < 0 && !confirmarBajoCosto) {
                throw new ConfirmacionRequeridaException("La lista " + precio.listaPrecio() + " tiene un precio de venta menor al costo final.");
            }
            precios.add(new PrecioValidado(precio.listaPrecio(), costoFinal, margen, precioVenta));
        }
        return precios;
    }

    private void guardarCosto(Long producto, CalculoValidado calculo) {
        ProductoCosto costo = productoCostoRepository.findByProducto(producto).orElse(ProductoCosto.builder().producto(producto).build());
        costo.actualizar(ProductoCosto.builder()
                .costoBase(calculo.costo().costoBase())
                .neto1(calculo.costo().neto1())
                .neto2(calculo.costo().neto2())
                .costoFinal(calculo.costo().costoFinal())
                .moneda(calculo.costo().moneda())
                .fechaCalculo(calculo.fechaCalculo())
                .activo(true)
                .build());
        productoCostoRepository.save(costo);
    }

    private void reemplazarConfiguracionComponentes(Long producto, List<ComponenteValidado> componentes) {
        productoComponenteCostoRepository.deleteAllByProducto(producto);
        for (ComponenteValidado componente : componentes) {
            productoComponenteCostoRepository.save(ProductoComponenteCosto.builder()
                    .producto(producto)
                    .componente(componente.componente())
                    .valorAplicado(componente.valorAplicado())
                    .activo(true)
                    .build());
        }
    }

    private void reemplazarDetallesCosto(Long producto, List<ComponenteValidado> componentes, LocalDateTime fechaCalculo) {
        productoCostoDetalleRepository.deleteAllByProducto(producto);
        for (ComponenteValidado componente : componentes) {
            productoCostoDetalleRepository.save(ProductoCostoDetalle.builder()
                    .producto(producto)
                    .componente(componente.componente())
                    .etapaAplicacion(componente.etapaAplicacion())
                    .resultadoEtapa(componente.resultadoEtapa())
                    .tipoValorAplicado(componente.tipoValorAplicado())
                    .valorAplicado(componente.valorAplicado())
                    .baseCalculo(componente.baseCalculo())
                    .importeCalculado(componente.importeCalculado())
                    .subtotalResultante(componente.subtotalResultante())
                    .fechaCalculo(fechaCalculo)
                    .build());
        }
    }

    private void guardarCodigosBarra(Long codigoProducto, List<String> barras) {
        for (String barra : normalizarBarras(barras)) {
            codigoDeBarraRepository.save(CodigoDeBarra.builder().codigoProducto(codigoProducto).barra(barra).build());
        }
    }

    private void reemplazarCodigosBarra(Long codigoProducto, List<String> barras) {
        codigoDeBarraRepository.deleteAll(codigoDeBarraRepository.findAllByCodigoProducto(codigoProducto));
        guardarCodigosBarra(codigoProducto, barras);
    }

    private void reemplazarPrecios(Long producto, List<PrecioValidado> precios, LocalDateTime fechaCalculo) {
        productoPrecioRepository.deleteAll(productoPrecioRepository.findAllByProducto(producto));
        for (PrecioValidado precio : precios) {
            productoPrecioRepository.save(ProductoPrecio.builder()
                    .producto(producto)
                    .listaPrecio(precio.listaPrecio())
                    .costoFinalReferencia(precio.costoFinalReferencia())
                    .margenPorcentaje(precio.margenPorcentaje())
                    .precioVenta(precio.precioVenta())
                    .activo(true)
                    .fechaCalculo(fechaCalculo)
                    .build());
        }
    }

    private List<ProductoCompletoPrecioDTO> construirPreciosDto(Long producto) {
        return productoPrecioRepository.findAllByProductoAndActivoTrue(producto).stream()
                .map(precio -> new ProductoCompletoPrecioDTO(precio.getListaPrecio(), precio.getCostoFinalReferencia(), precio.getMargenPorcentaje(), precio.getPrecioVenta()))
                .toList();
    }

    private ProductoCompletoCostoDTO construirCostoDto(ProductoCosto costo, List<ProductoCostoDetalle> detalles, Map<Long, String> nombresComponentes) {
        List<ProductoCompletoCostoComponenteDTO> componentes = detalles.stream()
                .map(detalle -> new ProductoCompletoCostoComponenteDTO(detalle.getComponente(), nombresComponentes.get(detalle.getComponente()), detalle.getEtapaAplicacion(), detalle.getResultadoEtapa(), detalle.getTipoValorAplicado(), detalle.getValorAplicado(), detalle.getBaseCalculo(), detalle.getImporteCalculado(), detalle.getSubtotalResultante()))
                .toList();
        return new ProductoCompletoCostoDTO(costo.getCostoBase(), costo.getNeto1(), costo.getNeto2(), costo.getCostoFinal(), costo.getMoneda(), componentes);
    }

    private Map<Long, String> obtenerNombresComponentes(List<ProductoCostoDetalle> detalles) {
        Map<Long, String> nombres = new HashMap<>();
        for (ProductoCostoDetalle detalle : detalles) {
            if (!nombres.containsKey(detalle.getComponente())) {
                String nombre = componenteCostoRepository.findByCodigo(detalle.getComponente()).map(ComponenteCosto::getNombre).orElse("Componente " + detalle.getComponente());
                nombres.put(detalle.getComponente(), nombre);
            }
        }
        return nombres;
    }

    private List<String> normalizarBarras(List<String> barras) {
        if (barras == null) {
            return Collections.emptyList();
        }
        return barras.stream().filter(Objects::nonNull).map(String::trim).filter(valor -> !valor.isEmpty()).toList();
    }

    private String normalizarTexto(String valor) {
        if (valor == null) {
            return null;
        }
        String normalizado = valor.trim();
        return normalizado.isEmpty() ? null : normalizado;
    }

    private String normalizarMoneda(String moneda) {
        String normalizada = normalizarTexto(moneda);
        return normalizada == null ? "ARS" : normalizada.toUpperCase();
    }

    private void crearStocksIniciales(Long codigoProducto, Double reservaInicial) {
        Double reserva = reservaInicial == null ? 0.00 : reservaInicial;
        sucursalRepository.findAllByActivoTrue().forEach(sucursal -> stockService.crearInicial(codigoProducto, sucursal.getCodigo(), reserva));
    }

    private void actualizarReservaSiCorresponde(Long codigoProducto, ProductoCompletoModificarDTO dto) {
        validarReservaSucursal(dto.sucursal(), dto.reserva());
        if (dto.sucursal() != null) {
            stockService.actualizarReserva(codigoProducto, dto.sucursal(), dto.reserva());
        }
    }

    private void validarReservaSucursal(Long sucursal, Double reserva) {
        if (sucursal == null && reserva == null) {
            return;
        }
        if (sucursal == null || reserva == null) {
            throw new OperacionNoValidaExeption("Para editar reserva debe informar sucursal y reserva.");
        }
    }

    private void validarReservaInicial(Double reservaInicial) {
        if (reservaInicial != null && reservaInicial < 0) {
            throw new OperacionNoValidaExeption("La reserva inicial no puede ser negativa.");
        }
    }

    private String normalizarTipoValor(String tipoValor) {
        String normalizado = normalizarTexto(tipoValor);
        if (normalizado == null) {
            throw new OperacionNoValidaExeption("El tipo de valor del componente es obligatorio.");
        }
        String resultado = normalizado.toUpperCase();
        if (!TIPO_FIJO.equals(resultado) && !TIPO_PORCENTAJE.equals(resultado)) {
            throw new OperacionNoValidaExeption("Tipo de valor no soportado: " + tipoValor);
        }
        return resultado;
    }

    private String normalizarEtapa(String etapaAplicacion) {
        String normalizada = normalizarTexto(etapaAplicacion);
        if (normalizada == null) {
            throw new OperacionNoValidaExeption("La etapa de aplicacion del componente es obligatoria.");
        }
        String resultado = normalizada.toUpperCase();
        if (!ETAPA_1.equals(resultado) && !ETAPA_2.equals(resultado) && !ETAPA_FINAL.equals(resultado)) {
            throw new OperacionNoValidaExeption("Etapa de aplicacion no soportada: " + etapaAplicacion);
        }
        return resultado;
    }

    private BigDecimal porcentajeDe(BigDecimal base, BigDecimal porcentaje) {
        return escala4(base.multiply(escala4(porcentaje)).divide(CIEN, 8, RoundingMode.HALF_UP));
    }

    private BigDecimal escala4(BigDecimal valor) {
        return valor.setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal escala2(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }

    private void compararCosto(String campo, BigDecimal recibido, BigDecimal esperado) {
        comparar(campo, recibido, esperado, TOLERANCIA_COSTO);
    }

    private void compararPrecio(String campo, BigDecimal recibido, BigDecimal esperado) {
        comparar(campo, recibido, esperado, TOLERANCIA_PRECIO);
    }

    private void comparar(String campo, BigDecimal recibido, BigDecimal esperado, BigDecimal tolerancia) {
        if (recibido == null) {
            throw new OperacionNoValidaExeption("El campo " + campo + " es obligatorio.");
        }
        BigDecimal normalizadoRecibido = recibido.setScale(esperado.scale(), RoundingMode.HALF_UP);
        if (normalizadoRecibido.subtract(esperado).abs().compareTo(tolerancia) > 0) {
            throw new CalculoInconsistenteException("El campo " + campo + " es inconsistente. Esperado " + esperado + " y se recibio " + normalizadoRecibido + ".");
        }
    }

    private record ComponenteEntrada(ComponenteCosto maestro, ProductoCompletoCostoComponenteNuevoDTO dto) {}
    private record ComponenteValidado(Long componente, String nombreComponente, String etapaAplicacion, String resultadoEtapa, String tipoValorAplicado, BigDecimal valorAplicado, BigDecimal baseCalculo, BigDecimal importeCalculado, BigDecimal subtotalResultante) {}
    private record EtapaCalculada(BigDecimal resultadoEtapa, List<ComponenteValidado> componentes) {}
    private record CostoValidado(BigDecimal costoBase, BigDecimal neto1, BigDecimal neto2, BigDecimal costoFinal, String moneda, List<ComponenteValidado> componentes) {}
    private record PrecioValidado(Long listaPrecio, BigDecimal costoFinalReferencia, BigDecimal margenPorcentaje, BigDecimal precioVenta) {}
    private record CalculoValidado(CostoValidado costo, List<PrecioValidado> precios, LocalDateTime fechaCalculo) {
        private List<ComponenteValidado> componentes() { return costo.componentes(); }
    }
}
