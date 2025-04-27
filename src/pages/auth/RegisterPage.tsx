// src/pages/auth/RegisterPage.tsx

import { Link } from "react-router-dom";
import RegisterForm from "../../features/auth/components/RegisterForm";
import AuthLayout from "../../layouts/AuthLayout";

const RegisterPage = () => {
  return (
    <AuthLayout>
      <div className="flex flex-col items-center justify-center min-h-screen p-4">
        <div className="w-full max-w-md">
          <RegisterForm />
          <div className="text-center mt-4">
            <p className="text-gray-600">
              ¿Ya tienes cuenta?{" "}
              <Link to="/login" className="text-blue-500 hover:text-blue-700">
                Inicia sesión aquí
              </Link>
            </p>
          </div>
        </div>
      </div>
    </AuthLayout>
  );
};

export default RegisterPage;
