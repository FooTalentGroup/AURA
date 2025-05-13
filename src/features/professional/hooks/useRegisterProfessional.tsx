import { useState, useCallback } from 'react';
import { authApi } from '../../auth/services/authApi';
import type { RegisterProfessionalPayload } from '../../auth/types/auth.types';
import type { AuthResponseDto } from '../../auth/types/auth.types';

export function useRegisterProfessional() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [data, setData] = useState<AuthResponseDto | null>(null);

  const register = useCallback(
    async (payload: RegisterProfessionalPayload) => {
      setLoading(true);
      setError(null);
      try {
        const response = await authApi.registerProfessional(payload);
        setData(response);
        return response;
      } catch (e: any) {
        setError(e.message || 'Error registrando profesional');
        throw e;
      } finally {
        setLoading(false);
      }
    },
    []
  );

  return { register, loading, error, data };
}