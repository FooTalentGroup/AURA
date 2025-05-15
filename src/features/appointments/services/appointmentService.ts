import { api } from "../../../core/services/api";
import { Appointment, AppointmentFormData } from "../types/appointment.types";

export const appointmentService = {
  getAppointments: async (token: string): Promise<Appointment[]> => {
    return api.get<Appointment[]>("/appointments", token);
  },

  getAppointmentById: async (
    id: number,
    token: string
  ): Promise<Appointment> => {
    return api.get<Appointment>(`/appointments/${id}`, token);
  },

  createAppointment: async (
    appointmentData: AppointmentFormData,
    token: string
  ): Promise<Appointment> => {
    return api.post<Appointment, AppointmentFormData>(
      "/appointments",
      appointmentData,
      token
    );
  },

  updateAppointment: async (
    id: number,
    appointmentData: AppointmentFormData,
    token: string
  ): Promise<Appointment> => {
    return api.put<Appointment, AppointmentFormData>(
      `/appointments/${id}`,
      appointmentData,
      token
    );
  },

  deleteAppointment: async (id: number, token: string): Promise<void> => {
    return api.delete<void>(`/appointments/${id}`, token);
  },
};
