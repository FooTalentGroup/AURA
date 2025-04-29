// --- Payload y modelos ---
export interface RegisterProfessionalPayload {
    email: string;
    password: string;
    dni: string;
    name: string;
    lastName: string;
    phoneNumber: string;
    country: string;
    photoUrl: string;
    birthDate: string; // ISO YYYY-MM-DD
    licenseNumber: string;
    specialty: string;
  }
  
  export interface AuthPayload {
    email: string;
    password: string;
  }
  
  export interface AuthResponseDto {
    userId: number;
    username: string;
    message: string;
    token: string;
    status: boolean;
  }
  
  export interface Patient {
    id: number;
    name: string;
    lastName: string;
    phoneNumber: string;
    country: string;
    photoUrl: string;
    birthDate: string;
    dni: string;
    email: string;
    hasInsurance: boolean;
    insuranceName: string;
    school: string;
    paymentType: string;
  }
  
  export interface PatientPayload {
    email: string;
    dni: string;
    name: string;
    lastName: string;
    phoneNumber: string;
    country: string;
    photoUrl: string;
    birthDate: string;
    hasInsurance: boolean;
    insuranceName: string;
    school: string;
    paymentType: string;
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
  
  export interface Professional {
    id: number;
    dni: string;
    name: string;
    lastName: string;
    phoneNumber: string;
    country: string;
    photoUrl: string;
    birthDate: string;
    licenseNumber: string;
    specialty: string;
  }
  
  export interface ProfessionalPayload {
    email?: string;
    password?: string;
    dni: string;
    name: string;
    lastName: string;
    phoneNumber: string;
    country: string;
    photoUrl: string;
    birthDate: string;
    licenseNumber: string;
    specialty: string;
  }
  
  //sort type
  export interface Sort {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
  }
  //pageable type
  export interface Pageable {
    sort: Sort;
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    unpaged: boolean;
  }
  
  //ver si se va a usar todo, posiblemente datos modificados como sort
  export interface ProfessionalsPageDto {
    totalElements: number;
    totalPages: number;
    size: number;
    content: Professional[];
    number: number;
    sort: Sort;
    numberOfElements: number;
    pageable: Pageable;
    first: boolean;
    last: boolean;
    empty: boolean;
  }
  
  
  
  // --- Configuración base ---
  const BASE_URL = import.meta.env.VITE_API_BASE_URL;
  
  // Función genérica de fetch con JSON headers y cookies
  async function request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const headers = new Headers({
      "Content-Type": "application/json",
      ...(options.headers as Record<string, string>),
    });
  
    const response = await fetch(`${BASE_URL}${endpoint}`, {
      ...options,
      headers,
      credentials: "include", // incluir cookies de sesión
    });
  
    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      const message = errorData.message || response.statusText;
      throw new Error(message);
    }
  
    // Si no hay cuerpo, devolvemos undefined
    if (response.status === 204) {
      return undefined as T;
    }
  
    return response.json() as Promise<T>;
  }
  
  // --- Endpoints exportados ---
  export const api = {
    // --- Autenticación ---
    registerProfessional: (data: RegisterProfessionalPayload) =>
      request<AuthResponseDto>(`/auth/profesional/registrarse`, {
        method: "POST",
        body: JSON.stringify(data),
      }),
  
    login: (data: AuthPayload) =>
      request<AuthResponseDto>(`/auth/login`, {
        method: "POST",
        body: JSON.stringify(data),
      }),
  
    logout: () =>
      request<void>(`/auth/logout`, { method: "POST" }),
  
    // --- Pacientes ---
    getPatientById: (id: number) =>
      request<Patient>(`/pacientes/${id}`, { method: "GET" }),
  
    createPatient: (data: PatientPayload) =>
      request<Patient>(`/pacientes/registro`, {
        method: "POST",
        body: JSON.stringify(data),
      }),
  
    updatePatient: (id: number, data: PatientPayload) =>
      request<Patient>(`/pacientes/${id}`, {
        method: "PUT",
        body: JSON.stringify(data),
      }),
  
    deletePatient: (id: number) =>
      request<void>(`/pacientes/${id}`, { method: "DELETE" }),
  
    listPatients: (from: number = 0, to: number = 9) =>
      request<Patient[]>(`/pacientes?from=${from}&to=${to}`, {
        method: "GET",
      }),
  
    searchPatientByDni: (dni: string) =>
      request<Patient>(`/pacientes/buscar/dni?dni=${encodeURIComponent(dni)}`, {
        method: "GET",
      }),
  
    // --- Historial médico ---
    getMedicalRecordById: (id: number) =>
      request<MedicalRecord>(`/registros-medicos/${id}`, { method: "GET" }),
  
    createMedicalRecord: (data: MedicalRecordPayload) =>
      request<MedicalRecord>(`/registros-medicos/crear`, {
        method: "POST",
        body: JSON.stringify(data),
      }),
  
    updateMedicalRecord: (id: number, data: MedicalRecordPayload) =>
      request<void>(`/registros-medicos/${id}`, {
        method: "PUT",
        body: JSON.stringify(data),
      }),
  
    deleteMedicalRecord: (id: number) =>
      request<void>(`/registros-medicos/${id}`, { method: "DELETE" }),
  
    listMedicalRecords: () =>
      request<MedicalRecord[]>(`/registros-medicos`, { method: "GET" }),
  
    // --- Profesionales ---
    getProfessionalById: (id: number) =>
      request<Professional>(`/profesionales/${id}`, { method: "GET" }),
  
    updateProfessional: (id: number, data: ProfessionalPayload) =>
      request<Professional>(`/profesionales/${id}`, {
        method: "PUT",
        body: JSON.stringify(data),
      }),
  
    deleteProfessional: (id: number) =>
      request<void>(`/profesionales/${id}`, { method: "DELETE" }),
  
    listProfessionals: () =>
      request<Professional[]>(`/profesionales`, { method: "GET" }),
  
    searchProfessionals: (keyword: string) =>
      request<Professional[]>(
        `/profesionales/busqueda?keyword=${encodeURIComponent(keyword)}`,
        { method: "GET" }
      ),
  
    getProfessionalsPaginated: (
      page: number = 0,
      size: number = 10
    ) =>
      request<ProfessionalsPageDto>(
        `/profesionales/pagina?page=${page}&size=${size}`,
        { method: "GET" }
      ),
  };
  