export interface LoginCredentials {
  email: string;
  password: string;
}

export interface RegisterCredentials extends LoginCredentials {
  confirmPassword?: string;
}

export interface AuthResponse {
  id: number;
  email: string;
  message: string;
  token: string;
  success: boolean;
}

export interface AuthUser {
  id: number;
  email: string;
  token: string;
}

export interface AuthState {
  user: AuthUser | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
}
