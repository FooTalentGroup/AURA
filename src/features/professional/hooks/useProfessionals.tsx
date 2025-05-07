import { useState, useEffect, useCallback } from "react";
import { professionalsService } from "../services/ProfessionalService";
import { Professional } from "../types/Professional.types";

export const useProfessionals = (page: number, size: number) => {
  const [professionals, setProfessionals] = useState<Professional[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchProfessionals = useCallback(
    async (search = "") => {
      setLoading(true);
      setError(null);
      try {
        const res = await professionalsService.list(page, size, search);
        setProfessionals(res.content);
      } catch (err) {
        setError((err as Error).message);
      } finally {
        setLoading(false);
      }
    },
    [page, size]
  );

  useEffect(() => {
    fetchProfessionals();
  }, [fetchProfessionals]);

  return { professionals, loading, error, reload: fetchProfessionals };
};