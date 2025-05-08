import React, { createContext, ReactNode, useReducer, useEffect } from "react";
import { authApi } from "./authApi";
import { AuthState, authReducer } from "../../../store/authStore";
import { useLocation } from "react-router-dom";
import {
  AuthContextType,
  CurrentUser,
  RegisterReceptionistPayload,
  RegisterProfessionalPayload,
  UserResponse,
} from "../types/auth.types";

const initialState: AuthState = {
  user: null,
  isAuthenticated: false,
  isLoading: true,
  error: null,
};

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(authReducer, initialState);
  const location = useLocation();

  useEffect(() => {
    const publicPaths = ["/login", "/register", "/forgot-password"];
    // Iniciar carga de estado
    dispatch({ type: "INIT" });

    // Si estamos en una ruta pública, no llamamos a /auth/me y marcamos como cargado
    if (publicPaths.includes(location.pathname)) {
      dispatch({ type: "LOGOUT" });
      return;
    }

    // En rutas privadas, rehidratar sesión desde backend
    authApi.me()
      .then((res: UserResponse) => {
        const user: CurrentUser = {
          id: res.id,
          username: res.email || "",
          email: res.email || "",
          roles: res.roles || [],
        };
        dispatch({ type: "LOGIN_SUCCESS", payload: user });
      })
      .catch(() => {
        dispatch({ type: "LOGOUT" });
      });
  }, [location.pathname]);

  const login = async (
    email: string,
    password: string
  ): Promise<boolean> => {
    dispatch({ type: "LOGIN_REQUEST" });
    try {
      const res = await authApi.login({ email, password });
      const user: CurrentUser = {
        id: res.Id,
        username: res.email,
        email,
      };
      dispatch({ type: "LOGIN_SUCCESS", payload: user });
      return true;
    } catch (err) {
      dispatch({ type: "LOGIN_FAILURE", payload: (err as Error).message });
      return false;
    }
  };

  const logout = async (): Promise<void> => {
    try {
      await authApi.logout();
    } catch (err) {
      console.warn("logout en backend falló:", err);
    } finally {
      dispatch({ type: "LOGOUT" });
    }
  };

  const clearError = () => dispatch({ type: "CLEAR_ERROR" });

  const isAdmin = !!state.user?.roles?.includes("ADMIN");
  const isProfessional = !!state.user?.roles?.includes("PROFESSIONAL");
  const isReceptionist = !!state.user?.roles?.includes("RECEPTIONIST");

  return (
    <AuthContext.Provider
      value={{
        state,
        login,
        logout,
        clearError,
        registerReceptionist: async (data: RegisterReceptionistPayload) => {
          await authApi.registerReceptionist(data);
        },
        registerProfessional: async (data: RegisterProfessionalPayload) => {
          await authApi.registerProfessional(data);
        },
        isAdmin,
        isProfessional,
        isReceptionist,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
