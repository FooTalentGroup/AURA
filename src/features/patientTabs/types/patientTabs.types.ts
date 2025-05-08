export type TabId =
  | "paciente"
  | "contacto"
  | "diagnostico"
  | "historial"
  | "antecedentes";

export interface TabItem {
  id: TabId;
  label: string;
}

export interface PatientData {
  id: number;
  name: string;
  lastName: string;
  birthDate: string;
  dni: string;
  age: number;
  genre: string;
  insuranceName: string;
  insurancePlan: string;
  membershipNumber: string;
}

export interface PatientDB {
  id: number;
  name: string;
  lastName: string;
  phoneNumber: string;
  birthDate: string;
  dni: string;
  email: string;
  hasInsurance: boolean;
  insuranceName: string;
  address: string;
  tutorName: string;
  relationToPatient: string;
  professionalIds: number[];
  level: string;
  shift: string;
  schoolId: number;
  // age: number; // Faltantes
  // genre: string;
  // insurancePlan: string;
  // membershipNumber: string;
  // schoolName: string;
  // schoolDirector: string;
  // diagnostic: {
  //   title: string;
  //   paragraphs: string[];
  //   treatmentPlan: {
  //     title: string;
  //     items: string[];
  //   };
  // };
  // clinicalHistory: {
  //   observations: string;
  //   interventions: string[];
  //   indications: string[];
  // };
}
