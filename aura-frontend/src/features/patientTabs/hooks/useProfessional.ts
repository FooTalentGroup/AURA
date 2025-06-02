import { useEffect, useState } from "react";
import { api } from "../../../core/services/api";
import { Professional } from "../../professional/types/Professional.types";

export function useProfessionalData() {
  const [professional, setProfessional] = useState<Professional | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchProfessionalData = async () => {
      try {
        const user = await api.getCurrentUser();
        const prof = await api.getProfessionalById(user.id);
        setProfessional(prof);
      } catch (err: unknown) {
        if (err instanceof Error) {
          setError(err.message || "Error al obtener datos del profesional");
        } else {
          setError("Error desconocido");
        }
      } finally {
        setLoading(false);
      }
    };

    fetchProfessionalData();
  }, []);

  return { professional, loading, error };
}
