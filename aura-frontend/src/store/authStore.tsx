import {CurrentUser } from "../features/auth/types/auth.types";


export interface AuthState {
  user: CurrentUser | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null | undefined;
}

export type AuthAction =
  | { type: "INIT" }
  | { type: "INIT_FINISHED" }
  | { type: "LOGIN_REQUEST" }
  | { type: "LOGIN_SUCCESS"; payload: CurrentUser }
  | { type: "LOGIN_FAILURE"; payload: string }
  | { type: "LOGOUT" }
  | { type: "CLEAR_ERROR" };

export function authReducer(state: AuthState, action: AuthAction): AuthState {
  switch (action.type) {
    case "INIT":
      return { ...state, isLoading: true };
    case "INIT_FINISHED":
      return { ...state, isLoading: false };
    case "LOGIN_REQUEST":
      return { ...state, isLoading: true, error: null };
    case "LOGIN_SUCCESS":
      return { ...state, user: action.payload, isAuthenticated: true, isLoading: false };
    case "LOGIN_FAILURE":
      return { ...state, error: action.payload, isLoading: false };
    case "LOGOUT":
      return { user: null, isAuthenticated: false, isLoading: false, error: null };
    case "CLEAR_ERROR":
      return { ...state, error: null };
    default:
      return state;
  }
}