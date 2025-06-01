import { Routes, Route, Navigate } from "react-router-dom";
import PublicRoutes from "./PublicRoutes";
import PrivateRoutes from "./PrivateRoutes";
import AdminRoute from "./AdminRoutes";

// Páginas públicas
import LoginPage from "../pages/auth/LoginPage";

// Páginas privadas
import DashboardPage from "../pages/dashboard/DashboardPage";
import PatientsPage from "../pages/patients/PatientsPage";
import NotFoundPage from "../pages/NotFoundPage";
import ProfilePage from "../pages/profile/ProfilePage";
import AdminPage from "../pages/admin/AdminPage";
import ProfessionalPage from "../pages/Professional/ProfessionalPage";
import { ProfessionalRegister } from "../features/professional/components/ProfessionalRegister";
import { ReceptionistRegister } from "../features/Receptionist/components/RecepcionistRegister";
import PatientTabs from "../pages/patientTabs/PatientTabsPage";
import { PatientRegister } from "../features/patients/components/PatientRegister";

const AppRoutes = () => {
  return (
    <Routes>
      <Route element={<PublicRoutes />}>
        <Route path="/login" element={<LoginPage />} />
      </Route>

      <Route element={<PrivateRoutes />}>
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/patients" element={<PatientsPage />} />
        <Route path="/patient/:id" element={<PatientTabs />} />
        <Route path="/Pregister" element={<ProfessionalRegister />} />
        <Route path="/Rregister" element={<ReceptionistRegister />} />
        <Route path="/Sregister" element={<PatientRegister />} />
      </Route>

      <Route element={<AdminRoute />}>
        <Route path="admin" element={<AdminPage />} />
        <Route path="/professionals" element={<ProfessionalPage />} />
      </Route>

      <Route path="/404" element={<NotFoundPage />} />

      <Route path="/" element={<Navigate to="/login" replace />} />

      <Route path="*" element={<Navigate to="/404" replace />} />
    </Routes>
  );
};

export default AppRoutes;
