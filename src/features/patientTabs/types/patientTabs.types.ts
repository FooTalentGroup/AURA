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
  name: string;
  dateBirth: string;
  dni: string;
  age: number;
  genre: string;
  socialSecurity: {
    name: string;
    plan: string;
    membershipNumber: string;
  };
}
