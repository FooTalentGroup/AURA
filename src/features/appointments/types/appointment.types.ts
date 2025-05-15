export interface Appointment {
  id: number;
  patientId: number;
  patientName: string;
  date: string;
  time: string;
  duration: number;
  status: "scheduled" | "completed" | "cancelled";
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface AppointmentFormData {
  patientId: number;
  date: string;
  time: string;
  duration: number;
  notes?: string;
}
