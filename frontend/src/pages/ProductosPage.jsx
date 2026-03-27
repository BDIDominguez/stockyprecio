import { useState, useEffect, useCallback, useMemo } from "react";
import Layout from "../components/Layout";
import Paginacion from "../components/Paginacion";
import AlertaError from "../components/AlertaError";
import { useSucursal } from "../context/SucursalContext";
import { apiGet, apiPost, apiPut, apiDelete, apiPatch } from "../api/apiCliente";
import "./MaestroPage.css";
import "./ProductosPage.css";

// Modal de confirmacion de precio bajo costo
function ConfirmarBajoCostoModal({ preciosDebajoCosto, onConfirmar, onCancelar }) {
  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h3>Precio por debajo del costo</h3>
        <p>Los siguientes precios de venta son menores al costo final:</p>
        <ul>
          {preciosDebajoCosto.map((p, i) => (
            <li key={i}>
              Lista {p.listaPrecio}: Precio Venta ${p.precioVenta?.toFixed(2)} (menor que) Costo ${p.costoFinalReferencia?.toFixed(2)}
            </li>
          ))}
        </ul>
        <p className="warning-text">Esta seguro de guardar asi?</p>
        <div className="modal-actions">
          <button className="btn-danger" onClick={onConfirmar}>Si, guardar</button>
          <button className="btn-secondary" onClick={onCancelar}>Cancelar</button>
        </div>
      </div>
    </div>
  );
}

// Helpers de redondeo
function round4(num) {
  return Math.round(num * 10000) / 10000;
}

function round2(num) {
  return Math.round(num * 100) / 100;
}

// Handler para seleccionar todo el texto al hacer focus
function handleSelectAll(e) {
  e.target.select();
}

export default function ProductosPage() {
  const { sucursalActiva } = useSucursal();
  const [activeTab, setActiveTab] = useState("activos");
  const [productos, setProductos] = useState([]);
  const [pageData, setPageData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [editando, setEditando] = useState(null);
  const [mostrarForm, setMostrarForm] = useState(false);
  const [busqueda, setBusqueda] = useState("");
  const [codigoBusqueda, setCodigoBusqueda] = useState("");
  const [cargandoCodigo, setCargandoCodigo] = useState(false);
  const [mostrarModalBajoCosto, setMostrarModalBajoCosto] = useState(false);
  const [preciosDebajoCosto, setPreciosDebajoCosto] = useState([]);
  const [requestPendiente, setRequestPendiente] = useState(null);
  
  const [categorias, setCategorias] = useState([]);
  const [proveedores, setProveedores] = useState([]);
  const [listasPrecios, setListasPrecios] = useState([]);
  const [componentesCosto, setComponentesCosto] = useState([]);
  
  // Estado del formulario
  const [formData, setFormData] = useState({
    codigo: "",
    nombre: "",
    descripcion: "",
    categoria: "",
    proveedor: "",
    manejaStock: true,
    costoBase: "",
    moneda: "ARS",
    // Componentes aplicados: { [codigoComponente]: valorAplicado }
    componentesAplicados: {},
    // Márgenes por lista de precio: { [codigoListaPrecio]: margenPorcentaje }
    margenesListas: {},
    // Precios de venta directos: { [codigoListaPrecio]: precioVenta }
    preciosDirectos: {},
    reservaInicial: "",
    codigosBarra: [""],
  });

  // Calcular costo compuesto - SE RECALCULA EN TIEMPO REAL
  const costoCalculado = useMemo(() => {
    const costoBase = parseFloat(formData.costoBase) || 0;
    
    if (costoBase <= 0) {
      return { componentesCalculados: [], costoFinal: 0, costoBase };
    }
    
    let subtotalAnterior = costoBase;
    const componentesCalculados = [];
    
    // Obtener componentes aplicados ordenados por prioridad
    const componentesOrdenados = componentesCosto
      .filter(c => formData.componentesAplicados[c.codigo] !== undefined || c.obligatorio)
      .sort((a, b) => a.prioridadAplicacion - b.prioridadAplicacion);
    
    componentesOrdenados.forEach(compMaestro => {
      // Usar el valor que el usuario ingresó, o el valor por defecto
      const valorAplicado = formData.componentesAplicados[compMaestro.codigo] ?? compMaestro.valorDefecto;
      
      // Calcular base según modoBase
      let baseCalculo = subtotalAnterior;
      if (compMaestro.modoBase === "COSTO_BASE") {
        baseCalculo = costoBase;
      }
      
      // Calcular importe
      let importeCalculado;
      if (compMaestro.tipoValor === "PORCENTAJE") {
        importeCalculado = round4(baseCalculo * valorAplicado / 100);
      } else {
        importeCalculado = valorAplicado;
      }
      
      const subtotalResultante = round4(subtotalAnterior + importeCalculado);
      
      componentesCalculados.push({
        componente: compMaestro.codigo,
        nombreComponente: compMaestro.nombre,
        tipoValorAplicado: compMaestro.tipoValor,
        modoBaseAplicado: compMaestro.modoBase,
        ordenAplicado: compMaestro.prioridadAplicacion,
        valorAplicado: round4(valorAplicado),
        baseCalculo: round4(baseCalculo),
        importeCalculado: importeCalculado,
        subtotalResultante: subtotalResultante,
      });
      
      subtotalAnterior = subtotalResultante;
    });
    
    const costoFinal = componentesCalculados.length > 0 
      ? componentesCalculados[componentesCalculados.length - 1].subtotalResultante 
      : costoBase;
    
    return { componentesCalculados, costoFinal, costoBase };
  }, [formData.costoBase, formData.componentesAplicados, componentesCosto]);

  // Calcular precios de venta - SE RECALCULA EN TIEMPO REAL
  // También considera los precios de venta directos que el usuario pueda ingresar
  const preciosCalculados = useMemo(() => {
    const { costoFinal } = costoCalculado;
    
    return listasPrecios.map(lista => {
      const margen = formData.margenesListas[lista.codigo] ?? 0;
      // Si el usuario escribió un precio de venta directo, usarlo
      const precioVentaIngresado = formData.preciosDirectos?.[lista.codigo];
      
      let margenCalculado = margen;
      let precioVenta = 0;
      
      if (precioVentaIngresado !== undefined && precioVentaIngresado !== null && precioVentaIngresado !== "") {
        // El usuario ingreso un precio de venta directo, calcular el margen
        precioVenta = parseFloat(precioVentaIngresado) || 0;
        if (costoFinal > 0) {
          // Redondear a 2 decimales el margen calculado
          margenCalculado = round2(((precioVenta / costoFinal) - 1) * 100);
        }
      } else {
        // Calcular precio de venta desde el margen
        precioVenta = costoFinal > 0 ? round2(costoFinal * (1 + margen / 100)) : 0;
        // Redondear el margen a 2 decimales
        margenCalculado = round2(margen);
      }
      
      const debajoCosto = costoFinal > 0 && precioVenta < costoFinal;
      
      return {
        listaPrecio: lista.codigo,
        nombreLista: lista.nombre,
        margenPorcentaje: margenCalculado,
        precioVenta,
        costoFinalReferencia: costoFinal,
        debajoCosto,
        precioVentaIngresado: precioVentaIngresado || "",
      };
    });
  }, [costoCalculado.costoFinal, formData.margenesListas, formData.preciosDirectos, listasPrecios]);

  // Cargar catálogos
  useEffect(() => {
    async function cargarCatalogos() {
      try {
        const [cats, provs, listas, comps] = await Promise.all([
          apiGet("/categorias", { activo: true, size: 100 }),
          apiGet("/proveedores", { activo: true, size: 100 }),
          apiGet("/listas-precios", { activo: true, size: 100 }),
          apiGet("/componentes-costo", { activo: true, size: 100 }),
        ]);
        setCategorias(cats.content || []);
        setProveedores(provs.content || []);
        setListasPrecios(listas.content || []);
        setComponentesCosto(comps.content || []);
      } catch (err) {
        console.error("Error cargando catalogos:", err);
      }
    }
    cargarCatalogos();
  }, []);

  const cargarDatos = useCallback(async (page = 0) => {
    setLoading(true);
    setError(null);
    try {
      const params = {
        page,
        size: 10,
        sort: "nombre",
        activo: activeTab === "activos",
      };
      if (busqueda.length >= 2) params.nombre = busqueda;
      if (codigoBusqueda) params.codigo = codigoBusqueda;
      if (sucursalActiva) params.sucursal = sucursalActiva.codigo;

      const data = await apiGet("/productos/completo", params);
      setProductos(data.content || []);
      setPageData(data);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  }, [activeTab, busqueda, codigoBusqueda, sucursalActiva]);

  useEffect(() => {
    cargarDatos();
  }, [cargarDatos]);

  useEffect(() => {
    if (mostrarForm && !editando && formData.codigo === "") {
      cargarSiguienteCodigo();
    }
  }, [mostrarForm, editando]);

  async function cargarSiguienteCodigo() {
    setCargandoCodigo(true);
    try {
      const codigo = await apiGet("/productos/siguiente-codigo");
      if (codigo !== null && codigo !== undefined) {
        setFormData(prev => ({ ...prev, codigo: String(codigo) }));
      }
    } catch (err) {
      console.error("Error cargando siguiente codigo:", err);
    } finally {
      setCargandoCodigo(false);
    }
  }

  // Cambiar valor de un componente de costo
  function cambiarValorComponente(codigoComponente, valor) {
    setFormData(prev => ({
      ...prev,
      componentesAplicados: {
        ...prev.componentesAplicados,
        [codigoComponente]: valor,
      },
    }));
  }

  // Cambiar margen de una lista de precio
  function cambiarMargenLista(codigoLista, valor) {
    setFormData(prev => ({
      ...prev,
      margenesListas: {
        ...prev.margenesListas,
        [codigoLista]: valor,
      },
    }));
  }

  // Cambiar precio de venta directo de una lista
  function cambiarPrecioDirecto(codigoLista, valor) {
    setFormData(prev => ({
      ...prev,
      preciosDirectos: {
        ...prev.preciosDirectos,
        [codigoLista]: valor,
      },
    }));
  }

  function abrirNuevo() {
    // Inicializar componentes obligatorios con valores por defecto
    const componentesIniciales = {};
    componentesCosto.forEach(c => {
      if (c.obligatorio) {
        componentesIniciales[c.codigo] = c.valorDefecto;
      }
    });
    
    // Inicializar márgenes de listas con 0
    const margenesIniciales = {};
    listasPrecios.forEach(l => {
      margenesIniciales[l.codigo] = 0;
    });
    
    setEditando(null);
    setFormData({
      codigo: "",
      nombre: "",
      descripcion: "",
      categoria: "",
      proveedor: "",
      manejaStock: true,
      costoBase: "",
      moneda: "ARS",
      componentesAplicados: componentesIniciales,
      margenesListas: margenesIniciales,
      reservaInicial: "",
      codigosBarra: [""],
    });
    setMostrarForm(true);
  }

  async function abrirEditar(productoCompleto) {
    try {
      const params = {};
      if (sucursalActiva) params.sucursal = sucursalActiva.codigo;
      const detalle = await apiGet(`/productos/completo/codigo/${productoCompleto.producto.codigo}`, params);

      // Reconstruir componentes aplicados desde la respuesta del backend
      const componentesAplicados = {};
      detalle.costo?.componentes?.forEach(comp => {
        componentesAplicados[comp.componente] = comp.valorAplicado;
      });

      // Reconstruir márgenes desde los precios del backend
      const margenesListas = {};
      detalle.precios?.forEach(p => {
        margenesListas[p.listaPrecio] = p.margenPorcentaje;
      });

      setFormData({
        codigo: String(detalle.producto.codigo),
        nombre: detalle.producto.nombre,
        descripcion: detalle.producto.descripcion || "",
        categoria: detalle.producto.categoria || "",
        proveedor: detalle.producto.proveedor || "",
        manejaStock: detalle.producto.manejaStock,
        costoBase: detalle.costo?.costoBase || "",
        moneda: detalle.costo?.moneda || "ARS",
        componentesAplicados,
        margenesListas,
        reservaInicial: "",
        codigosBarra: detalle.codigosBarra?.length > 0 ? detalle.codigosBarra : [""],
      });
      setEditando(detalle);
      setMostrarForm(true);
    } catch (err) {
      setError(err);
    }
  }

  function cerrarForm() {
    setMostrarForm(false);
    setEditando(null);
    setMostrarModalBajoCosto(false);
    setPreciosDebajoCosto([]);
    setRequestPendiente(null);
  }

  function agregarCodigoBarra() {
    setFormData(prev => ({
      ...prev,
      codigosBarra: [...prev.codigosBarra, ""],
    }));
  }

  function cambiarCodigoBarra(index, valor) {
    const nuevos = [...formData.codigosBarra];
    nuevos[index] = valor;
    setFormData(prev => ({ ...prev, codigosBarra: nuevos }));
  }

  function eliminarCodigoBarra(index) {
    const nuevos = formData.codigosBarra.filter((_, i) => i !== index);
    setFormData(prev => ({ ...prev, codigosBarra: nuevos }));
  }

  async function guardar(e) {
    e.preventDefault();
    setError(null);

    const codigoNum = parseInt(formData.codigo);
    if (!codigoNum || codigoNum <= 0) {
      setError({ mensaje: "El codigo debe ser mayor a 0", status: 400 });
      return;
    }

    if (!formData.costoBase || parseFloat(formData.costoBase) <= 0) {
      setError({ mensaje: "El costo base debe ser mayor a 0", status: 400 });
      return;
    }

    const { componentesCalculados, costoFinal } = costoCalculado;

    // Construir precios con los márgenes actuales
    const preciosPayload = listasPrecios.map(lista => {
      const margen = formData.margenesListas[lista.codigo] ?? 0;
      return {
        listaPrecio: lista.codigo,
        margenPorcentaje: round4(margen),
        precioVenta: round2(costoFinal * (1 + margen / 100)),
      };
    });

    const payload = {
      producto: editando ? {
        nombre: formData.nombre,
        descripcion: formData.descripcion,
        categoria: formData.categoria ? parseInt(formData.categoria) : null,
        proveedor: formData.proveedor ? parseInt(formData.proveedor) : null,
        manejaStock: formData.manejaStock,
        activo: editando.producto.activo,
      } : {
        codigo: codigoNum,
        nombre: formData.nombre,
        descripcion: formData.descripcion,
        categoria: formData.categoria ? parseInt(formData.categoria) : null,
        proveedor: formData.proveedor ? parseInt(formData.proveedor) : null,
        manejaStock: formData.manejaStock,
        activo: true,
      },
      costo: {
        costoBase: round4(parseFloat(formData.costoBase)),
        costoFinal: costoFinal,
        moneda: formData.moneda,
        componentes: componentesCalculados,
      },
      precios: preciosPayload,
      codigosBarra: formData.codigosBarra.filter(c => c.trim() !== ""),
      confirmarBajoCosto: false,
    };

    if (editando) {
      payload.sucursal = sucursalActiva?.codigo;
      if (formData.reservaInicial) {
        payload.reserva = parseFloat(formData.reservaInicial);
      }
    } else {
      if (formData.reservaInicial) {
        payload.reservaInicial = parseFloat(formData.reservaInicial);
      }
    }

    try {
      if (editando) {
        await apiPut(`/productos/completo/codigo/${editando.producto.codigo}`, payload);
      } else {
        await apiPost("/productos/completo", payload);
      }
      cerrarForm();
      cargarDatos();
    } catch (err) {
      if (err.codigo === "PREC-409-002") {
        const preciosDebajo = preciosPayload.filter(p => p.precioVenta < costoFinal);
        setPreciosDebajoCosto(preciosDebajo.map(p => ({
          ...p,
          costoFinalReferencia: costoFinal,
        })));
        setRequestPendiente(payload);
        setMostrarModalBajoCosto(true);
      } else {
        setError(err);
      }
    }
  }

  async function confirmarBajoCosto() {
    if (!requestPendiente) return;
    
    try {
      await apiPut(`/productos/completo/codigo/${editando.producto.codigo}`, {
        ...requestPendiente,
        confirmarBajoCosto: true,
      });
      cerrarForm();
      cargarDatos();
    } catch (err) {
      setError(err);
      setMostrarModalBajoCosto(false);
    }
  }

  async function desactivar(productoCompleto) {
    if (!confirm(`Desactivar el producto "${productoCompleto.producto.nombre}"?`)) return;
    setError(null);
    try {
      await apiDelete(`/productos/${productoCompleto.producto.codigo}`);
      cargarDatos();
    } catch (err) {
      setError(err);
    }
  }

  async function activar(productoCompleto) {
    setError(null);
    try {
      await apiPatch(`/productos/${productoCompleto.producto.codigo}/activar`);
      cargarDatos();
    } catch (err) {
      setError(err);
    }
  }

  const formatoMoneda = (valor, moneda = "ARS") => {
    return new Intl.NumberFormat("es-AR", {
      style: "currency",
      currency: moneda,
      minimumFractionDigits: 2,
    }).format(valor || 0);
  };

  return (
    <Layout showSucursalSelector>
      <div className="maestro-page producto-page">
        <div className="maestro-header">
          <h1>Productos</h1>
        </div>

        <AlertaError error={error} onClose={() => setError(null)} />

        {mostrarModalBajoCosto && (
          <ConfirmarBajoCostoModal
            preciosDebajoCosto={preciosDebajoCosto}
            onConfirmar={confirmarBajoCosto}
            onCancelar={() => setMostrarModalBajoCosto(false)}
          />
        )}

        {mostrarForm && (
          <div className="producto-form-panel">
            <h3>{editando ? "Editar Producto" : "Nuevo Producto"}</h3>
            <form onSubmit={guardar} className="producto-form">
              {/* Datos del Producto */}
              <div className="form-section">
                <h4>Datos del Producto</h4>
                <div className="form-row">
                  <label>Codigo:</label>
                  <div className="input-with-spinner">
                    {cargandoCodigo && <span className="spinner"></span>}
                    <input
                      type="text"
                      inputMode="numeric"
                      pattern="[0-9]*"
                      value={formData.codigo}
                      onChange={e => setFormData(prev => ({ ...prev, codigo: e.target.value }))}
                      required
                      disabled={!!editando || cargandoCodigo}
                      placeholder={cargandoCodigo ? "Cargando..." : ""}
                    />
                  </div>
                </div>
                <div className="form-row">
                  <label>Nombre:</label>
                  <input
                    type="text"
                    value={formData.nombre}
                    onChange={e => setFormData(prev => ({ ...prev, nombre: e.target.value }))}
                    required
                  />
                </div>
                <div className="form-row">
                  <label>Descripcion:</label>
                  <input
                    type="text"
                    value={formData.descripcion}
                    onChange={e => setFormData(prev => ({ ...prev, descripcion: e.target.value }))}
                  />
                </div>
                <div className="form-row">
                  <label>Categoria:</label>
                  <select
                    value={formData.categoria}
                    onChange={e => setFormData(prev => ({ ...prev, categoria: e.target.value }))}
                  >
                    <option value="">Sin categoria</option>
                    {categorias.map(c => (
                      <option key={c.codigo} value={c.codigo}>{c.nombre}</option>
                    ))}
                  </select>
                </div>
                <div className="form-row">
                  <label>Proveedor:</label>
                  <select
                    value={formData.proveedor}
                    onChange={e => setFormData(prev => ({ ...prev, proveedor: e.target.value }))}
                  >
                    <option value="">Sin proveedor</option>
                    {proveedores.map(p => (
                      <option key={p.codigo} value={p.codigo}>{p.nombre}</option>
                    ))}
                  </select>
                </div>
                <div className="form-row checkbox-row">
                  <label>
                    <input
                      type="checkbox"
                      checked={formData.manejaStock}
                      onChange={e => setFormData(prev => ({ ...prev, manejaStock: e.target.checked }))}
                    />
                    Maneja Stock
                  </label>
                </div>
              </div>

              {/* Costo Compuesto */}
              <div className="form-section">
                <h4>Costo Compuesto</h4>
                
                {/* Costo Base y Moneda en la MISMA línea */}
                <div className="form-row costo-base-row">
                  <label>Costo Base:</label>
                  <input
                    type="text"
                    inputMode="decimal"
                    value={formData.costoBase}
                    onChange={e => setFormData(prev => ({ ...prev, costoBase: e.target.value }))}
                    onFocus={handleSelectAll}
                    placeholder="0.0000"
                    required
                    className="costo-base-input"
                  />
                  <label className="moneda-label">Moneda:</label>
                  <select
                    value={formData.moneda}
                    onChange={e => setFormData(prev => ({ ...prev, moneda: e.target.value }))}
                    className="moneda-select"
                  >
                    <option value="ARS">ARS</option>
                    <option value="USD">USD</option>
                    <option value="EUR">EUR</option>
                  </select>
                </div>

                {/* Tabla de componentes de costo */}
                <div className="componentes-grid">
                  <div className="grid-header">
                    <span>#</span>
                    <span>Componente</span>
                    <span>Tipo</span>
                    <span>Base</span>
                    <span>Valor</span>
                    <span>Importe</span>
                    <span>Subtotal</span>
                  </div>
                  
                  {/* Fila del costo base */}
                  <div className="grid-row costo-base-row-display">
                    <span>-</span>
                    <span>Costo Base</span>
                    <span>-</span>
                    <span>-</span>
                    <span>-</span>
                    <span>-</span>
                    <span className="subtotal-base">{formatoMoneda(costoCalculado.costoBase, formData.moneda)}</span>
                  </div>
                  
                  {/* Filas de componentes aplicados */}
                  {costoCalculado.componentesCalculados.map((comp) => {
                    const compMaestro = componentesCosto.find(c => c.codigo === comp.componente);
                    const esEditable = compMaestro?.editableEnProducto ?? false;
                    
                    return (
                      <div key={comp.componente} className="grid-row">
                        <span>{comp.ordenAplicado}</span>
                        <span className="comp-nombre">{comp.nombreComponente}</span>
                        <span>{comp.tipoValorAplicado}</span>
                        <span className="comp-base">{formatoMoneda(comp.baseCalculo, formData.moneda)}</span>
                        <span className="comp-valor">
                          {esEditable ? (
                            <input
                              type="text"
                              inputMode="decimal"
                              value={comp.valorAplicado}
                              onChange={e => cambiarValorComponente(comp.componente, parseFloat(e.target.value) || 0)}
                              onFocus={handleSelectAll}
                              className="valor-input"
                            />
                          ) : (
                            <span>{comp.tipoValorAplicado === "PORCENTAJE" ? `${comp.valorAplicado}%` : formatoMoneda(comp.valorAplicado, formData.moneda)}</span>
                          )}
                        </span>
                        <span className="comp-importe">{formatoMoneda(comp.importeCalculado, formData.moneda)}</span>
                        <span className="comp-subtotal">{formatoMoneda(comp.subtotalResultante, formData.moneda)}</span>
                      </div>
                    );
                  })}
                </div>
                
                {/* Costo Final */}
                <div className="costo-final-box">
                  <span className="costo-final-label">Costo Final:</span>
                  <span className="costo-final-valor">
                    {formatoMoneda(costoCalculado.costoFinal, formData.moneda)}
                  </span>
                </div>
              </div>

              {/* Precios por Lista */}
              <div className="form-section">
                <h4>Precios de Venta por Lista</h4>
                
                <div className="precios-grid">
                  <div className="grid-header">
                    <span>Lista</span>
                    <span>Margen %</span>
                    <span>Costo Ref.</span>
                    <span>Precio Venta</span>
                    <span>Estado</span>
                  </div>
                  
                  {preciosCalculados.map(precio => (
                    <div key={precio.listaPrecio} className={`grid-row ${precio.debajoCosto ? 'precio-bajo-costo' : ''}`}>
                      <span className="precio-lista-nombre">{precio.nombreLista}</span>
                      <span className="precio-margen">
                        <input
                          type="text"
                          inputMode="decimal"
                          value={precio.margenPorcentaje}
                          onChange={e => cambiarMargenLista(precio.listaPrecio, parseFloat(e.target.value) || 0)}
                          onFocus={handleSelectAll}
                          className="margen-input"
                        />
                        <span className="margen-suffix">%</span>
                      </span>
                      <span className="precio-costo">{formatoMoneda(precio.costoFinalReferencia, formData.moneda)}</span>
                      <span className="precio-venta-input">
                        <input
                          type="text"
                          inputMode="decimal"
                          value={precio.precioVentaIngresado !== "" ? precio.precioVentaIngresado : precio.precioVenta.toFixed(2)}
                          onChange={e => cambiarPrecioDirecto(precio.listaPrecio, e.target.value)}
                          onFocus={handleSelectAll}
                          className={`precio-input ${precio.debajoCosto ? 'debajo-costo' : ''}`}
                        />
                      </span>
                      <span className="precio-estado">
                        {precio.debajoCosto && <span className="alerta-bajo-costo">BAJO COSTO</span>}
                      </span>
                    </div>
                  ))}
                </div>
              </div>

              {/* Codigos de Barra */}
              <div className="form-section">
                <h4>Codigos de Barra</h4>
                {formData.codigosBarra.map((cb, i) => (
                  <div key={i} className="codigo-barra-row">
                    <input
                      type="text"
                      value={cb}
                      onChange={e => cambiarCodigoBarra(i, e.target.value)}
                      placeholder="Codigo de barras"
                    />
                    {formData.codigosBarra.length > 1 && (
                      <button type="button" className="btn-remove" onClick={() => eliminarCodigoBarra(i)}>x</button>
                    )}
                  </div>
                ))}
                <button type="button" className="btn-add" onClick={agregarCodigoBarra}>
                  + Agregar Codigo
                </button>
              </div>

              {/* Stock */}
              <div className="form-section">
                <h4>Stock</h4>
                <div className="form-row">
                  <label>{editando ? "Modificar Reserva:" : "Reserva Inicial:"}</label>
                  <input
                    type="text"
                    inputMode="decimal"
                    value={formData.reservaInicial}
                    onChange={e => setFormData(prev => ({ ...prev, reservaInicial: e.target.value }))}
                    onFocus={handleSelectAll}
                    placeholder={editando ? "Dejar vacio para no modificar" : "Opcional"}
                  />
                </div>
              </div>

              <div className="form-actions">
                <button type="submit" className="btn-primary">Guardar</button>
                <button type="button" onClick={cerrarForm} className="btn-secondary">Cancelar</button>
              </div>
            </form>
          </div>
        )}

        <div className="maestro-tabs">
          <button 
            className={activeTab === "activos" ? "active" : ""} 
            onClick={() => { setActiveTab("activos"); setBusqueda(""); setCodigoBusqueda(""); }}
          >
            Activos
          </button>
          <button 
            className={activeTab === "eliminados" ? "active" : ""} 
            onClick={() => { setActiveTab("eliminados"); setBusqueda(""); setCodigoBusqueda(""); }}
          >
            Eliminados
          </button>
        </div>

        <div className="maestro-toolbar producto-toolbar">
          {activeTab === "activos" && !mostrarForm && (
            <button onClick={abrirNuevo} className="btn-primary">+ Nuevo Producto</button>
          )}
          <div className="busqueda-productos">
            <input
              type="text"
              placeholder="Buscar por nombre (min. 2 caracteres)"
              value={busqueda}
              onChange={e => setBusqueda(e.target.value)}
            />
            <input
              type="number"
              placeholder="Buscar por codigo"
              value={codigoBusqueda}
              onChange={e => setCodigoBusqueda(e.target.value)}
            />
          </div>
        </div>

        {loading ? (
          <div className="maestro-loading">Cargando...</div>
        ) : (
          <>
            <table className="maestro-table producto-table">
              <thead>
                <tr>
                  <th>Cod.</th>
                  <th>Nombre</th>
                  <th>Costo Final</th>
                  <th>Precio Venta</th>
                  <th>Stock</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {productos.length === 0 ? (
                  <tr>
                    <td colSpan="6" className="maestro-empty">
                      No hay productos {activeTab === "activos" ? "activos" : "eliminados"}
                    </td>
                  </tr>
                ) : (
                  productos.map(p => {
                    const precioPrincipal = p.precios?.[0];
                    const stock = p.stock;
                    const debajoCosto = precioPrincipal?.debajoCosto;
                    
                    return (
                      <tr key={p.producto.codigo}>
                        <td>{p.producto.codigo}</td>
                        <td>{p.producto.nombre}</td>
                        <td>{formatoMoneda(p.costoFinal, p.moneda)}</td>
                        <td className={debajoCosto ? "precio-bajo-costo" : ""}>
                          {precioPrincipal ? formatoMoneda(precioPrincipal.precioVenta, p.moneda) : "-"}
                          {debajoCosto && <span className="alerta-bajo-costo"> !</span>}
                        </td>
                        <td>
                          {stock ? (
                            <span className="stock-info">
                              Stock: {stock.cantidad?.toFixed(2) || 0} | Reserva: {stock.reserva?.toFixed(2) || 0}
                            </span>
                          ) : (
                            <span className="stock-info">Sin stock</span>
                          )}
                        </td>
                        <td>
                          {activeTab === "activos" ? (
                            <>
                              <button onClick={() => abrirEditar(p)} className="btn-action btn-edit">Editar</button>
                              <button onClick={() => desactivar(p)} className="btn-action btn-delete">Desactivar</button>
                            </>
                          ) : (
                            <button onClick={() => activar(p)} className="btn-action btn-activate">Activar</button>
                          )}
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
            <Paginacion pageData={pageData} onPageChange={pag => cargarDatos(pag)} />
          </>
        )}
      </div>
    </Layout>
  );
}
