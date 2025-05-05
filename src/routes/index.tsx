import { Routes, Route, Navigate } from "react-router-dom";
import PublicRoutes from "./PublicRoutes";
import PrivateRoutes from "./PrivateRoutes";

// Páginas públicas
import LoginPage from "../pages/auth/LoginPage";
import RegisterPage from "../pages/auth/RegisterPage";

// Páginas privadas
import DashboardPage from "../pages/dashboard/DashboardPage";
import PatientsPage from "../pages/patients/PatientsPage";
import AppointmentsPage from "../pages/appointments/AppointmentsPage";
import NotFoundPage from "../pages/NotFoundPage";
import ForgotPasswordForm from "../pages/auth/ForgotPasswordForm";

const AppRoutes = () => {
  return (
    <Routes>
      <Route element={<PublicRoutes />}>
        <Route path="/forgot-password" element={<ForgotPasswordForm />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
      </Route>

      <Route element={<PrivateRoutes />}>
        <Route path="/dashboard" element={<DashboardPage />} />
        {/* <Route path="/dashboard" element={<ProfilePage />} /> */}
        <Route path="/patients" element={<PatientsPage />} />
        <Route path="/appointments" element={<AppointmentsPage />} />
      </Route>

      <Route path="/404" element={<NotFoundPage />} />

      <Route path="/" element={<Navigate to="/login" replace />} />

      <Route path="*" element={<Navigate to="/404" replace />} />
    </Routes>
  );
};

export default AppRoutes;
