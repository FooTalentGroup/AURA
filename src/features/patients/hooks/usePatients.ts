import { useState, useEffect, useCallback } from "react";
import { patientService } from "../services/patientService";
import type { Patient } from "../types/patient.types";

/**
 * Hook para manejar la lista de pacientes con búsqueda por nombre o DNI (incluye búsqueda parcial).
 * @param page Página inicial (default 0)
 * @param size Tamaño de página (default 20)
 */
export function usePatients(page = 0, size = 20) {
  const [patients, setPatients] = useState<Patient[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  /**
   * Carga la lista de pacientes:
   * - Sin query: lista paginada
   * - Query sólo dígitos: búsqueda parcial de DNI (filtrado local)
   * - Otro texto: busca por nombre (API)
   */
  const load = useCallback(
    async (query?: string, patientsOverride?: Patient[]) => {
      setLoading(true);
      setError(null);

      try {
        if (patientsOverride) {
          setPatients(patientsOverride);
          return;
        }

        let data: Patient[];

        if (!query) {
          data = await patientService.list(page, size);
        } else if (/^\d+$/.test(query)) {
          const all = await patientService.list(0, 1000);
          data = all.filter((p) => p.dni.includes(query));
        } else {
          data = await patientService.searchByName(query);
        }

        setPatients(data);
      } catch (e: unknown) {
        if (e instanceof Error) {
          setError(e.message || "Error al cargar pacientes");
          setPatients([]);
        } else {
          setError("Error desconocido");
          setPatients([]);
        }
      } finally {
        setLoading(false);
      }
    },
    [page, size]
  );

  // Al montar o cambiar página/tamaño, cargamos sin filtro
  useEffect(() => {
    load();
  }, [load]);

  return { patients, loading, error, reload: load };
}
