import "./Paginacion.css";

export default function Paginacion({ pageData, onPageChange }) {
  if (!pageData) return null;

  const { number, totalPages, totalElements, size } = pageData;

  if (totalPages <= 1 && totalElements <= size) return null;

  const start = number * size + 1;
  const end = Math.min((number + 1) * size, totalElements);

  const getPageNumbers = () => {
    const pages = [];
    const maxVisible = 5;
    let startPage = Math.max(0, number - Math.floor(maxVisible / 2));
    let endPage = Math.min(totalPages - 1, startPage + maxVisible - 1);

    if (endPage - startPage < maxVisible - 1) {
      startPage = Math.max(0, endPage - maxVisible + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  };

  return (
    <div className="paginacion">
      <span className="paginacion-info">
        Mostrando {start}-{end} de {totalElements} registros
      </span>

      <div className="paginacion-controles">
        <button
          onClick={() => onPageChange(0)}
          disabled={number === 0}
          title="Primera página"
        >
          ««
        </button>
        <button
          onClick={() => onPageChange(number - 1)}
          disabled={number === 0}
          title="Página anterior"
        >
          «
        </button>

        {getPageNumbers().map((page) => (
          <button
            key={page}
            onClick={() => onPageChange(page)}
            className={page === number ? "active" : ""}
          >
            {page + 1}
          </button>
        ))}

        <button
          onClick={() => onPageChange(number + 1)}
          disabled={number >= totalPages - 1}
          title="Página siguiente"
        >
          »
        </button>
        <button
          onClick={() => onPageChange(totalPages - 1)}
          disabled={number >= totalPages - 1}
          title="Última página"
        >
          »»
        </button>
      </div>
    </div>
  );
}
