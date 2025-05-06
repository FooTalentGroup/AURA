import { ReactNode, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useContextAuth } from "../features/auth/hooks/useContextAuth";
import { FiChevronDown } from "react-icons/fi";
import {
  CalendarRangeIcon,
  HomeIcon,
  MenuIcon,
  MessageSquareIcon,
  PatientsIcon,
} from "../components/shared/ui/Icons";

interface DashboardLayoutProps {
  children: ReactNode;
}

const DashboardLayout = ({ children }: DashboardLayoutProps) => {
  const { logout, state } = useContextAuth();
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const handleModal = () => {
    setIsModalOpen(!isModalOpen);
  };

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <header className="bg-white shadow-sm">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <div className="flex items-center gap-8">
            <button className="cursor-pointer" title="Menú">
              <MenuIcon />
            </button>
            <h1 className="font-bold text-xl text-blue-500">AURA</h1>
          </div>

          <div className="flex items-center relative">
            <ul className="flex items-center gap-4">
              <li>
                <a
                  href="#"
                  className="flex items-center gap-2 hover:bg-gray-100 text-gray-800"
                >
                  <MessageSquareIcon />
                </a>
              </li>
              <li>
                <a
                  href="#"
                  className="text-xl font-semibold w-11 h-11 flex justify-center items-center gap-2 p-3 rounded-full bg-sky-200 hover:bg-sky-300 text-gray-800"
                >
                  A
                </a>
              </li>
              <li className="flex items-center">
                <button onClick={handleModal} className="cursor-pointer">
                  <FiChevronDown size={24} />
                </button>
              </li>
            </ul>

            <div
              className={`${
                isModalOpen ? "block" : "hidden"
              } absolute z-[1] top-15 right-0 bg-neutral-200 rounded-md p-4`}
            >
              <nav>
                <ul className="font-medium flex flex-col gap-3">
                  <li>{state.user?.email}</li>
                  <li>
                    <Link className="hover:text-blue-600" to="/profile">
                      Perfil
                    </Link>
                  </li>
                  <li>
                    <button
                      onClick={handleLogout}
                      className="cursor-pointer hover:text-blue-600"
                    >
                      Cerrar Sesión
                    </button>
                  </li>
                </ul>
              </nav>
            </div>
          </div>
        </div>
      </header>

      <div className="flex flex-grow">
        <aside className="flex-grow flex justify-center items-center max-w-24">
          <nav className="px-1">
            <ul className="text-gray-800 flex flex-col gap-4 h-full text-center [&>li>a]:flex-col [&>li>a]:hover:text-blue-700 [&>li>a]:hover:bg-sky-200/60">
              <li className="">
                <Link
                  to="/dashboard"
                  className="p-2 flex items-center rounded-2xl"
                >
                  <HomeIcon />
                  Home
                </Link>
              </li>
              <li className="">
                <Link
                  to="/patients"
                  className="p-2 flex items-center rounded-2xl"
                >
                  <PatientsIcon />
                  Pacientes
                </Link>
              </li>
              <li className="">
                <Link
                  to="/appointments"
                  className="p-2 flex items-center rounded-2xl"
                >
                  <CalendarRangeIcon />
                  Agenda
                </Link>
              </li>
            </ul>
          </nav>
        </aside>
        <main className="flex-grow p-6">{children}</main>
      </div>

      <footer className="bg-white py-4">
        <div className="container mx-auto px-4 text-center text-gray-500 text-sm">
          &copy; {new Date().getFullYear()} AURA. Todos los derechos reservados.
        </div>
      </footer>
    </div>
  );
};

export default DashboardLayout;
