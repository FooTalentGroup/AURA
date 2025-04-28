import { api } from "../../../core/services/api";
import {
  AuthResponse,
  LoginCredentials,
  RegisterCredentials,
} from "../types/auth.types";

interface CurrentUser {
  id: number;
  email: string;
  name: string;
}

export const authService = {
  login: async (credentials: LoginCredentials): Promise<AuthResponse> => {
    return api.post<AuthResponse, LoginCredentials>("/auth/login", credentials);
  },

  register: async (userData: RegisterCredentials): Promise<AuthResponse> => {
    return api.post<AuthResponse, RegisterCredentials>(
      "/auth/register",
      userData
    );
  },

  getCurrentUser: async (token: string): Promise<CurrentUser> => {
    return api.get<CurrentUser>("/auth/me", token);
  },

  verifyToken: async (token: string): Promise<boolean> => {
    try {
      await api.get("/auth/verify", token);
      return true;
    } catch (error) {
      if (error instanceof Error) {
        console.error(error.message);
      } else {
        console.error("Unknown error occurred");
      }
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
