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
  professionalIds: [];
}

export interface PatientPayload {
  email: string;
  dni: string;
  name: string;
  lastName: string;
  phoneNumber: string;
  birthDate: string;
  genre: string;
  hasInsurance: boolean;
  insuranceName: string;
  insurancePlan: string;
  memberShipNumber: string;
  address: string;
  tutorName: string;
  relationToPatient: string;
  professionalIds: number[]
    schoolId: number;
}