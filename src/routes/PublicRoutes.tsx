// src/features/auth/routes/PublicRoutes.tsx
import React from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useContextAuth } from '../features/auth/hooks/useContextAuth';

const PublicRoutes: React.FC = () => {
  const { state } = useContextAuth();
  const location = useLocation();

  const publicPaths = ['/login', '/register', '/forgot-password'];

  // 1) Si es ruta pública, siempre mostramos el Outlet (login/register)
  if (publicPaths.includes(location.pathname)) {
    return <Outlet />;
  }

  // 2) Si es ruta privada y aún cargando la sesión, no renderizamos nada
  if (state.isLoading) {
    return null;
  }

  // 3) Si ya está autenticado, redirigimos al dashboard
  if (state.isAuthenticated) {
    return <Navigate to="/patients" replace />;
  }

  // 4) Si ruta privada y no autenticado, mostramos login (o quizá otra ruta)
  return <Outlet />;
};

export default PublicRoutes;