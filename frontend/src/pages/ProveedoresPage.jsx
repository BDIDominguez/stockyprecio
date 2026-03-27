import { useState, useEffect, useCallback } from "react";
import Layout from "../components/Layout";
import Paginacion from "../components/Paginacion";
import AlertaError from "../components/AlertaError";
import { apiGet, apiPost, apiPut, apiDelete, apiPatch } from "../api/apiCliente";
import "./MaestroPage.css";

export default function ProveedoresPage() {
  const [activeTab, setActiveTab] = useState("activos");
  const [proveedores, setProveedores] = useState([]);
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
    contacto: "",
    telefono: "",
    email: "",
    direccion: "",
  });

  const cargarDatos = useCallback(async (page = 0) => {
    setLoading(true);
    setError(null);
    try {
      const params = { page, size: 10, sort: "nombre", activo: activeTab === "activos" };
      if (busqueda.length >= 2) params.nombre = busqueda;
      const data = await apiGet("/proveedores", params);
      setProveedores(data.content || []);
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
      const codigo = await apiGet("/proveedores/siguiente-codigo");
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
    setFormData({ codigo: "", nombre: "", contacto: "", telefono: "", email: "", direccion: "" });
    setMostrarForm(true);
  }

  // Cargar siguiente código cuando se muestra el formulario para crear
  useEffect(() => {
    if (mostrarForm && !editando && formData.codigo === "") {
      cargarSiguienteCodigo();
    }
  }, [mostrarForm, editando]);

  function abrirEditar(proveedor) {
    setEditando(proveedor);
    setFormData({
      codigo: proveedor.codigo,
      nombre: proveedor.nombre,
      contacto: proveedor.contacto || "",
      telefono: proveedor.telefono || "",
      email: proveedor.email || "",
      direccion: proveedor.direccion || "",
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
      const datos = { nombre: formData.nombre, contacto: formData.contacto, telefono: formData.telefono, email: formData.email, direccion: formData.direccion };
      if (editando) {
        await apiPut(`/proveedores/${editando.codigo}`, datos);
      } else {
        await apiPost("/proveedores", { codigo: codigoNum, ...datos });
      }
      cerrarForm();
      cargarDatos();
    } catch (err) {
      setError(err);
    }
  }

  async function eliminar(proveedor) {
    if (!confirm(`¿Desactivar el proveedor "${proveedor.nombre}"?`)) return;
    setError(null);
    try {
      await apiDelete(`/proveedores/${proveedor.codigo}`);
      cargarDatos();
    } catch (err) {
      setError(err);
    }
  }

  async function activar(proveedor) {
    setError(null);
    try {
      await apiPatch(`/proveedores/${proveedor.codigo}/activar`);
      cargarDatos();
    } catch (err) {
      setError(err);
    }
  }

  return (
    <Layout>
      <div className="maestro-page">
        <div className="maestro-header">
          <h1>Proveedores</h1>
        </div>

        <AlertaError error={error} onClose={() => setError(null)} />

        {mostrarForm && (
          <div className="maestro-form-panel">
            <h3>{editando ? "Editar Proveedor" : "Nuevo Proveedor"}</h3>
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
                <label>Contacto:</label>
                <input type="text" value={formData.contacto} onChange={e => setFormData({ ...formData, contacto: e.target.value })} />
              </div>
              <div className="form-row">
                <label>Teléfono:</label>
                <input type="text" value={formData.telefono} onChange={e => setFormData({ ...formData, telefono: e.target.value })} />
              </div>
              <div className="form-row">
                <label>Email:</label>
                <input type="email" value={formData.email} onChange={e => setFormData({ ...formData, email: e.target.value })} />
              </div>
              <div className="form-row">
                <label>Dirección:</label>
                <input type="text" value={formData.direccion} onChange={e => setFormData({ ...formData, direccion: e.target.value })} />
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
                  <th>Contacto</th>
                  <th>Teléfono</th>
                  <th>Email</th>
                  <th>Dirección</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {proveedores.length === 0 ? (
                  <tr><td colSpan="7" className="maestro-empty">No hay proveedores {activeTab === "activos" ? "activos" : "eliminados"}</td></tr>
                ) : (
                  proveedores.map(p => (
                    <tr key={p.codigo}>
                      <td>{p.codigo}</td>
                      <td>{p.nombre}</td>
                      <td>{p.contacto || "-"}</td>
                      <td>{p.telefono || "-"}</td>
                      <td>{p.email || "-"}</td>
                      <td>{p.direccion || "-"}</td>
                      <td>
                        {activeTab === "activos" ? (
                          <><button onClick={() => abrirEditar(p)} className="btn-action btn-edit">Editar</button><button onClick={() => eliminar(p)} className="btn-action btn-delete">Desactivar</button></>
                        ) : (
                          <button onClick={() => activar(p)} className="btn-action btn-activate">Activar</button>
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
