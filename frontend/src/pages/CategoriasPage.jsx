import { useState, useEffect, useCallback } from "react";
import Layout from "../components/Layout";
import Paginacion from "../components/Paginacion";
import AlertaError from "../components/AlertaError";
import { apiGet, apiPost, apiPut, apiDelete, apiPatch } from "../api/apiCliente";
import "./MaestroPage.css";

export default function CategoriasPage() {
  const [activeTab, setActiveTab] = useState("activos");
  const [categorias, setCategorias] = useState([]);
  const [pageData, setPageData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [editando, setEditando] = useState(null);
  const [mostrarForm, setMostrarForm] = useState(false);
  const [busqueda, setBusqueda] = useState("");
   const [formData, setFormData] = useState({ codigo: "", nombre: "", descripcion: "" });
  const [cargandoCodigo, setCargandoCodigo] = useState(false);

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
      if (busqueda.length >= 2) {
        params.nombre = busqueda;
      }
      const data = await apiGet("/categorias", params);
      setCategorias(data.content || []);
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
      const codigo = await apiGet("/categorias/siguiente-codigo");
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
    setFormData({ codigo: "", nombre: "", descripcion: "" });
    setMostrarForm(true);
  }

  // Cargar siguiente código cuando se muestra el formulario para crear
  useEffect(() => {
    if (mostrarForm && !editando && formData.codigo === "") {
      cargarSiguienteCodigo();
    }
  }, [mostrarForm, editando]);

  function abrirEditar(cat) {
    setEditando(cat);
    setFormData({
      codigo: cat.codigo,
      nombre: cat.nombre,
      descripcion: cat.descripcion || "",
    });
    setMostrarForm(true);
  }

  function cerrarForm() {
    setMostrarForm(false);
    setEditando(null);
    setFormData({ codigo: "", nombre: "", descripcion: "" });
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
      if (editando) {
        await apiPut(`/categorias/${editando.codigo}`, {
          nombre: formData.nombre,
          descripcion: formData.descripcion,
        });
      } else {
        await apiPost("/categorias", {
          codigo: codigoNum,
          nombre: formData.nombre,
          descripcion: formData.descripcion,
        });
      }
      cerrarForm();
      cargarDatos();
    } catch (err) {
      setError(err);
    }
  }

  async function eliminar(cat) {
    if (!confirm(`¿Desactivar la categoría "${cat.nombre}"?`)) return;
    setError(null);
    try {
      await apiDelete(`/categorias/${cat.codigo}`);
      cargarDatos();
    } catch (err) {
      setError(err);
    }
  }

  async function activar(cat) {
    setError(null);
    try {
      await apiPatch(`/categorias/${cat.codigo}/activar`);
      cargarDatos();
    } catch (err) {
      setError(err);
    }
  }

  return (
    <Layout>
      <div className="maestro-page">
        <div className="maestro-header">
          <h1>Categorías</h1>
        </div>

        <AlertaError error={error} onClose={() => setError(null)} />

        {mostrarForm && (
          <div className="maestro-form-panel">
            <h3>{editando ? "Editar Categoría" : "Nueva Categoría"}</h3>
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
                  required
                />
              </div>
              <div className="form-row">
                <label>Descripción:</label>
                <input
                  type="text"
                  value={formData.descripcion}
                  onChange={e => setFormData({ ...formData, descripcion: e.target.value })}
                />
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
            <button onClick={abrirNuevo} className="btn-primary">+ Nueva</button>
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
                  <th>Descripción</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {categorias.length === 0 ? (
                  <tr>
                    <td colSpan="4" className="maestro-empty">
                      No hay categorías {activeTab === "activos" ? "activas" : "eliminadas"}
                    </td>
                  </tr>
                ) : (
                  categorias.map(cat => (
                    <tr key={cat.codigo}>
                      <td>{cat.codigo}</td>
                      <td>{cat.nombre}</td>
                      <td>{cat.descripcion || "-"}</td>
                      <td>
                        {activeTab === "activos" ? (
                          <>
                            <button onClick={() => abrirEditar(cat)} className="btn-action btn-edit">Editar</button>
                            <button onClick={() => eliminar(cat)} className="btn-action btn-delete">Desactivar</button>
                          </>
                        ) : (
                          <button onClick={() => activar(cat)} className="btn-action btn-activate">Activar</button>
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
