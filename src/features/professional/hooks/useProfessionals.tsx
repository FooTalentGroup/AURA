import { useState, useEffect, useCallback } from "react";
import { professionalsService } from "../services/ProfessionalService";
import { Professional } from "../types/Professional.types";

export const useProfessionals = (
  page: number,
  size: number,
  searchTerm: string
) => {
  const [professionals, setProfessionals] = useState<Professional[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchProfessionals = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      if (searchTerm.trim()) {
        //  Ejecutamos búsqueda por especialidad en el endpoint especializado
        const bySpecialty = await professionalsService.search(
          searchTerm.trim()
        );

        //  Para buscar por nombre/apellido, obtenemos la página completa (o un size alto)
        const pageRes = await professionalsService.list(page, size);
        const byName = pageRes.content.filter((p) => {
          const q = searchTerm.trim().toLowerCase();
          return (
            p.name.toLowerCase().includes(q) ||
            p.lastName.toLowerCase().includes(q)
          );
        });

        //  Unimos ambos arrays, eliminando duplicados
        const map = new Map<number, Professional>();
        [...bySpecialty, ...byName].forEach((p) => map.set(p.id, p));
        setProfessionals(Array.from(map.values()));
      } else {
        // Sin búsqueda, list paginada normal
        const res = await professionalsService.list(page, size);
        setProfessionals(res.content);
      }
    } catch (err) {
      setError((err as Error).message);
    } finally {
      setLoading(false);
    }
  }, [page, size, searchTerm]);

  useEffect(() => {
    fetchProfessionals();
  }, [fetchProfessionals]);

  return {
    professionals,
    loading,
    error,
    reload: fetchProfessionals,
  };
};