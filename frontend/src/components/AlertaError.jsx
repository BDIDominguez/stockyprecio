import "./AlertaError.css";

export default function AlertaError({ error, onClose }) {
  if (!error) return null;

  return (
    <div className="alerta-error">
      <div className="alerta-error-header">
        <span className="alerta-error-icon">⚠️</span>
        <strong>Error {error.status}: {error.codigo}</strong>
        {onClose && (
          <button className="alerta-error-close" onClick={onClose}>
            ×
          </button>
        )}
      </div>
      <div className="alerta-error-body">
        <p className="alerta-error-mensaje">{error.mensaje}</p>
        {error.detalle && (
          <p className="alerta-error-detalle">{error.detalle}</p>
        )}
        {error.errores && Array.isArray(error.errores) && error.errores.length > 0 && (
          <ul className="alerta-error-errores">
            {error.errores.map((err, i) => (
              <li key={i}>{err}</li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}
