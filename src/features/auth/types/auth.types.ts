import { AuthState } from "../../../store/authStore";

export interface AuthContextType {
  state: AuthState;
  login: (email: string, password: string) => Promise<boolean>;
  logout: () => Promise<void>;
  clearError: () => void;
  registerReceptionist: (data: RegisterReceptionistPayload) => Promise<void>;
  registerProfessional: (data: RegisterProfessionalPayload) => Promise<void>;
  isAdmin: boolean;
  isProfessional: boolean;
  isReceptionist: boolean;

}
export interface AuthPayload {
  email: string;
  password: string;
}

export interface AuthResponseDto {
  Id: number;
  email: string;
  message: string;
  success: boolean;
}

export interface CurrentUser {
  id: number;
  username: string;
  email: string;
  roles?: string[];
}


export interface RegisterProfessionalPayload {
  email: string;
  password: string;
  dni: string;
  name: string;
  lastName: string;
  phoneNumber: string;
  country: string;
  photoUrl: string;
  birthDate: string; //  YYYY-MM-DD
  licenseNumber: string;
  specialty: string;
}

export interface RegisterReceptionistPayload {
  email: string;
  password: string;
  dni: string;
  name: string;
  lastName: string;
  phoneNumber: string;
  country: string;
  photoUrl: string;
  birthDate: string; 
}

export interface AuthResponseRegisterDto {
  userId: number;     
  username: string;    
  message: string;     
  status: boolean;     
}

export interface UserResponse {
  id: number;
  email?: string;
  roles?: string[];
  name?: string;
  lastName?: string;
}