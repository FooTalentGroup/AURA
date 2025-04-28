import { ReactNode, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useContextAuth } from "../features/auth/hooks/useContextAuth";
import { FaHome, FaUserFriends, FaCalendarAlt } from "react-icons/fa";
import {
  FiMessageCircle,
  FiBell,
  // FiSearch,
  FiChevronDown,
} from "react-icons/fi";

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
          <nav className="">
            <ul className="flex">
              <li className="">
                <Link
                  to="/dashboard"
                  className="flex items-center gap-2 py-2 px-4 rounded-full hover:bg-gray-100 text-gray-800"
                >
                  <FaHome />
                  Home
                </Link>
              </li>
              <li className="">
                <Link
                  to="/patients"
                  className="flex items-center gap-2 py-2 px-4 rounded-full hover:bg-gray-100 text-gray-800"
                >
                  <FaUserFriends />
                  Pacientes
                </Link>
              </li>
              <li className="">
                <Link
                  to="/appointments"
                  className="flex items-center gap-2 py-2 px-4 rounded-full hover:bg-gray-100 text-gray-800"
                >
                  <FaCalendarAlt />
                  Agenda
                </Link>
              </li>
            </ul>
          </nav>

          <div className="flex items-center relative">
            <ul className="flex items-center gap-2">
              <li>
                <a
                  href="#"
                  className="flex items-center gap-2 p-3 rounded-full border hover:bg-gray-100 text-gray-800"
                >
                  <FiMessageCircle size={20} />
                </a>
              </li>
              <li>
                <a
                  href="#"
                  className="flex items-center gap-2 p-3 rounded-full border hover:bg-gray-100 text-gray-800"
                >
                  <FiBell size={20} />
                </a>
              </li>
              <li>
                <a
                  href="#"
                  className="text-xl font-semibold w-11 h-11 flex justify-center items-center gap-2 p-3 rounded-full border hover:bg-gray-100 text-gray-800"
                >
                  P
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
              } absolute -bottom-27 right-0 bg-neutral-200 rounded-md p-4`}
            >
              <nav>
                <ul className="flex flex-col gap-3">
                  <li className="font-medium">{state.user?.email}</li>
                  <li>
                    <button
                      onClick={handleLogout}
                      className="font-semibold cursor-pointer hover:text-blue-600"
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
