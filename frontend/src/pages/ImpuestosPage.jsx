import { useState, useEffect, useCallback } from "react";
import Layout from "../components/Layout";
import Paginacion from "../components/Paginacion";
import AlertaError from "../components/AlertaError";
import { apiGet, apiPost, apiPut, apiDelete, apiPatch } from "../api/apiCliente";
import "./MaestroPage.css";

export default function ImpuestosPage() {
  const [activeTab, setActiveTab] = useState("activos");
  const [impuestos, setImpuestos] = useState([]);
  const [pageData, setPageData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [editando, setEditando] = useState(null);
  const [mostrarForm, setMostrarForm] = useState(false);
  const [busqueda, setBusqueda] = useState("");
  const [cargandoCodigo, setCargandoCodigo] = useState(false);
  const [formData, setFormData] = useState({ codigo: "", nombre: "", descripcion: "", porcentaje: "" });

  const cargarDatos = useCallback(async (page = 0) => {
    setLoading(true);
    setError(null);
    try {
      const params = { page, size: 10, sort: "nombre", activo: activeTab === "activos" };
      if (busqueda.length >= 2) params.nombre = busqueda;
      const data = await apiGet("/impuestos", params);
      setImpuestos(data.content || []);
      setPageData(data);
    } catch (err) {
      setError(err);
    } finally {
      setLoading(false);
    }
  }, [activeTab, busqueda]);

  useEffect(() => { cargarDatos(); }, [cargarDatos]);

  async function cargarSiguienteCodigo() {
    setCargandoCodigo(true);
    try {
      const codigo = await apiGet("/impuestos/siguiente-codigo");
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
    setFormData({ codigo: "", nombre: "", descripcion: "", porcentaje: "" });
    setMostrarForm(true);
  }

  // Cargar siguiente código cuando se muestra el formulario para crear
  useEffect(() => {
    if (mostrarForm && !editando && formData.codigo === "") {
      cargarSiguienteCodigo();
    }
  }, [mostrarForm, editando]);

  function abrirEditar(impuesto) {
    setEditando(impuesto);
    setFormData({
      codigo: impuesto.codigo,
      nombre: impuesto.nombre,
      descripcion: impuesto.descripcion || "",
      porcentaje: impuesto.porcentaje,
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
    
    // Validar que el código sea mayor a 0
    const codigoNum = parseInt(formData.codigo);
    if (!codigoNum || codigoNum <= 0) {
      setError({ mensaje: "El código debe ser mayor a 0", status: 400 });
      return;
    }
    
    try {
      const datos = { nombre: formData.nombre, descripcion: formData.descripcion, porcentaje: parseFloat(formData.porcentaje) };
      if (editando) {
        await apiPut(`/impuestos/${editando.codigo}`, datos);
      } else {
        await apiPost("/impuestos", { codigo: codigoNum, ...datos });
      }
      cerrarForm();
      cargarDatos();
    } catch (err) {
      setError(err);
    }
  }

  async function eliminar(impuesto) {
    if (!confirm(`¿Desactivar el impuesto "${impuesto.nombre}"?`)) return;
    setError(null);
    try {
      await apiDelete(`/impuestos/${impuesto.codigo}`);
      cargarDatos();
    } catch (err) {
      setError(err);
    }
  }

  async function activar(impuesto) {
    setError(null);
    try {
      await apiPatch(`/impuestos/${impuesto.codigo}/activar`);
      cargarDatos();
    } catch (err) {
      setError(err);
    }
  }

  return (
    <Layout>
      <div className="maestro-page">
        <div className="maestro-header">
          <h1>Impuestos</h1>
        </div>

        <AlertaError error={error} onClose={() => setError(null)} />

        {mostrarForm && (
          <div className="maestro-form-panel">
            <h3>{editando ? "Editar Impuesto" : "Nuevo Impuesto"}</h3>
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
                <input type="text" value={formData.nombre} onChange={e => setFormData({ ...formData, nombre: e.target.value })} required />
              </div>
              <div className="form-row">
                <label>Descripción:</label>
                <input type="text" value={formData.descripcion} onChange={e => setFormData({ ...formData, descripcion: e.target.value })} />
              </div>
              <div className="form-row">
                <label>Porcentaje:</label>
                <input type="number" step="0.01" value={formData.porcentaje} onChange={e => setFormData({ ...formData, porcentaje: e.target.value })} required />
              </div>
              <div className="form-actions">
                <button type="submit" className="btn-primary">Guardar</button>
                <button type="button" onClick={cerrarForm} className="btn-secondary">Cancelar</button>
              </div>
            </form>
          </div>
        )}

        <div className="maestro-tabs">
          <button className={activeTab === "activos" ? "active" : ""} onClick={() => { setActiveTab("activos"); setBusqueda(""); }}>Activos</button>
          <button className={activeTab === "eliminados" ? "active" : ""} onClick={() => { setActiveTab("eliminados"); setBusqueda(""); }}>Eliminados</button>
        </div>

        <div className="maestro-toolbar">
          {activeTab === "activos" && !mostrarForm && <button onClick={abrirNuevo} className="btn-primary">+ Nuevo</button>}
          <div className="maestro-busqueda">
            <input type="text" placeholder="Buscar por nombre (mín. 2 caracteres)" value={busqueda} onChange={e => setBusqueda(e.target.value)} />
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
                  <th>Descripción</th>
                  <th>Porcentaje</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {impuestos.length === 0 ? (
                  <tr><td colSpan="5" className="maestro-empty">No hay impuestos {activeTab === "activos" ? "activos" : "eliminados"}</td></tr>
                ) : (
                  impuestos.map(i => (
                    <tr key={i.codigo}>
                      <td>{i.codigo}</td>
                      <td>{i.nombre}</td>
                      <td>{i.descripcion || "-"}</td>
                      <td>{i.porcentaje?.toFixed(2)}%</td>
                      <td>
                        {activeTab === "activos" ? (
                          <><button onClick={() => abrirEditar(i)} className="btn-action btn-edit">Editar</button><button onClick={() => eliminar(i)} className="btn-action btn-delete">Desactivar</button></>
                        ) : (
                          <button onClick={() => activar(i)} className="btn-action btn-activate">Activar</button>
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
