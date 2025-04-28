import { useContextAuth } from "../../features/auth/hooks/useContextAuth";
import DashboardLayout from "../../layouts/DashboardLayout";

const DashboardPage = () => {
  const { state } = useContextAuth();

  return (
    <DashboardLayout>
      <div className="bg-white shadow rounded-lg p-6">
        <h1 className="text-2xl font-bold mb-4">
          Bienvenido {state.user?.email}
        </h1>

        <div className="bg-blue-50 border-l-4 border-blue-500 p-4 mb-6">
          <p className="text-blue-700">
            Has iniciado sesión exitosamente como{" "}
            <strong>{state.user?.email}</strong>
          </p>
        </div>

        <div className="mb-6">
          <h3 className="text-lg font-semibold mb-2">
            Información de tu cuenta
          </h3>
          <ul className="list-disc pl-5">
            <li className="mb-1">ID de Usuario: {state.user?.id}</li>
            <li className="mb-1">Email: {state.user?.email}</li>
          </ul>
        </div>

        <div>
          <h3 className="text-lg font-semibold mb-2">¿Qué puedes hacer?</h3>
          <p className="mb-4 text-gray-600">
            Desde aquí puedes administrar tu cuenta y acceder a todas las
            funcionalidades de la aplicación.
          </p>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="border rounded p-4 hover:bg-gray-50 cursor-pointer">
              <h4 className="font-medium">Gestionar Perfil</h4>
              <p className="text-sm text-gray-500">
                Actualiza tu información personal
              </p>
            </div>
            <div className="border rounded p-4 hover:bg-gray-50 cursor-pointer">
              <h4 className="font-medium">Configuración</h4>
              <p className="text-sm text-gray-500">
                Ajusta las preferencias de tu cuenta
              </p>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default DashboardPage;
