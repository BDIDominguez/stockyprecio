export default function CategoriaTabla({ categorias, onEditar, onEliminar }) {
  return (
    <table className="tabla">
      <thead>
        <tr>
          <th>Codigo</th>
          <th>Nombre</th>
          <th>Descripcion</th>
          <th>Acciones</th>
        </tr>
      </thead>

      <tbody>
        {categorias.map((cat) => (
          <tr key={cat.codigo}>
            <td>{cat.codigo}</td>
            <td>{cat.nombre}</td>
            <td>{cat.descripcion}</td>

            <td>
              <button onClick={() => onEditar(cat)}>Editar</button>
              <button onClick={() => onEliminar(cat.codigo)}>Eliminar</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}