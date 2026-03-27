import Sidebar from "./Sidebar";
import SelectorSucursal from "./SelectorSucursal";
import "./Layout.css";

export default function Layout({ children, showSucursalSelector = false }) {
  return (
    <div className="layout">
      <Sidebar />
      <main className="layout-main">
        {showSucursalSelector && (
          <header className="layout-header">
            <SelectorSucursal />
          </header>
        )}
        <div className="layout-content">{children}</div>
      </main>
    </div>
  );
}
