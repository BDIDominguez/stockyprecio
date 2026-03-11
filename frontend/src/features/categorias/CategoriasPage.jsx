import { useEffect, useState } from "react";
import { obtenerCategorias } from "./categoriasApi";

function CategoriasPage() {

  const [categorias, setCategorias] = useState([]);
  const [pagina, setPagina] = useState(0);
  const [totalPaginas, setTotalPaginas] = useState(0);
  const [busqueda, setBusqueda] = useState("");
  const [busquedaDebounce, setBusquedaDebounce] = useState("");

  useEffect(() => {
    const timer = setTimeout(() => {setBusquedaDebounce(busqueda);}, 500);
    return () => clearTimeout(timer);
  }, [busqueda]);

  useEffect(() => {
    async function cargar() {
      const data = await obtenerCategorias(pagina, busquedaDebounce);
      setCategorias(data.content);
      setTotalPaginas(data.totalPages);
    }
    cargar();
  }, [pagina, busquedaDebounce]);

  return (
    <div>
      <h2>Categorías</h2>
      <input type="text" placeholder="Buscar categoría..." value={busqueda} onChange={(e) => { setBusqueda(e.target.value); setPagina(0);}}/>
      <ul>{categorias.map((cat) => (<li key={cat.codigo}>{cat.nombre}</li>))}</ul>
      <div>
        <button disabled={pagina === 0} onClick={() => setPagina(pagina - 1)}>Anterior</button>
        <span>Página {pagina + 1} de {totalPaginas}</span>
        <button disabled={pagina + 1 >= totalPaginas} onClick={() => setPagina(pagina + 1)}>Siguiente</button>
      </div>
    </div>
  );
}

export default CategoriasPage;
