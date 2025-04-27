import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../store/authStore";

const PublicRoutes = () => {
  const { state } = useAuth();

  // Si el usuario está autenticado, redirigir al dashboard
  return state.isAuthenticated ? (
    <Navigate to="/dashboard" replace />
  ) : (
    <Outlet />
  );
};

export default PublicRoutes;
