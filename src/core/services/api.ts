import {
  Admin,
  AuthResponseRegisterDto,
  RegisterProfessionalPayload,
} from "../../features/auth/types/auth.types.ts";
import { PatientPayload } from "../../features/patients/types/patient.types.ts";
import { Patient } from "../../features/patients/types/patient.types.ts";
import { SchoolPayload } from "../../features/patients/types/school.types.ts";
import {
  AppointmentProps,
  FollowEntriesProps,
  MedicalRecordPatientIdProps,
  PatientDiagnosesProps,
  PatientNotesInfo,
  PatientProps,
  School,
  SchoolsListResponse,
  updatedDiagnosis,
} from "../../features/patientTabs/types/patientTabs.types.ts";
import { Professional } from "../../features/professional/types/Professional.types.ts";
import {
  CurrentUserProps,
  UserUpdateData,
} from "../../features/profile/types/profile.type.ts";

// --- Payload y modelos ---
export interface SuspendRequestDto {
  duration: number;
  unit: "HOURS" | "DAYS" | "WEEKS" | "MONTHS";
}

export interface ReceptionistRequestDto {
  email: string;
  password: string;
  dni: string;
  name: string;
  lastName: string;
  phoneNumber: string;
}

export interface ReceptionistUpdateDto {
  email?: string;
  dni?: string;
  name?: string;
  lastName?: string;
  phoneNumber?: string;
}

export interface Receptionist {
  id: number;
  dni: string;
  name: string;
  lastName: string;
  phoneNumber: string;
  country: string;
  photoUrl: string;
  birthDate: string;
}

export interface MedicalRecord {
  id: number;
  notes: string;
  allergies: string;
  previousConditions: string;
  patientId: number;
}

export interface MedicalRecordPayload {
  patientId: number;
  notes: string;
  allergies: string;
  previousConditions: string;
}

export interface Paginated<T> {
  content: T[];
  currentPage: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;
}

// --- Configuración base ---
const BASE_URL = import.meta.env.VITE_API_BASE_URL;

// Función genérica de fetch con JSON headers y cookies
export async function request<T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> {
  const headers = new Headers({
    "Content-Type": "application/json",
    ...(options.headers as Record<string, string>),
  });

  const response = await fetch(`${BASE_URL}${endpoint}`, {
    mode: "cors",
    credentials: "include",
    ...options,
    headers,
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    const message = errorData.message || response.statusText;
    throw new Error(message);
  }

  if (response.status === 204) {
    return undefined as unknown as T;
  }

  return response.json() as Promise<T>;
}

// --- Endpoints exportados ---
export const api = {
  // --- Autenticación ---
  suspendUser: (userId: number, data: SuspendRequestDto) =>
    request<void>(`/auth/${userId}/suspend`, {
      method: "POST",
      body: JSON.stringify(data),
    }),

  activateUser: (userId: number) =>
    request<void>(`/auth/${userId}/activate`, {
      method: "POST",
    }),

  registerReceptionist: (data: ReceptionistRequestDto) =>
    request<AuthResponseRegisterDto>(`/auth/receptionist/register`, {
      method: "POST",
      body: JSON.stringify(data),
    }),

  registerProfessional: (data: RegisterProfessionalPayload) =>
    request<AuthResponseRegisterDto>(`/auth/professional/register`, {
      method: "POST",
      body: JSON.stringify(data),
    }),
  getProfessionalById: (id: number) =>
    request<Professional>(`/professionals/${id}`, {
      method: "GET",
    }),

  getCurrentUser: (): Promise<CurrentUserProps> =>
    request<CurrentUserProps>(`/auth/me`, { method: "GET" }),

  updateCurrentUser: (
    id: number,
    data: UserUpdateData
  ): Promise<CurrentUserProps> =>
    request<CurrentUserProps>(`/auth/me/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),

  // --- Pacientes ---
  getPatientById: (id: number) =>
    request<PatientProps>(`/patients/${id}`, { method: "GET" }),

  createPatient: (data: PatientPayload) =>
    request<Patient>(`/patients/register`, {
      method: "POST",
      body: JSON.stringify(data),
    }),

  updatePatient: (id: number, data: PatientPayload) =>
    request<Patient>(`/patients/${id}`, {
      method: "PATCH",
      body: JSON.stringify(data),
    }),

  deletePatient: (id: number) =>
    request<void>(`/patients/${id}`, { method: "DELETE" }),

  listPatientsPaginated: (page: number = 0, size: number = 10) =>
    request<Paginated<Patient>>(`/patients?page=${page}&size=${size}`, {
      method: "GET",
    }),

  searchPatientByName: (nombre: string) =>
    request<Patient[]>(
      `/patients/search/name?name=${encodeURIComponent(nombre)}`,
      { method: "GET" }
    ),

  searchPatientByDni: (dni: string) =>
    request<Patient>(`/patients/search/dni?dni=${encodeURIComponent(dni)}`, {
      method: "GET",
    }),

  // --- Historial médico ---
  getMedicalRecordById: (id: number) =>
    request<MedicalRecord>(`/medical-records/${id}`, { method: "GET" }),

  getMedicalRecordByPatientId: (id: number) =>
    request<MedicalRecordPatientIdProps>(`/medical-records/patient/${id}`, {
      method: "GET",
    }),

  createMedicalRecord: (data: MedicalRecordPayload) =>
    request<MedicalRecord>(`/medical-records/create`, {
      method: "POST",
      body: JSON.stringify(data),
    }),

  updateMedicalRecord: (id: number, data: MedicalRecordPayload) =>
    request<void>(`/medical-records/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),

  deleteMedicalRecord: (id: number) =>
    request<void>(`/medical-records/${id}`, { method: "DELETE" }),

  listMedicalRecordsPaginated: (page: number = 0, size: number = 10) =>
    request<Paginated<MedicalRecord>>(
      `/medical-records?page=${page}&size=${size}`,
      { method: "GET" }
    ),

  getMedicalRecordFilter: () =>
    request<AppointmentProps[]>(`/medical-records/filter`, { method: "GET" }),

  // --- Recepcionistas ---
  getReceptionistById: (id: number) =>
    request<Receptionist>(`/receptionist/${id}`, { method: "GET" }),

  updateReceptionist: (id: number, data: ReceptionistUpdateDto) =>
    request<Receptionist>(`/receptionist/${id}`, {
      method: "PATCH",
      body: JSON.stringify(data),
    }),

  deleteReceptionist: (id: number) =>
    request<void>(`/receptionist/${id}`, { method: "DELETE" }),

  listReceptionistsPaginated: (page: number = 0, size: number = 10) =>
    request<Paginated<Receptionist>>(
      `/receptionist?page=${page}&size=${size}`,
      { method: "GET" }
    ),

  // --- Usuarios ---
  getAdmin: () => request<Admin>(`/user/all_admin`, { method: "GET" }),

  // --- Profesionales ---

  listProfessionalsPaginated: (page: number = 0, size: number = 10) =>
    request<Paginated<Professional>>(
      `/professionals?page=${page}&size=${size}`,
      { method: "GET" }
    ),

  searchProfessionals: (keyword: string) =>
    request<Professional[]>(
      `/professionals/search?keyword=${encodeURIComponent(keyword)}`,
      { method: "GET" }
    ),

  getPatientsByProfessional: (professionalId: number) =>
    request<Patient[]>(`/professionals/${professionalId}/patients`, {
      method: "GET",
    }),

  // --- Escuelas ---
  listSchoolsPaginated: (page: number = 0, size: number = 10) =>
    request<SchoolsListResponse>(`/schools/schools?page=${page}&size=${size}`, {
      method: "GET",
    }),
  createSchool: (data: SchoolPayload) =>
    request<School>(`/schools`, {
      method: "POST",
      body: JSON.stringify(data),
    }),

  updateSchool: (id: number, data: SchoolPayload) =>
    request<School>(`/schools/schools/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),

  deleteSchool: (id: number) =>
    request<void>(`/schools/${id}`, { method: "DELETE" }),

  // --- Entradas de seguimiento ---
  getFollowEntriesById: (id: number) =>
    request<FollowEntriesProps>(`/follow-up-entries/${id}`, { method: "GET" }),

  // --- Diagnóstico de pacientes
  getDiagnosesById: (id: number) =>
    request<PatientDiagnosesProps>(`/diagnoses/${id}`, { method: "GET" }),
  createDiagnosis: (data: PatientDiagnosesProps) =>
    request<PatientDiagnosesProps>(`/diagnoses/create`, {
      method: "POST",
      body: JSON.stringify(data),
    }),
  updateDiagnosis: (id: number, data: updatedDiagnosis) =>
    request<PatientDiagnosesProps>(`/diagnoses/update/${id}`, {
      method: "PATCH",
      body: JSON.stringify(data),
    }),

  // --- Antecedentes Médicos
  getMedicalBackgroundsById: (id: number) =>
    request<PatientNotesInfo>(`/medical-backgrounds/patient/${id}`, {
      method: "GET",
    }),
  createFollowUpEntry: (data: {
    medicalRecordId: number;
    observations: string;
    interventions: string;
    nextSessionInstructions: string;
  }) =>
    request<FollowEntriesProps>("/follow-up-entries/create", {
      method: "POST",
      body: JSON.stringify(data),
    }),
};
