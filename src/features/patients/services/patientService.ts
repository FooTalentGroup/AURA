import { api } from "../../../core/services/api";
import { Patient, PatientPayload  } from "../types/patient.types";

/**
 * Servicio unificado para gestión de pacientes,
 * usando el cliente genérico `api` basado en fetch + request<T>.
 */
export const patientService = {
  /** Obtiene un listado de pacientes con paginación opcional */
  list: async (from: number = 0, to: number = 9): Promise<Patient[]> => {
    return api.listPatients(from, to);
  },

  /** Obtiene un paciente por su ID */
  getById: async (id: number): Promise<Patient> => {
    return api.getPatientById(id);
  },

  /** Crea un nuevo paciente */
  create: async (data: PatientPayload): Promise<Patient> => {
    return api.createPatient(data);
  },

  /** Actualiza un paciente existente */
  update: async (id: number, data: PatientPayload): Promise<Patient> => {
    return api.updatePatient(id, data);
  },

  /** Elimina un paciente */
  delete: async (id: number): Promise<void> => {
    return api.deletePatient(id);
  },

  /** Busca un paciente por DNI */
  searchByDni: async (dni: string): Promise<Patient> => {
    return api.searchPatientByDni(dni);
  }
};
