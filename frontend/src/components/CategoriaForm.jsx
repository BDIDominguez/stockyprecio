import { useState, useEffect } from "react";

export default function CategoriaForm({ onGuardar, categoriaEditando }) {
  const [codigo, setCodigo] = useState("");
  const [nombre, setNombre] = useState("");
  const [descripcion, setDescripcion] = useState("");

  useEffect(() => {
    if (!categoriaEditando) return;

    setCodigo(categoriaEditando.codigo);
    setNombre(categoriaEditando.nombre);
    setDescripcion(categoriaEditando.descripcion ?? "");
  }, [categoriaEditando]);

  function handleSubmit(e) {
    e.preventDefault();

    onGuardar({
      codigo: Number(codigo),
      nombre,
      descripcion,
    });

    setCodigo("");
    setNombre("");
    setDescripcion("");
  }

  return (
    <form className="formulario" onSubmit={handleSubmit}>
      <input placeholder="Codigo" value={codigo} onChange={(e) => setCodigo(e.target.value)} required />

      <input placeholder="Nombre" value={nombre} onChange={(e) => setNombre(e.target.value)} required />

      <input placeholder="Descripcion" value={descripcion} onChange={(e) => setDescripcion(e.target.value)} />

      <button type="submit">Guardar</button>
    </form>
  );
}
