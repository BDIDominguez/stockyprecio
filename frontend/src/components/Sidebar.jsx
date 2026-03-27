import { NavLink } from "react-router-dom";
import "./Sidebar.css";

const menuItems = [
  { path: "/categorias", label: "Categorías", icon: "📁" },
  { path: "/proveedores", label: "Proveedores", icon: "🏢" },
  { path: "/componentes-costo", label: "Componentes de Costo", icon: "💰" },
  { path: "/listas-precios", label: "Listas de Precios", icon: "📋" },
  { path: "/sucursales", label: "Sucursales", icon: "🏪" },
  { path: "/productos", label: "Productos", icon: "📦" },
];

export default function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <h2>📦 Stock System</h2>
      </div>
      <nav className="sidebar-nav">
        {menuItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) =>
              `sidebar-link ${isActive ? "active" : ""}`
            }
          >
            <span className="sidebar-icon">{item.icon}</span>
            <span className="sidebar-label">{item.label}</span>
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}
