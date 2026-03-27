import { useState, useEffect, useCallback } from "react";
import Layout from "../components/Layout";
import Paginacion from "../components/Paginacion";
import AlertaError from "../components/AlertaError";
import { apiGet, apiPost, apiPut, apiDelete, apiPatch } from "../api/apiCliente";
import "./MaestroPage.css";

export default function ComponentesCostoPage() {
  const [activeTab, setActiveTab] = useState("activos");
  const [componentes, setComponentes] = useState([]);
  const [pageData, setPageData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [editando, setEditando] = useState(null);
  const [mostrarForm, setMostrarForm] = useState(false);
  const [busqueda, setBusqueda] = useState("");
  const [cargandoCodigo, setCargandoCodigo] = useState(false);
  
  const [formData, setFormData] = useState({
    codigo: "",
    nombre: "",
    descripcion: "",
    tipoComponente: "GASTO",
    tipoValor: "FIJO",
    valorDefecto: "",
    modoBase: "COSTO_BASE",
    prioridadAplicacion: 1,
    editableEnProducto: true,
    obligatorio: false,
  });

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
      const data = await apiGet("/componentes-costo", params);
      setComponentes(data.content || []);
      setPageData(data);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  }, [activeTab, busqueda]);

  useEffect(() => {
    cargarDatos();
  }, [cargarDatos]);

  async function cargarSiguienteCodigo() {
    setCargandoCodigo(true);
    try {
      const codigo = await apiGet("/componentes-costo/siguiente-codigo");
      console.log("Siguiente código recibido:", codigo);
      if (codigo !== null && codigo !== undefined) {
        setFormData(prev => ({ ...prev, codigo: String(codigo) }));
      }
    } catch (err) {
      console.error("Error cargando siguiente código:", err);
    } finally {
      setCargandoCodigo(false);
    }
  }

  function abrirNuevo() {
    setEditando(null);
    setFormData({
      codigo: "",
      nombre: "",
      descripcion: "",
      tipoComponente: "GASTO",
      tipoValor: "FIJO",
      valorDefecto: "",
      modoBase: "COSTO_BASE",
      prioridadAplicacion: 1,
      editableEnProducto: true,
      obligatorio: false,
    });
    setMostrarForm(true);
  }

  useEffect(() => {
    if (mostrarForm && !editando && formData.codigo === "") {
      cargarSiguienteCodigo();
    }
  }, [mostrarForm, editando]);

  function abrirEditar(comp) {
    setEditando(comp);
    setFormData({
      codigo: comp.codigo,
      nombre: comp.nombre,
      descripcion: comp.descripcion || "",
      tipoComponente: comp.tipoComponente || "GASTO",
      tipoValor: comp.tipoValor || "FIJO",
      valorDefecto: comp.valorDefecto || "",
      modoBase: comp.modoBase || "COSTO_BASE",
      prioridadAplicacion: comp.prioridadAplicacion || 1,
      editableEnProducto: comp.editableEnProducto ?? true,
      obligatorio: comp.obligatorio ?? false,
    });
    setMostrarForm(true);
  }

  function cerrarForm() {
    setMostrarForm(false);
    setEditando(null);
  }

  async function guardar(e) {
    e.preventDefault();
    setError(null);
    
    const codigoNum = parseInt(formData.codigo);
    if (!codigoNum || codigoNum <= 0) {
      setError({ mensaje: "El código debe ser mayor a 0", status: 400 });
      return;
    }
    
    if (!formData.nombre || formData.nombre.trim() === "") {
      setError({ mensaje: "El nombre es obligatorio", status: 400 });
      return;
    }
    
    if (!formData.valorDefecto || parseFloat(formData.valorDefecto) < 0) {
      setError({ mensaje: "El valor por defecto debe ser 0 o mayor", status: 400 });
      return;
    }
    
    try {
      const datos = {
        nombre: formData.nombre.trim(),
        descripcion: formData.descripcion.trim(),
        tipoComponente: formData.tipoComponente,
        tipoValor: formData.tipoValor,
        valorDefecto: parseFloat(formData.valorDefecto) || 0,
        modoBase: formData.modoBase,
        prioridadAplicacion: parseInt(formData.prioridadAplicacion) || 1,
        editableEnProducto: formData.editableEnProducto,
        obligatorio: formData.obligatorio,
      };
      
      if (editando) {
        await apiPut(`/componentes-costo/codigo/${editando.codigo}`, datos);
      } else {
        await apiPost("/componentes-costo", { codigo: codigoNum, ...datos });
      }
      cerrarForm();
      cargarDatos();
    } catch (err) {
      setError(err);
    }
  }

  async function eliminar(comp) {
    if (!confirm(`¿Desactivar el componente de costo "${comp.nombre}"?`)) return;
    setError(null);
    try {
      await apiDelete(`/componentes-costo/codigo/${comp.codigo}`);
      cargarDatos();
    } catch (err) {
      setError(err);
    }
  }

  async function activar(comp) {
    setError(null);
    try {
      await apiPatch(`/componentes-costo/codigo/${comp.codigo}/activar`);
      cargarDatos();
    } catch (err) {
      setError(err);
    }
  }

  const getTipoComponenteLabel = (tipo) => {
    const labels = {
      GASTO: "Gasto",
      IMPUESTO: "Impuesto",
      RECARGO: "Recargo",
      AJUSTE: "Ajuste",
    };
    return labels[tipo] || tipo;
  };

  const getModoBaseLabel = (modo) => {
    const labels = {
      COSTO_BASE: "Costo Base",
      SUBTOTAL_ACUMULADO: "Subtotal Acumulado",
      SUBTOTAL_SIN_IVA: "Subtotal sin IVA",
      SUBTOTAL_ANTES_DE_IMPUESTOS_FINALES: "Antes de Impuestos Finales",
    };
    return labels[modo] || modo;
  };

  return (
    <Layout>
      <div className="maestro-page">
        <div className="maestro-header">
          <h1>Componentes de Costo</h1>
        </div>

        <AlertaError error={error} onClose={() => setError(null)} />

        {mostrarForm && (
          <div className="maestro-form-panel">
            <h3>{editando ? "Editar Componente de Costo" : "Nuevo Componente de Costo"}</h3>
            <form onSubmit={guardar} className="maestro-form">
              <div className="form-row">
                <label>Código:</label>
                <div className="input-with-spinner">
                  {cargandoCodigo && <span className="spinner"></span>}
                  <input
                    type="text"
                    inputMode="numeric"
                    pattern="[0-9]*"
                    value={formData.codigo}
                    onChange={e => setFormData({ ...formData, codigo: e.target.value })}
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
                  onChange={e => setFormData({ ...formData, nombre: e.target.value })}
                  placeholder="Ej: Flete Logistico, IVA 21"
                  required
                />
              </div>
              
              <div className="form-row">
                <label>Descripción:</label>
                <input
                  type="text"
                  value={formData.descripcion}
                  onChange={e => setFormData({ ...formData, descripcion: e.target.value })}
                  placeholder="Descripción del componente"
                />
              </div>
              
              <div className="form-row">
                <label>Tipo de Componente:</label>
                <select
                  value={formData.tipoComponente}
                  onChange={e => setFormData({ ...formData, tipoComponente: e.target.value })}
                >
                  <option value="GASTO">Gasto</option>
                  <option value="IMPUESTO">Impuesto</option>
                  <option value="RECARGO">Recargo</option>
                  <option value="AJUSTE">Ajuste</option>
                </select>
              </div>
              
              <div className="form-row">
                <label>Tipo de Valor:</label>
                <select
                  value={formData.tipoValor}
                  onChange={e => setFormData({ ...formData, tipoValor: e.target.value })}
                >
                  <option value="FIJO">Fijo ($)</option>
                  <option value="PORCENTAJE">Porcentaje (%)</option>
                </select>
              </div>
              
              <div className="form-row">
                <label>Valor por Defecto:</label>
                <input
                  type="number"
                  step="0.0001"
                  value={formData.valorDefecto}
                  onChange={e => setFormData({ ...formData, valorDefecto: e.target.value })}
                  placeholder="0.0000"
                  required
                />
              </div>
              
              <div className="form-row">
                <label>Modo Base:</label>
                <select
                  value={formData.modoBase}
                  onChange={e => setFormData({ ...formData, modoBase: e.target.value })}
                >
                  <option value="COSTO_BASE">Costo Base</option>
                  <option value="SUBTOTAL_ACUMULADO">Subtotal Acumulado</option>
                  <option value="SUBTOTAL_SIN_IVA">Subtotal sin IVA</option>
                  <option value="SUBTOTAL_ANTES_DE_IMPUESTOS_FINALES">Antes de Impuestos Finales</option>
                </select>
              </div>
              
              <div className="form-row">
                <label>Prioridad de Aplicación:</label>
                <input
                  type="number"
                  min="1"
                  value={formData.prioridadAplicacion}
                  onChange={e => setFormData({ ...formData, prioridadAplicacion: e.target.value })}
                  placeholder="1"
                />
                <small>Define el orden de aplicación del componente</small>
              </div>
              
              <div className="form-row checkbox-row">
                <label>
                  <input
                    type="checkbox"
                    checked={formData.editableEnProducto}
                    onChange={e => setFormData({ ...formData, editableEnProducto: e.target.checked })}
                  />
                  Editable en Producto
                </label>
                <small>Si está marcado, el valor puede modificarse por producto</small>
              </div>
              
              <div className="form-row checkbox-row">
                <label>
                  <input
                    type="checkbox"
                    checked={formData.obligatorio}
                    onChange={e => setFormData({ ...formData, obligatorio: e.target.checked })}
                  />
                  Obligatorio
                </label>
                <small>Si está marcado, se incluye automáticamente en nuevos productos</small>
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
            onClick={() => { setActiveTab("activos"); setBusqueda(""); }}
          >
            Activos
          </button>
          <button
            className={activeTab === "eliminados" ? "active" : ""}
            onClick={() => { setActiveTab("eliminados"); setBusqueda(""); }}
          >
            Eliminados
          </button>
        </div>

        <div className="maestro-toolbar">
          {activeTab === "activos" && !mostrarForm && (
            <button onClick={abrirNuevo} className="btn-primary">+ Nuevo</button>
          )}
          <div className="maestro-busqueda">
            <input
              type="text"
              placeholder="Buscar por nombre (mín. 2 caracteres)"
              value={busqueda}
              onChange={e => setBusqueda(e.target.value)}
            />
          </div>
        </div>

        {loading ? (
          <div className="maestro-loading">Cargando...</div>
        ) : (
          <>
            <table className="maestro-table">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Nombre</th>
                  <th>Tipo</th>
                  <th>Valor</th>
                  <th>Modo Base</th>
                  <th>Prioridad</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {componentes.length === 0 ? (
                  <tr>
                    <td colSpan="7" className="maestro-empty">
                      No hay componentes {activeTab === "activos" ? "activos" : "eliminados"}
                    </td>
                  </tr>
                ) : (
                  componentes.map(comp => (
                    <tr key={comp.codigo}>
                      <td>{comp.codigo}</td>
                      <td>{comp.nombre}</td>
                      <td>{getTipoComponenteLabel(comp.tipoComponente)}</td>
                      <td>
                        {comp.tipoValor === "PORCENTAJE" 
                          ? `${parseFloat(comp.valorDefecto || 0).toFixed(2)}%`
                          : `$${parseFloat(comp.valorDefecto || 0).toFixed(2)}`
                        }
                      </td>
                      <td>{getModoBaseLabel(comp.modoBase)}</td>
                      <td>{comp.prioridadAplicacion}</td>
                      <td>
                        {activeTab === "activos" ? (
                          <>
                            <button onClick={() => abrirEditar(comp)} className="btn-action btn-edit">Editar</button>
                            <button onClick={() => eliminar(comp)} className="btn-action btn-delete">Desactivar</button>
                          </>
                        ) : (
                          <button onClick={() => activar(comp)} className="btn-action btn-activate">Activar</button>
                        )}
                      </td>
                    </tr>
                  ))
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
