import { request } from "../../../core/services/api.ts";
import {
  AuthPayload,
  AuthResponseDto,
  CurrentUser,
  RegisterReceptionistPayload,
  RegisterProfessionalPayload,
} from "../types/auth.types";

export const authApi = {
  login: (creds: AuthPayload) =>
    request<AuthResponseDto>("/auth/login", {
      method: "POST",
      body: JSON.stringify(creds),
    }),
    logout: () =>
      request<void>("/auth/logout", {
        method: "POST",
      }),

  registerReceptionist: (data: RegisterReceptionistPayload) =>
    request<AuthResponseDto>("/auth/receptionist/register", {
      method: "POST",
      body: JSON.stringify(data),
    }),

  registerProfessional: (data: RegisterProfessionalPayload) =>
    request<AuthResponseDto>("/auth/professional/register", {
      method: "POST",
      body: JSON.stringify(data),
    }),

    me: () => request<CurrentUser>("/auth/me", { method: "GET" }),
};