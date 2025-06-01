import { useState, useCallback } from "react";
import { patientService } from "../services/patientService";
import { schoolService } from "../services/shoolService";
import type { PatientPayload, Patient } from "../types/patient.types";

export function useRegisterPatient() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<Patient | null>(null);

  const register = useCallback(
    async (
      payload: PatientPayload & {
        schoolName?: string;
        emailSchool?: string;
        phoneSchool?: string;
      }
    ) => {
      setLoading(true);
      setError(null);
      try {
        let schoolId: number | undefined;

        if (payload.schoolName) {
          const school = await schoolService.create({
            schoolName: payload.schoolName,
            emailSchool: payload.emailSchool!,
            phoneSchool: payload.phoneSchool!,
          });
          schoolId = school.id;
        }

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
          professionalIds: payload.professionalIds,
          schoolId: schoolId ?? payload.schoolId!,
        };
        const patient = await patientService.create(finalPayload);
        setData(patient);
        return patient;
      } catch (e: unknown) {
        if (e instanceof Error) {
          setError(e.message || "Error registrando paciente");
          throw e;
        } else {
          setError("Error desconocido al registrar paciente");
          throw new Error("Error desconocido");
        }
      } finally {
        setLoading(false);
      }
    },
    []
  );

  return { register, loading, error, data };
}
