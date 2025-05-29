import React from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useContextAuth } from '../features/auth/hooks/useContextAuth';

const PublicRoutes: React.FC = () => {
  const { state } = useContextAuth();
  const location = useLocation();

  if (state.isLoading) {
    return null;
  }


  if (state.isAuthenticated) {
    return <Navigate to="/dashboard" state={{ from: location }} replace />;
  }

  return <Outlet />;
};

export default PublicRoutes;