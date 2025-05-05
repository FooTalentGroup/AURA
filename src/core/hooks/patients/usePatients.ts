import { useState, useEffect, useCallback } from 'react';
import { patientService } from '../../../features/patients/services/patientService';
import type { Patient } from '../../../features/patients/types/patient.types';

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
  const load = useCallback(async (query?: string) => {
    setLoading(true);
    setError(null);

    try {
      let data: Patient[];

      if (!query) {
        // Lista paginada del backend
        data = await patientService.list(page, size);
      } else if (/^\d+$/.test(query)) {
        // Búsqueda parcial por DNI
        // Obtenemos un lote amplio de pacientes y filtramos localmente
        const all = await patientService.list(0, 1000); // ajusta 1000 según tu volumen de datos
        data = all.filter(p => p.dni.includes(query));
      } else {
        // Búsqueda por nombre via API
        data = await patientService.searchByName(query);
      }

      setPatients(data);
    } catch (e: any) {
      setError(e.message || 'Error al cargar pacientes');
      setPatients([]);
    } finally {
      setLoading(false);
    }
  }, [page, size]);

  // Al montar o cambiar página/tamaño, cargamos sin filtro
  useEffect(() => {
    load();
  }, [load]);

  return { patients, loading, error, reload: load };
}
