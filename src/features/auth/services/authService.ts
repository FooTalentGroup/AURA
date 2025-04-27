// src/features/auth/services/authService.ts

import { api } from "../../../core/services/api";
import {
  AuthResponse,
  LoginCredentials,
  RegisterCredentials,
} from "../types/auth.types";

export const authService = {
  login: async (credentials: LoginCredentials): Promise<AuthResponse> => {
    return api.post<AuthResponse>("/auth/login", credentials);
  },

  register: async (userData: RegisterCredentials): Promise<AuthResponse> => {
    return api.post<AuthResponse>("/auth/register", userData);
  },

  getCurrentUser: async (token: string): Promise<any> => {
    return api.get<any>("/auth/me", token);
  },

  // Método para verificar si el token es válido
  verifyToken: async (token: string): Promise<boolean> => {
    try {
      await api.get("/auth/verify", token);
      return true;
    } catch (error) {
      return false;
    }
  },

  // Obtener el token almacenado
  getStoredToken: (): string | null => {
    return localStorage.getItem("auth_token");
  },

  // Guardar el token en localStorage
  setStoredToken: (token: string): void => {
    localStorage.setItem("auth_token", token);
  },

  // Eliminar el token cuando se cierra sesión
  removeStoredToken: (): void => {
    localStorage.removeItem("auth_token");
  },
};
