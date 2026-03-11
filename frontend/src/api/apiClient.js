const API_BASE_URL = "http://localhost:8080/api";

export async function apiFetch(endpoint) {
  const response = await fetch(`${API_BASE_URL}${endpoint}`);

  if (!response.ok) {
    throw new Error("Error en la llamada a la API");
  }

  return response.json();
}