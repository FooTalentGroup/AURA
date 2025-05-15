import { api } from "../../../core/services/api";
import { PatientProps } from "../../patientTabs/types/patientTabs.types";
import { Patient, PatientPayload } from "../types/patient.types";

export const patientService = {
  /**
   * Obtiene un listado de pacientes paginado.
   * @param page número de página (0-based)
   * @param size tamaño de página
   */
  list: async (page: number = 0, size: number = 10): Promise<Patient[]> => {
    const paginated = await api.listPatientsPaginated(page, size);
    return paginated.content;
  },

  /** Obtiene un paciente por su ID */
  getById: (id: number): Promise<PatientProps> =>
    api.getPatientById(id),

  /** Crea un nuevo paciente */
  create: (data: PatientPayload): Promise<Patient> =>
    api.createPatient(data),

  /** Actualiza un paciente existente */
  update: (id: number, data: PatientPayload): Promise<Patient> =>
    api.updatePatient(id, data),

  /** Elimina un paciente */
  delete: (id: number): Promise<void> =>
    api.deletePatient(id),

  /** Busca pacientes por DNI */
  searchByDni: (dni: string): Promise<Patient> =>
    api.searchPatientByDni(dni),

  /** Busca pacientes por nombre (fuzzy) */
  searchByName: (name: string): Promise<Patient[]> =>
    api.searchPatientByName(name),
};