import { useState, useEffect } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import { patientService } from "../../features/patients/services/patientService";
import { Patient } from "../../features/patients/types/patient.types";
import { useContextAuth } from "../../features/auth/hooks/useContextAuth";

const PatientsPage = () => {
  const [patients, setPatients] = useState<Patient[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { state } = useContextAuth();

  useEffect(() => {
    const fetchPatients = async () => {
      try {
        if (state.user?.token) {
          const data = await patientService.getPatients(state.user.token);
          setPatients(data);
          setError(null);
        }
      } catch (err) {
        setError("Error al cargar los pacientes");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchPatients();
  }, [state.user]);

  return (
    <DashboardLayout>
      <div className="container mx-auto px-4 py-6">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold">Pacientes</h1>
          <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
            Nuevo Paciente
          </button>
        </div>

        {loading ? (
          <div className="flex justify-center">
            <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
          </div>
        ) : error ? (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
            {error}
          </div>
        ) : (
          <div className="bg-white shadow-md rounded-lg overflow-hidden">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Nombre
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    DNI
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Email
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Teléfono
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Acciones
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {patients.length > 0 ? (
                  patients.map((patient) => (
                    <tr key={patient.id}>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {patient.firstName} {patient.lastName}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {patient.dni}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {patient.email || "-"}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {patient.phone || "-"}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <button className="text-blue-600 hover:text-blue-900 mr-3">
                          Ver
                        </button>
                        <button className="text-green-600 hover:text-green-900 mr-3">
                          Editar
                        </button>
                        <button className="text-red-600 hover:text-red-900">
                          Eliminar
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td
                      colSpan={5}
                      className="px-6 py-4 text-center text-gray-500"
                    >
                      No hay pacientes registrados
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </DashboardLayout>
  );
};

export default PatientsPage;
