import { createContext, useContext, useState, useEffect } from "react";
import { apiGet } from "../api/apiCliente";

const SucursalContext = createContext();

export function SucursalProvider({ children }) {
  const [sucursales, setSucursales] = useState([]);
  const [sucursalActiva, setSucursalActiva] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function cargarSucursales() {
      try {
        const data = await apiGet("/sucursal", { activo: true, size: 100 });
        const lista = data.content || [];
        setSucursales(lista);
        
        // Seleccionar la primera sucursal por defecto o la guardada
        const guardada = localStorage.getItem("sucursalActiva");
        const encontrada = lista.find(s => s.codigo === parseInt(guardada));
        setSucursalActiva(encontrada || lista[0] || null);
      } catch (error) {
        console.error("Error cargando sucursales:", error);
      } finally {
        setLoading(false);
      }
    }
    cargarSucursales();
  }, []);

  const seleccionarSucursal = (sucursal) => {
    setSucursalActiva(sucursal);
    if (sucursal) {
      localStorage.setItem("sucursalActiva", sucursal.codigo);
    }
  };

  return (
    <SucursalContext.Provider
      value={{ sucursales, sucursalActiva, seleccionarSucursal, loading }}
    >
      {children}
    </SucursalContext.Provider>
  );
}

export function useSucursal() {
  const context = useContext(SucursalContext);
  if (!context) {
    throw new Error("useSucursal debe usarse dentro de SucursalProvider");
  }
  return context;
}
