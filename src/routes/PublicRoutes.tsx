import { Navigate, Outlet } from "react-router-dom";
import { useContextAuth } from "../features/auth/hooks/useContextAuth";

const PublicRoutes: React.FC = () => {
  const { state } = useContextAuth();

  // 1) Mientras valida la sesión, no muestres nada
  if (state.isLoading) {
    return null;
  }

  // 2) Si ya está autenticado, redirige al dashboard
  if (state.isAuthenticated) {
    return <Navigate to="/dashboard" replace />;
  }

  // 3) Si no, muestra el login/register
  return <Outlet />;
};

export default PublicRoutes;