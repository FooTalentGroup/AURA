/** Representa una escuela en el sistema */
export interface School {
  id: number;
  schoolName: string;
  emailSchool: string;
  phoneSchool: string; // cadenas separadas por comas
}

/** Datos necesarios para crear o actualizar una escuela */
export interface SchoolPayload {
  schoolName: string;
  emailSchool: string;
  phoneSchool: string;
}

/** Respuesta paginada de escuelas */
export interface PaginatedSchools {
  content: School[];
  currentPage: number;
  pageSize: number;
  totalPages: number;
  totalElements: number;
}