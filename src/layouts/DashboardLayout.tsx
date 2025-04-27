// src/layouts/DashboardLayout.tsx

import { ReactNode } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../store/authStore";

interface DashboardLayoutProps {
  children: ReactNode;
}

const DashboardLayout = ({ children }: DashboardLayoutProps) => {
  const { logout, state } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <header className="bg-white shadow-sm">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <h1 className="text-xl font-semibold text-gray-800">
            Panel de Control
          </h1>

          <div className="flex items-center">
            {state.user && (
              <span className="text-gray-600 mr-4">{state.user.email}</span>
            )}
            <button
              onClick={handleLogout}
              className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded"
            >
              Cerrar Sesión
            </button>
          </div>
        </div>
      </header>

      <div className="flex flex-grow">
        <aside className="w-64 bg-white shadow-md">
          <nav className="p-4">
            <ul>
              <li className="mb-2">
                <a
                  href="/dashboard"
                  className="block p-2 rounded hover:bg-gray-100 text-gray-800"
                >
                  Inicio
                </a>
              </li>
              <li className="mb-2">
                <a
                  href="/dashboard/profile"
                  className="block p-2 rounded hover:bg-gray-100 text-gray-800"
                >
                  Perfil
                </a>
              </li>
              <li className="mb-2">
                <a
                  href="/dashboard/settings"
                  className="block p-2 rounded hover:bg-gray-100 text-gray-800"
                >
                  Configuración
                </a>
              </li>
            </ul>
          </nav>
        </aside>

        <main className="flex-grow p-6">{children}</main>
      </div>

      <footer className="bg-white py-4">
        <div className="container mx-auto px-4 text-center text-gray-500 text-sm">
          &copy; {new Date().getFullYear()} Aura. Todos los derechos reservados.
        </div>
      </footer>
    </div>
  );
};

export default DashboardLayout;
