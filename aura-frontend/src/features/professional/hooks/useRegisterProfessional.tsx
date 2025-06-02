import { useState, useCallback } from "react";
import { authApi } from "../../auth/services/authApi";
import type { RegisterProfessionalPayload } from "../../auth/types/auth.types";
import type { AuthResponseDto } from "../../auth/types/auth.types";

export function useRegisterProfessional() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<AuthResponseDto | null>(null);

  const register = useCallback(async (payload: RegisterProfessionalPayload) => {
    setLoading(true);
    setError(null);
    try {
      const response = await authApi.registerProfessional(payload);
      setData(response);
      return response;
    } catch (e: unknown) {
      if (e instanceof Error) {
        setError(e.message || "Error registrando profesional");
        throw e;
      } else {
        setError("Error desconocido");
      }
    } finally {
      setLoading(false);
    }
  }, []);

  return { register, loading, error, data };
}
