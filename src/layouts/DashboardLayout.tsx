import { ReactNode, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useContextAuth } from "../features/auth/hooks/useContextAuth";
import { FiChevronDown } from "react-icons/fi";
import { BsNutFill } from "react-icons/bs";

interface DashboardLayoutProps {
  children: ReactNode;
}

const DashboardLayout = ({ children }: DashboardLayoutProps) => {
  const { logout, state, isAdmin } = useContextAuth();
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const handleLogout = async () => {
    await logout();
    navigate("/login", { replace: true });
  };

  const handleModal = () => {
    setIsModalOpen(!isModalOpen);
  };

  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <header className="bg-white shadow-sm">
        <div className="container mx-auto px-4 py-4 flex justify-between items-center">
          <div className="flex items-center gap-8">
            <button
              onClick={toggleSidebar}
              className="cursor-pointer"
              title="Menú"
            >
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
              <li>
                <Link
                  to="/dashboard"
                  className="p-2 flex items-center rounded-2xl"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    fill="currentColor"
                    className="size-5"
                  >
                    <path d="M11.47 3.841a.75.75 0 0 1 1.06 0l8.69 8.69a.75.75 0 1 0 1.06-1.061l-8.689-8.69a2.25 2.25 0 0 0-3.182 0l-8.69 8.69a.75.75 0 1 0 1.061 1.06l8.69-8.689Z" />
                    <path d="m12 5.432 8.159 8.159c.03.03.06.058.091.086v6.198c0 1.035-.84 1.875-1.875 1.875H15a.75.75 0 0 1-.75-.75v-4.5a.75.75 0 0 0-.75-.75h-3a.75.75 0 0 0-.75.75V21a.75.75 0 0 1-.75.75H5.625a1.875 1.875 0 0 1-1.875-1.875v-6.198a2.29 2.29 0 0 0 .091-.086L12 5.432Z" />
                  </svg>
                  Home
                </Link>
              </li>
              {isAdmin ? (
                <li className="">
                  <Link
                    to="/professionals"
                    className="p-2 flex items-center rounded-2xl"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      viewBox="0 0 24 24"
                      fill="currentColor"
                      className="size-5"
                    >
                      <path d="M4.5 6.375a4.125 4.125 0 1 1 8.25 0 4.125 4.125 0 0 1-8.25 0ZM14.25 8.625a3.375 3.375 0 1 1 6.75 0 3.375 3.375 0 0 1-6.75 0ZM1.5 19.125a7.125 7.125 0 0 1 14.25 0v.003l-.001.119a.75.75 0 0 1-.363.63 13.067 13.067 0 0 1-6.761 1.873c-2.472 0-4.786-.684-6.76-1.873a.75.75 0 0 1-.364-.63l-.001-.122ZM17.25 19.128l-.001.144a2.25 2.25 0 0 1-.233.96 10.088 10.088 0 0 0 5.06-1.01.75.75 0 0 0 .42-.643 4.875 4.875 0 0 0-6.957-4.611 8.586 8.586 0 0 1 1.71 5.157v.003Z" />
                    </svg>
                    Profesionales
                  </Link>
                </li>
              ) : (
                <li className="">
                  <Link
                    to="/patients"
                    className="p-2 flex items-center rounded-2xl"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      viewBox="0 0 24 24"
                      fill="currentColor"
                      className="size-5"
                    >
                      <path d="M4.5 6.375a4.125 4.125 0 1 1 8.25 0 4.125 4.125 0 0 1-8.25 0ZM14.25 8.625a3.375 3.375 0 1 1 6.75 0 3.375 3.375 0 0 1-6.75 0ZM1.5 19.125a7.125 7.125 0 0 1 14.25 0v.003l-.001.119a.75.75 0 0 1-.363.63 13.067 13.067 0 0 1-6.761 1.873c-2.472 0-4.786-.684-6.76-1.873a.75.75 0 0 1-.364-.63l-.001-.122ZM17.25 19.128l-.001.144a2.25 2.25 0 0 1-.233.96 10.088 10.088 0 0 0 5.06-1.01.75.75 0 0 0 .42-.643 4.875 4.875 0 0 0-6.957-4.611 8.586 8.586 0 0 1 1.71 5.157v.003Z" />
                    </svg>
                    Pacientes
                  </Link>
                </li>
              )}
              {/* <li className="">
                <Link
                  to="/patients"
                  className="p-2 flex items-center rounded-2xl"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    fill="currentColor"
                    className="size-5"
                  >
                    <path d="M4.5 6.375a4.125 4.125 0 1 1 8.25 0 4.125 4.125 0 0 1-8.25 0ZM14.25 8.625a3.375 3.375 0 1 1 6.75 0 3.375 3.375 0 0 1-6.75 0ZM1.5 19.125a7.125 7.125 0 0 1 14.25 0v.003l-.001.119a.75.75 0 0 1-.363.63 13.067 13.067 0 0 1-6.761 1.873c-2.472 0-4.786-.684-6.76-1.873a.75.75 0 0 1-.364-.63l-.001-.122ZM17.25 19.128l-.001.144a2.25 2.25 0 0 1-.233.96 10.088 10.088 0 0 0 5.06-1.01.75.75 0 0 0 .42-.643 4.875 4.875 0 0 0-6.957-4.611 8.586 8.586 0 0 1 1.71 5.157v.003Z" />
                  </svg>
                  Pacientes
                </Link>
              </li> */}
              <li className="">
                <Link
                  to="/appointments"
                  className="p-2 flex items-center rounded-2xl"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="24"
                    height="24"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                    stroke-linecap="round"
                    stroke-nejoin="round"
                    className="size-5 lucide lucide-calendar-range-icon lucide-calendar-range"
                  >
                    <rect width="18" height="18" x="3" y="4" rx="2" />
                    <path d="M16 2v4" />
                    <path d="M3 10h18" />
                    <path d="M8 2v4" />
                    <path d="M17 14h-6" />
                    <path d="M13 18H7" />
                    <path d="M7 14h.01" />
                    <path d="M17 18h.01" />
                  </svg>
                  Agenda
                </Link>
              </li>
              <li className="">
                <Link to="/admin" className="p-2 flex items-center rounded-2xl">
                  <BsNutFill className="size-5 lucide lucide-calendar-range-icon lucide-calendar-range">
                    <rect width="18" height="18" x="3" y="4" rx="2" />
                    <path d="M16 2v4" />
                    <path d="M3 10h18" />
                    <path d="M8 2v4" />
                    <path d="M17 14h-6" />
                    <path d="M13 18H7" />
                    <path d="M7 14h.01" />
                    <path d="M17 18h.01" />
                  </BsNutFill>
                  Config.
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

      {sidebarOpen && (
        <div
          className="fixed inset-0 bg-black/30 z-30"
          onClick={toggleSidebar}
        />
      )}

      <aside
        className={`fixed top-0 left-0 h-full bg-gray-800 w-64 transform transition-transform duration-300 ease-in-out z-40 ${
          sidebarOpen ? "translate-x-0" : "-translate-x-full"
        }`}
      >
        <div className="p-4 flex gap-4 items-center border-b border-gray-700">
          <button
            className="text-gray-300 hover:text-white cursor-pointer"
            onClick={toggleSidebar}
          >
            <MenuIcon />
          </button>
          <div className="flex items-center">
            {/* TODO reemplazar imagen por logo real de AURA */}
            <img className="w-6 bg-white rounded-full" src={AURALogo} alt="" />
            <h2 className="ml-2 text-white text-lg font-semibold">AURA</h2>
          </div>
        </div>
        <nav className="p-4">
          <ul className="space-y-3 [&>li]:font-semibold [&>li]:text-lg">
            <li>
              <Link
                to="/dashboard"
                className="flex items-center gap-4 text-gray-300 hover:text-white p-2 rounded hover:bg-gray-700"
              >
                <HomeIcon />
                Home
              </Link>
            </li>
            <li>
              <Link
                to="/patients"
                className="flex items-center gap-4 text-gray-300 hover:text-white p-2 rounded hover:bg-gray-700"
              >
                <PatientsIcon />
                Pacientes
              </Link>
            </li>
            <li>
              <Link
                to="/appointments"
                className="flex items-center gap-4 text-gray-300 hover:text-white p-2 rounded hover:bg-gray-700"
              >
                <CalendarRangeIcon />
                Agenda
              </Link>
            </li>
          </ul>
        </nav>
      </aside>
    </div>
  );
};

export default DashboardLayout;
