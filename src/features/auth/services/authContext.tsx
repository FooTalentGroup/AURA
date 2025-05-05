import React, { createContext, ReactNode, useReducer, useEffect } from "react";
import { authApi } from "./authApi";
import { AuthState, authReducer } from "../../../store/authStore";
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
  isLoading: false,
  error: null,
};

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(authReducer, initialState);

  // Rehidrata sesión usando /auth/me en lugar de sessionStorage
  useEffect(() => {
    dispatch({ type: "INIT" });
    authApi.me()
      .then((res: UserResponse) => {
        const user: CurrentUser = {
          id: res.id,
          username: res.email || "",
          email: res.email || "",
        };
        dispatch({ type: "LOGIN_SUCCESS", payload: user });
      })
      .catch(() => {
        dispatch({ type: "LOGOUT" });
      });
  }, []);

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
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
