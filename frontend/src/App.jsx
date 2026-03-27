import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { SucursalProvider } from "./context/SucursalContext";
import CategoriasPage from "./pages/CategoriasPage";
import ProveedoresPage from "./pages/ProveedoresPage";
import ComponentesCostoPage from "./pages/ComponentesCostoPage";
import ListasPreciosPage from "./pages/ListasPreciosPage";
import SucursalesPage from "./pages/SucursalesPage";
import ProductosPage from "./pages/ProductosPage";

function App() {
  return (
    <SucursalProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/categorias" replace />} />
          <Route path="/categorias" element={<CategoriasPage />} />
          <Route path="/proveedores" element={<ProveedoresPage />} />
          <Route path="/componentes-costo" element={<ComponentesCostoPage />} />
          <Route path="/listas-precios" element={<ListasPreciosPage />} />
          <Route path="/sucursales" element={<SucursalesPage />} />
          <Route path="/productos" element={<ProductosPage />} />
        </Routes>
      </BrowserRouter>
    </SucursalProvider>
  );
}

export default App;
