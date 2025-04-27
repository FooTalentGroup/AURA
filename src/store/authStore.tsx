import {
  //createContext,
  // useContext,
  useReducer,
  ReactNode,
  useEffect,
} from "react";
import { AuthState, AuthUser } from "../features/auth/types/auth.types";
import { authService } from "../features/auth/services/authService";
import { AuthContext } from "../features/auth/services/authContext";

// Estado inicial
const initialState: AuthState = {
  user: null,
  isAuthenticated: false,
  isLoading: true,
  error: null,
};

// Tipos de acciones
type AuthAction =
  | { type: "LOGIN_REQUEST" }
  | { type: "LOGIN_SUCCESS"; payload: AuthUser }
  | { type: "LOGIN_FAILURE"; payload: string }
  | { type: "REGISTER_REQUEST" }
  | { type: "REGISTER_SUCCESS"; payload: AuthUser }
  | { type: "REGISTER_FAILURE"; payload: string }
  | { type: "LOGOUT" }
  | { type: "CLEAR_ERROR" };

// Reducer
function authReducer(state: AuthState, action: AuthAction): AuthState {
  switch (action.type) {
    case "LOGIN_REQUEST":
    case "REGISTER_REQUEST":
      return {
        ...state,
        isLoading: true,
        error: null,
      };
    case "LOGIN_SUCCESS":
    case "REGISTER_SUCCESS":
      return {
        ...state,
        isAuthenticated: true,
        isLoading: false,
        user: action.payload,
        error: null,
      };
    case "LOGIN_FAILURE":
    case "REGISTER_FAILURE":
      return {
        ...state,
        isLoading: false,
        user: null,
        isAuthenticated: false,
        error: action.payload,
      };
    case "LOGOUT":
      return {
        ...state,
        isAuthenticated: false,
        user: null,
        error: null,
      };
    case "CLEAR_ERROR":
      return {
        ...state,
        error: null,
      };
    default:
      return state;
  }
}

// Crear contexto
export type AuthContextType = {
  state: AuthState;
  login: (email: string, password: string) => Promise<void>;
  register: (email: string, password: string) => Promise<void>;
  logout: () => void;
  clearError: () => void;
};

// export const AuthContext = createContext<AuthContextType | undefined>(
//   undefined
// );

// Proveedor del contexto
export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [state, dispatch] = useReducer(authReducer, initialState);

  // Verificar si hay un token almacenado al cargar la aplicación
  useEffect(() => {
    // const initAuth = async () => {
    //   const token = authService.getStoredToken();

    //   if (token) {
    //     try {
    //       const isValid = await authService.verifyToken(token);

    //       if (isValid) {
    //         // Supongamos que podemos extraer el ID y email del token o hacer una llamada a la API
    //         // para obtener los datos del usuario
    //         const userData = await authService.getCurrentUser(token);

    //         dispatch({
    //           type: "LOGIN_SUCCESS",
    //           payload: {
    //             id: userData.id,
    //             email: userData.email,
    //             token: token,
    //           },
    //         });
    //       } else {
    //         authService.removeStoredToken();
    //         dispatch({ type: "LOGOUT" });
    //       }
    //     } catch (error) {
    //       authService.removeStoredToken();
    //       console.error((error as Error).message);

    //       dispatch({ type: "LOGOUT" });
    //     }
    //   } else {
    //     dispatch({ type: "LOGOUT" });
    //   }

    //   // Finalmente, marcamos la carga como completada incluso si no hay token
    //   if (state.isLoading) {
    //     dispatch({ type: "LOGIN_FAILURE", payload: "" });
    //   }
    // };

    const initAuth = async () => {
      try {
        const token = authService.getStoredToken();

        if (token) {
          const userData = await authService.getCurrentUser(token);

          if (userData) {
            dispatch({
              type: "LOGIN_SUCCESS",
              payload: {
                id: userData.id,
                email: userData.email,
                token: token,
              },
            });
          } else {
            authService.removeStoredToken();
            dispatch({ type: "LOGOUT" });
          }
        } else {
          dispatch({ type: "LOGOUT" });
        }
      } catch (error) {
        authService.removeStoredToken();
        console.error((error as Error).message);
        dispatch({ type: "LOGOUT" });
      } finally {
        // Marcar la carga como completada
        dispatch({ type: "LOGIN_FAILURE", payload: "" });
      }
    };

    initAuth();
  }, []);

  // Métodos para interactuar con el estado
  const login = async (email: string, password: string) => {
    dispatch({ type: "LOGIN_REQUEST" });

    try {
      const response = await authService.login({ email, password });

      if (response.success) {
        authService.setStoredToken(response.token);

        dispatch({
          type: "LOGIN_SUCCESS",
          payload: {
            id: response.id,
            email: response.email,
            token: response.token,
          },
        });
      } else {
        dispatch({
          type: "LOGIN_FAILURE",
          payload: response.message || "Error en inicio de sesión",
        });
      }
    } catch (error) {
      dispatch({
        type: "LOGIN_FAILURE",
        payload:
          error instanceof Error ? error.message : "Error en inicio de sesión",
      });
    }
  };

  const register = async (email: string, password: string) => {
    dispatch({ type: "REGISTER_REQUEST" });

    try {
      const response = await authService.register({ email, password });

      if (response.success) {
        authService.setStoredToken(response.token);

        dispatch({
          type: "REGISTER_SUCCESS",
          payload: {
            id: response.id,
            email: response.email,
            token: response.token,
          },
        });
      } else {
        dispatch({
          type: "REGISTER_FAILURE",
          payload: response.message || "Error en registro",
        });
      }
    } catch (error) {
      dispatch({
        type: "REGISTER_FAILURE",
        payload: error instanceof Error ? error.message : "Error en registro",
      });
    }
  };

  const logout = () => {
    authService.removeStoredToken();
    dispatch({ type: "LOGOUT" });
  };

  const clearError = () => {
    dispatch({ type: "CLEAR_ERROR" });
  };

  return (
    <AuthContext.Provider
      value={{ state, login, register, logout, clearError }}
    >
      {children}
    </AuthContext.Provider>
  );
};

// Hook personalizado para usar el contexto
// export const useAuth = () => {
//   const context = useContext(AuthContext);
//   if (context === undefined) {
//     throw new Error("useAuth debe ser usado dentro de un AuthProvider");
//   }
//   return context;
// };
