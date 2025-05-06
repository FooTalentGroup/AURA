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
  nombre: string;
  fechaNacimiento: string;
  dni: string;
  edad: number;
  sexo: string;
  obraSocial: {
    nombre: string;
    plan: string;
    numeroAfiliado: string;
  };
}
