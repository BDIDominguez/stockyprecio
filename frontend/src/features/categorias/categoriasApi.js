import { apiFetch } from "../../api/apiClient";

export function obtenerCategorias(pagina = 0, nombre = "") {
  let endpoint = `/categorias?page=${pagina}&size=10`;
  if (nombre){
    endpoint += `&nombre=${encodeURIComponent(nombre)}`;
  }
  return apiFetch(endpoint)
}