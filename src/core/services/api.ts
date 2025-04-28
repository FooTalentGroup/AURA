const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export const api = {
  get: async <T>(endpoint: string, token?: string): Promise<T> => {
    const headers: HeadersInit = {
      "Content-Type": "application/json",
    };

    if (token) {
      headers["Authorization"] = `Bearer ${token}`;
    }

    const response = await fetch(`${BASE_URL}${endpoint}`, {
      method: "GET",
      headers,
    });

    if (!response.ok) {
      throw new Error(`Error: ${response.status}`);
    }

    return response.json();
  },

  post: async <T, D>(endpoint: string, data: D, token?: string): Promise<T> => {
    const headers: HeadersInit = {
      "Content-Type": "application/json",
    };

    if (token) {
      headers["Authorization"] = `Bearer ${token}`;
    }

    const response = await fetch(`${BASE_URL}${endpoint}`, {
      method: "POST",
      headers,
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.message || `Error: ${response.status}`);
    }

    return response.json();
  },

  put: async <T, D>(endpoint: string, data: D, token?: string): Promise<T> => {
    const headers: HeadersInit = {
      "Content-Type": "application/json",
    };

    if (token) {
      headers["Authorization"] = `Bearer ${token}`;
    }

    const response = await fetch(`${BASE_URL}${endpoint}`, {
      method: "PUT",
      headers,
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.message || `Error: ${response.status}`);
    }

    return response.json();
  },

  delete: async <T>(endpoint: string, token?: string): Promise<T> => {
    const headers: HeadersInit = {
      "Content-Type": "application/json",
    };

    if (token) {
      headers["Authorization"] = `Bearer ${token}`;
    }

    const response = await fetch(`${BASE_URL}${endpoint}`, {
      method: "DELETE",
      headers,
    });

    if (!response.ok) {
      throw new Error(`Error: ${response.status}`);
    }

    return response.json();
  },
};
