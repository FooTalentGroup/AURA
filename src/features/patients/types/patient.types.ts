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