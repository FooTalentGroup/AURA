import { useState, useCallback } from "react";
import { authApi } from "../../auth/services/authApi";
import type {
  RegisterReceptionistPayload,
  AuthResponseDto,
} from "../../auth/types/auth.types";

export function useRegisterReceptionist() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<AuthResponseDto | null>(null);

  const register = useCallback(async (payload: RegisterReceptionistPayload) => {
    setLoading(true);
    setError(null);
    try {
      const response = await authApi.registerReceptionist(payload);
      setData(response);
      return response;
    } catch (e: unknown) {
      if (e instanceof Error) {
        setError(e.message || "Error registrando recepcionista");
        throw e;
      } else {
        setError("Error desconocido registrando recepcionista");
        throw new Error("Error desconocido");
      }
    } finally {
      setLoading(false);
    }
  }, []);

  return { register, loading, error, data };
}
