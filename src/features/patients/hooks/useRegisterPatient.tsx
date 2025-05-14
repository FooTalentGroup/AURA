import { useState, useCallback } from 'react';
import { patientService } from '../services/patientService';
import { schoolApi } from '../services/schoolService';
import type { PatientPayload, Patient } from '../types/patient.types';

/**
 * Hook para registrar paciente. Maneja opcional school:
 * si payload.schoolName existe, crea primero la escuela,
 * luego registra el paciente con el id retornado.
 */
export function useRegisterPatient() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<Patient | null>(null);

  const register = useCallback(
    async (payload: PatientPayload & { schoolName?: string; emailSchool?: string; phoneSchool?: string }) => {
      setLoading(true);
      setError(null);
      try {
        let schoolId: number | undefined;
        // Si viene escuela opcional, la creamos primero
        if (payload.schoolName) {
          const school = await schoolApi.createSchool({
            schoolName: payload.schoolName,
            emailSchool: payload.emailSchool!, // asumimos no null si schoolName
            phoneSchool: payload.phoneSchool!,
          });
          schoolId = school.id;
        }
        // Armar payload final sin datos de escuela extra
        const finalPayload: PatientPayload = {
          email: payload.email,
          dni: payload.dni,
          name: payload.name,
          lastName: payload.lastName,
          phoneNumber: payload.phoneNumber,
          birthDate: payload.birthDate,
          genre: payload.genre,
          hasInsurance: payload.hasInsurance,
          insuranceName: payload.insuranceName,
          insurancePlan: payload.insurancePlan,
          memberShipNumber: payload.memberShipNumber,
          address: payload.address,
          tutorName: payload.tutorName,
          relationToPatient: payload.relationToPatient,
          professionalId: payload.professionalId,
          schoolId: schoolId ?? payload.schoolId!,
        };
        const patient = await patientService.create(finalPayload);
        setData(patient);
        return patient;
      } catch (e: any) {
        setError(e.response?.message || e.message || 'Error registrando paciente');
        throw e;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  return { register, loading, error, data };
}