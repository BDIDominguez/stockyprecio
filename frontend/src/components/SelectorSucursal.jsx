import { useSucursal } from "../context/SucursalContext";
import "./SelectorSucursal.css";

export default function SelectorSucursal() {
  const { sucursales, sucursalActiva, seleccionarSucursal, loading } = useSucursal();

  if (loading) {
    return <span className="selector-sucursal-loading">Cargando...</span>;
  }

  if (sucursales.length === 0) {
    return <span className="selector-sucursal-empty">Sin sucursales</span>;
  }

  return (
    <div className="selector-sucursal">
      <label>Sucursal:</label>
      <select
        value={sucursalActiva?.codigo || ""}
        onChange={(e) => {
          const selected = sucursales.find(s => s.codigo === parseInt(e.target.value));
          seleccionarSucursal(selected);
        }}
      >
        {sucursales.map((s) => (
          <option key={s.codigo} value={s.codigo}>
            {s.codigo} - {s.nombre}
          </option>
        ))}
      </select>
    </div>
  );
}
