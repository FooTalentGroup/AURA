export interface Patient {
  id: number;
  name: string;
  lastName: string;
  phoneNumber: string;
  birthDate: string;
  dni: string;
  email: string;
  hasInsurance: boolean;
  insuranceName: string;
  school: string;
  address: string;
  tutorName: string;
  relationToPatient: string;
  professionalId: [];
}

export interface PatientPayload {
  email: string;
  dni: string;
  name: string;
  lastName: string;
  phoneNumber: string;
  birthDate: string;
  hasInsurance: boolean;
  insuranceName: string;
  school: string;
  address: string;
  tutorName: string;
  relationToPatient: string;
  professionalId: number[]}