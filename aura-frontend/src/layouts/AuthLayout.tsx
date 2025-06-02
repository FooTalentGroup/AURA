import { ReactNode } from "react";

interface AuthLayoutProps {
  children: ReactNode;
}

const AuthLayout = ({ children }: AuthLayoutProps) => {
  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <header className="bg-white shadow-sm py-4">
        <div className="container mx-auto px-4">
          <h1 className="text-xl font-semibold text-gray-800">
            AURA | Administración Unificada de Registros y Atenciones
          </h1>
        </div>
      </header>

      <main className="flex-grow">{children}</main>

      <footer className="bg-white py-4 mt-auto">
        <div className="container mx-auto px-4 text-center text-gray-500 text-sm">
          &copy; {new Date().getFullYear()} AURA. Todos los derechos reservados.
        </div>
      </footer>
    </div>
  );
};

export default AuthLayout;
