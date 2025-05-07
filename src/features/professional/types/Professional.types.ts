export interface Professional {
    id: number;
    dni: string;
    name: string;
    lastName: string;
    phoneNumber: string;
    country: string;
    photoUrl: string;
    birthDate: string;
    licenseNumber: string;
    specialty: string;
    patientIds?: number[];
    email: string;
  }
  
  export interface ProfessionalPayload {
    email: string;
    password: string;
    dni: string;
    name: string;
    lastName: string;
    phoneNumber: string;
    licenseNumber: string;
    specialty: string;
  }
  