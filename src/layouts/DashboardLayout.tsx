import { ReactNode, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useContextAuth } from "../features/auth/hooks/useContextAuth";
import { FiChevronDown } from "react-icons/fi";

interface DashboardLayoutProps {
  children: ReactNode;
}

const DashboardLayout = ({ children }: DashboardLayoutProps) => {
  const { logout, state } = useContextAuth();
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleLogout = async () => {
    await logout();
    navigate("/login", { replace: true });
  };

  const handleModal = () => {
    setIsModalOpen(!isModalOpen);
  };

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <header className="bg-white shadow-sm">
        <div className="w-full px-20 py-4 flex justify-between items-center">
          <div className="flex items-center gap-8">
            <h1 className="font-bold text-xl text-[#0072C3]">AURA</h1>
          </div>

          <div className="flex items-center relative">
            <ul className="flex items-center gap-4">
  
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
          </nav>
        </aside>
        <main className="flex-grow p-6">{children}</main>
      </div>
    </div>
  );
};

export default DashboardLayout;
