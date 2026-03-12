import { useEffect, useState } from "react";
import CategoriaForm from "../components/CategoriaForm";
import CategoriaTabla from "../components/CategoriaTabla";
import { apiGet, apiPost, apiPut, apiDelete } from "../api/apiCliente";
import "../styles/categorias.css";

export default function CategoriasPage() {

  const [categorias, setCategorias] = useState([]);
  const [editando, setEditando] = useState(null);

  async function cargarCategorias() {
    const data = await apiGet("/categorias");
    setCategorias(data.content);
  }

  useEffect(() => {
    cargarCategorias();
  }, []);

  async function guardarCategoria(cat) {
  try {
    if (editando) {
      await apiPut(`/categorias/${cat.codigo}`, cat);
    } else {
      await apiPost("/categorias", cat);
    }
    setEditando(null);
    cargarCategorias();
  } catch (error) {
    alert(error.message);
  }
}

  async function eliminarCategoria(codigo) {
    await apiDelete(`/categorias/${codigo}`);
    cargarCategorias();
  }

  function editarCategoria(cat) {
    setEditando(cat);
  }

  return (
    <div className="contenedor">

      <h1>Categorias</h1>

      <CategoriaForm
        onGuardar={guardarCategoria}
        categoriaEditando={editando}
      />

      <CategoriaTabla
        categorias={categorias}
        onEditar={editarCategoria}
        onEliminar={eliminarCategoria}
      />

    </div>
  );
}