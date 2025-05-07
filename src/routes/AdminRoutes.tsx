import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import { useContextAuth } from "../features/auth/hooks/useContextAuth";

const AdminRoute: React.FC = () => {
  const { state, isAdmin } = useContextAuth();

  if (state.isLoading) {
    return <div>Cargando autorización…</div>;
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