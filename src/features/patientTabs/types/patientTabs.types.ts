export interface School {
  id: number;
  schoolName: string;
  emailSchool: string;
  phoneSchool: string;
}

export interface SchoolsListResponse {
  content: School[];
  currentPage: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;
}

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
  memberShipNumber: string;
  address: string;
  tutorName: string;
  relationToPatient: string;
  professionalIds: number[];
  schoolId: number;
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

export interface MedicalRecordPatientIdProps {
  id: number;
  createdAt: string;
  updatedAt: string;
  patientId: number;
  professionalId: number;
  diagnosisIds: number[];
  followUpIds: number[];
}

export interface FollowEntriesProps {
  id: number;
  observations: string;
  interventions: string;
  nextSessionInstructions: string;
  createdAt: string;
  updatedAt: string;
  professionalId: number;
  medicalRecordId: number;
}

export interface PatientDiagnosesProps {
  id: number;
  date: string;
  title: string;
  details: string;
  idProfessional: number;
  medicalRecordId: number;
}

export interface AppointmentProps {
  specialty: string;
  professionalName: string;
  createdAt: string;
}

export interface AppointmentTableProps {
  appointments?: AppointmentProps[];
}

export interface PatientNotesInfo {
  id: number;
  patientId: number;
  schoolReports: string;
  allergies: string[];
  disabilities: string[];
  createdAt: string;
  updatedAt: string;
}

export interface TreatmentNotesProps {
  patientNotesInfo: PatientNotesInfo;
}

export interface TabButtonProps {
  label: string;
  isActive: boolean;
  onClick: () => void;
}

export interface PatientTabsProps {
  patient?: PatientProps;
}

export interface ContactTabProps {
  patient?: PatientProps;
  school?: SchoolProps;
}

export interface DiagnosticTabProps {
  diagnoses?: DiagnosesProps;
}

export interface MedicalBackgroundTabProps {
  medicalBackgrounds: PatientNotesInfo;
}

export interface ClinicalHistoryTabProps {
  medicalFilters?: AppointmentProps[];
  followEntries?: FollowEntriesProps;
}

export const tabs: TabItem[] = [
  { id: "paciente", label: "Paciente" },
  { id: "contacto", label: "Contacto" },
  { id: "diagnostico", label: "Diagnóstico" },
  { id: "historial", label: "Historial clínico" },
  { id: "antecedentes", label: "Antecedentes" },
];
