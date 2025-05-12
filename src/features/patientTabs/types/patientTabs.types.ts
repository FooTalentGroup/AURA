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

export interface PatientProps {
  id: number;
  name: string;
  lastName: string;
  phoneNumber: string;
  birthDate: string;
  dni: string;
  email: string;
  age: number;
  genre: string;
  hasInsurance: boolean;
  insuranceName: string;
  insurancePlan: string;
  memberShipNumer: string;
  address: string;
  tutorName: string;
  relationToPatient: string;
  professionalIds: number[];
  schoolId: number;

  // Faltantes
  // schoolName: string;
  // schoolDirector: string; //sacar

  //endpoints diferentes
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

export interface SchoolProps {
  id: number;
  schoolName: string;
  emailSchool: string;
  phoneSchool: string;
}

export interface DiagnosesProps {
  id: number;
  date: string;
  title: string;
  details: string;
  idProfessional: number;
  medicalRecordId: number;
}
