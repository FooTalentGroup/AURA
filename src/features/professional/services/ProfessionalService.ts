// src/core/services/professionalsService.ts
import { request } from "../../../core/services/api";
import { Paginated } from "../../../core/services/api";               // si tu api exporta este tipo genérico
import { Professional, ProfessionalPayload  } from "../types/Professional.types";

export const professionalsService = {
  /** Listar profesionales paginados */
  list: (page = 0, size = 10, search = "") =>
    request<Paginated<Professional>>(
      `/professionals?page=${page}&size=${size}${search ? `&keyword=${encodeURIComponent(search)}` : ""}`,
      { method: "GET" }
    ),

  /** Buscar profesionales con keyword */
  search: (keyword: string) =>
    request<Professional[]>(
      `/professionals/search?keyword=${encodeURIComponent(keyword)}`,
      { method: "GET" }
    ),

  /** Obtener un profesional por ID */
  getById: (id: number) =>
    request<Professional>(`/professionals/${id}`, { method: "GET" }),

  /** Crear o actualizar profesional */
  update: (id: number, data: ProfessionalPayload) =>
    request<Professional>(`/professionals/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),

  /** Eliminar profesional */
  delete: (id: number) =>
    request<void>(`/professionals/${id}`, { method: "DELETE" }),

  /** Obtener pacientes de un profesional */
  getPatients: (professionalId: number) =>
    request<Professional[]>(
      `/professionals/${professionalId}/patients`,
      { method: "GET" }
    ),
     
};