import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useContextAuth } from "../features/auth/hooks/useContextAuth";

const AdminRoute: React.FC = () => {
  const { state, isAdmin } = useContextAuth();

  // Si todavía estamos validando la sesión…
  if (state.isLoading) {
    return (
      <div className="flex flex-col items-center justify-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
        <p className="mt-4 text-gray-600">Cargando autorización…</p>
      </div>
    );
  }

  // Si no está autenticado, lo mandamos al login
  if (!state.isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  // Si está autenticado pero no es admin, lo mandamos al 404
  if (!isAdmin) {
    return <Navigate to="/404" replace />;
  }

  // Es admin → renderiza las rutas hijas
  return <Outlet />;
};

export default AdminRoute;