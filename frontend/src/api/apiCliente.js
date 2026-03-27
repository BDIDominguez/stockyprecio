const API_BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";

/**
 * Maneja errores de respuesta de la API
 * @param {Response} response 
 * @returns {Promise<any>}
 */
async function handleResponse(response) {
  const contentType = response.headers.get("content-type");
  const isJson = contentType && contentType.includes("application/json");
  const body = isJson ? await response.json() : null;

  if (!response.ok) {
    const error = {
      status: response.status,
      error: response.statusText,
      codigo: body?.codigo || `ERR-${response.status}`,
      mensaje: body?.mensaje || "Error desconocido del servidor",
      detalle: body?.detalle || null,
      errores: body?.errores || null,
    };
    throw error;
  }

  return body;
}

/**
 * Realiza una petición GET
 */
export async function apiGet(endpoint, params = {}) {
  const url = new URL(`${API_BASE_URL}${endpoint}`);
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== "") {
      url.searchParams.append(key, value);
    }
  });

  const response = await fetch(url.toString(), {
    method: "GET",
    headers: { "Accept": "application/json" },
  });

  return handleResponse(response);
}

/**
 * Realiza una petición POST
 */
export async function apiPost(endpoint, data) {
  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });

  return handleResponse(response);
}

/**
 * Realiza una petición PUT
 */
export async function apiPut(endpoint, data) {
  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });

  return handleResponse(response);
}

/**
 * Realiza una petición PATCH
 */
export async function apiPatch(endpoint, data = {}) {
  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: Object.keys(data).length > 0 ? JSON.stringify(data) : undefined,
  });

  return handleResponse(response);
}

/**
 * Realiza una petición DELETE
 */
export async function apiDelete(endpoint) {
  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    method: "DELETE",
  });

  if (!response.ok) {
    const contentType = response.headers.get("content-type");
    const isJson = contentType && contentType.includes("application/json");
    const body = isJson ? await response.json() : null;

    const error = {
      status: response.status,
      error: response.statusText,
      mensaje: body?.mensaje || "Error al eliminar",
    };
    throw error;
  }

  return true;
}

export { API_BASE_URL };
