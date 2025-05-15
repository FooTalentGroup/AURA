import {CurrentUser } from "../features/auth/types/auth.types";


export interface AuthState {
  user: CurrentUser | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null | undefined;
}

export type AuthAction =
  | { type: "INIT" }
  | { type: "LOGIN_REQUEST" }
  | { type: "LOGIN_SUCCESS"; payload: CurrentUser }
  | { type: "LOGIN_FAILURE"; payload: string }
  | { type: "LOGOUT" }
  | { type: "CLEAR_ERROR" };

export function authReducer(state: AuthState, action: AuthAction): AuthState {
  switch (action.type) {
    case "INIT":
    case "LOGIN_REQUEST":
      return { ...state, isLoading: true, error: null };
    case "LOGIN_SUCCESS":
      return { user: action.payload, isAuthenticated: true, isLoading: false, error: null };
    case "LOGIN_FAILURE":
      return { user: null, isAuthenticated: false, isLoading: false, error: action.payload };
    case "LOGOUT":
      return { user: null, isAuthenticated: false, isLoading: false, error: null };
    case "CLEAR_ERROR":
      return { ...state, error: null };
    default:
      return state;
  }
}