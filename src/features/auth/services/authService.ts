import { api } from "../../../core/services/api";
import { AuthPayload, AuthResponseDto, RegisterProfessionalPayload } from "../types/auth.types";


export const authService = {
  login: async (credentials: AuthPayload): Promise<AuthResponseDto> => {
    return api.login(credentials); // la cookie se guarda por el backend
  },

  register: async (data: RegisterProfessionalPayload): Promise<AuthResponseDto> => {
    return api.registerProfessional(data);
  },

  logout: async (): Promise<void> => {
    return api.logout();
  },
};