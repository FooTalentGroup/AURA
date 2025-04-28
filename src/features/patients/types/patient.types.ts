export interface Patient {
  id: number;
  firstName: string;
  lastName: string;
  dni: string;
  email?: string;
  phone?: string;
  birthDate?: string;
  address?: string;
  createdAt: string;
  updatedAt: string;
}

export interface PatientFormData {
  firstName: string;
  lastName: string;
  dni: string;
  email?: string;
  phone?: string;
  birthDate?: string;
  address?: string;
}
