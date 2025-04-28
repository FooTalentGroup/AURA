import { useState, useEffect } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import { appointmentService } from "../../features/appointments/services/appointmentService";
import { Appointment } from "../../features/appointments/types/appointment.types";
import { useContextAuth } from "../../features/auth/hooks/useContextAuth";

const AppointmentsPage = () => {
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { state } = useContextAuth();

  useEffect(() => {
    const fetchAppointments = async () => {
      try {
        if (state.user?.token) {
          const data = await appointmentService.getAppointments(
            state.user.token
          );
          setAppointments(data);
          setError(null);
        }
      } catch (err) {
        setError("Error al cargar las citas");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchAppointments();
  }, [state.user]);

  const formatDate = (dateString: string) => {
    const options: Intl.DateTimeFormatOptions = {
      year: "numeric",
      month: "long",
      day: "numeric",
    };
    return new Date(dateString).toLocaleDateString("es-ES", options);
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "scheduled":
        return "bg-blue-100 text-blue-800";
      case "completed":
        return "bg-green-100 text-green-800";
      case "cancelled":
        return "bg-red-100 text-red-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  const translateStatus = (status: string) => {
    switch (status) {
      case "scheduled":
        return "Programada";
      case "completed":
        return "Completada";
      case "cancelled":
        return "Cancelada";
      default:
        return status;
    }
  };

  return (
    <DashboardLayout>
      <div className="container mx-auto px-4 py-6">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold">Agenda de Citas</h1>
          <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
            Nueva Cita
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
                    Paciente
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Fecha
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Hora
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Duración
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Estado
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Acciones
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {appointments.length > 0 ? (
                  appointments.map((appointment) => (
                    <tr key={appointment.id}>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {appointment.patientName}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {formatDate(appointment.date)}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {appointment.time}
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        {appointment.duration} min
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span
                          className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${getStatusColor(
                            appointment.status
                          )}`}
                        >
                          {translateStatus(appointment.status)}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <button className="text-blue-600 hover:text-blue-900 mr-3">
                          Ver
                        </button>
                        <button className="text-green-600 hover:text-green-900 mr-3">
                          Editar
                        </button>
                        <button className="text-red-600 hover:text-red-900">
                          Cancelar
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td
                      colSpan={6}
                      className="px-6 py-4 text-center text-gray-500"
                    >
                      No hay citas programadas
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

export default AppointmentsPage;
