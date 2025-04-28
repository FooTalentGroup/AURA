import { api } from "../../../core/services/api";
import { Patient, PatientFormData } from "../types/patient.types";

export const patientService = {
  getPatients: async (token: string): Promise<Patient[]> => {
    return api.get<Patient[]>("/patients", token);
  },

  getPatientById: async (id: number, token: string): Promise<Patient> => {
    return api.get<Patient>(`/patients/${id}`, token);
  },

  createPatient: async (
    patientData: PatientFormData,
    token: string
  ): Promise<Patient> => {
    return api.post<Patient, PatientFormData>("/patients", patientData, token);
  },

  updatePatient: async (
    id: number,
    patientData: PatientFormData,
    token: string
  ): Promise<Patient> => {
    return api.put<Patient, PatientFormData>(
      `/patients/${id}`,
      patientData,
      token
    );
  },

  deletePatient: async (id: number, token: string): Promise<void> => {
    return api.delete<void>(`/patients/${id}`, token);
  },
};
