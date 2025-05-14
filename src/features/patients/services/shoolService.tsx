import { api } from "../../../core/services/api";
import {
  School,
  SchoolPayload,
} from "../types/school.types";

export const schoolService = {
  /**
   * Obtiene un listado de escuelas (solo el array de content)
   */
  list: async (page = 0, size = 10): Promise<School[]> => {
    const { content } = await api.listSchoolsPaginated(page, size);
    return content;
  },

  /** Crea una nueva escuela */
  create: (data: SchoolPayload): Promise<School> =>
    api.createSchool(data),

  /** Actualiza una escuela existente */
  update: (id: number, data: SchoolPayload): Promise<School> =>
    api.updateSchool(id, data),

  /** Elimina una escuela */
  delete: (id: number): Promise<void> =>
    api.deleteSchool(id),
};