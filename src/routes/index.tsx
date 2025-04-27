import { Routes, Route, Navigate } from "react-router-dom";
import PublicRoutes from "./PublicRoutes";
import PrivateRoutes from "./PrivateRoutes";

// Páginas públicas
import LoginPage from "../pages/auth/LoginPage";
import RegisterPage from "../pages/auth/RegisterPage";

// Páginas privadas
import DashboardPage from "../pages/dashboard/DashboardPage";
import NotFoundPage from "../pages/NotFoundPage";

const AppRoutes = () => {
  return (
    <Routes>
      {/* Rutas públicas */}
      <Route element={<PublicRoutes />}>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
      </Route>

      {/* Rutas privadas */}
      <Route element={<PrivateRoutes />}>
        <Route path="/dashboard" element={<DashboardPage />} />
        {/* Aquí puedes añadir más rutas privadas */}
      </Route>

      {/* Ruta para la página 404 */}
      <Route path="/404" element={<NotFoundPage />} />

      {/* Redirección a login como ruta por defecto */}
      <Route path="/" element={<Navigate to="/login" replace />} />

      {/* Redirección a 404 para cualquier otra ruta */}
      <Route path="*" element={<Navigate to="/404" replace />} />
    </Routes>
  );
};

export default AppRoutes;
