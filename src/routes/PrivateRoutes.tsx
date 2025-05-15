import { Navigate, Outlet } from "react-router-dom";
import { useContextAuth } from "../features/auth/hooks/useContextAuth";

const PrivateRoutes = () => {
  const { state } = useContextAuth();

  // Si el usuario no está autenticado, redirigir al login
  // Si está en proceso de carga, mostrar un indicador de carga
  if (state.isLoading) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
      </div>
    );
  }

  return state.isAuthenticated ? <Outlet /> : <Navigate to="/login" replace />;
};

export default PrivateRoutes;
