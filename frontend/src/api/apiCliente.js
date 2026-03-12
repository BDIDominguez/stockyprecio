const API_BASE_URL = "http://localhost:8080/api";

export async function apiGet(endpoint) {
  const response = await fetch(`${API_BASE_URL}${endpoint}`);

  if (!response.ok) {
    throw new Error("Error en GET");
  }

  return response.json();
}

export async function apiPost(endpoint, data) {
  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  });
  
  const body = await response.json();
  
  if (!response.ok) {
    console.log(body.mensaje);
    throw new Error(body.mensaje || "Error en POST");
  }

  return body;
}

export async function apiPut(endpoint, data) {
  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  });

  if (!response.ok) {
    throw new Error("Error en PUT");
  }

  return response.json();
}

export async function apiDelete(endpoint) {
  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    method: "DELETE"
  });

  if (!response.ok) {
    throw new Error("Error en DELETE");
  }
}