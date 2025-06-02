import { ReactNode, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useContextAuth } from "../features/auth/hooks/useContextAuth";
import { FiChevronDown } from "react-icons/fi";
import { BsPersonBadgeFill } from "react-icons/bs";
import { useLocation } from "react-router-dom";
interface DashboardLayoutProps {
  children: ReactNode;
}

const DashboardLayout = ({ children }: DashboardLayoutProps) => {
  const { logout, state } = useContextAuth();
  const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false);
    const { isAdmin} = useContextAuth();
  const location = useLocation();
  const handleLogout = async () => {
    await logout();
    navigate("/login", { replace: true });
  };

  const handleModal = () => {
    setIsModalOpen(!isModalOpen);
  };
  const isPerfilePage = location.pathname === "/profile";

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      <header className=" ">
        <div className="w-full px-20 py-4 flex justify-between items-center">
          <div className="flex items-center gap-8">
            <h1 className="font-bold text-xl text-[#0072C3] cursor-pointer" onClick={() => navigate("/")}>AURA</h1>
          </div>

          <div className="flex items-center relative">
            <ul className="flex items-center gap-4">
              <li>
                <a
                  href="#"
                  className="text-xl font-semibold w-11 h-11 flex justify-center items-center gap-2 p-3 rounded-full bg-sky-200 hover:bg-sky-300 text-gray-800"
                  onClick={() => navigate("/profile")}
                >
                  {state.user?.username?.charAt(0).toUpperCase() || "X"}
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
        <aside className="flex-grow flex justify-center items-center max-w-24 mb-[29em]">
         <nav className="px-1">
          {isAdmin && !isPerfilePage && ( <ul className="text-gray-800 flex flex-col gap-4 h-full text-center [&>li>a]:flex-col [&>li>a]:hover:text-blue-700 [&>li>a]:hover:bg-sky-200/60">
              
              
        

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
                        <li className="">
                  <Link
                    to="/professionals"
                    className="p-2 flex items-center rounded-2xl"
                    >
                    
                 <BsPersonBadgeFill className="size-5"/>
                    Personal
                  </Link>
                </li>
                </ul>)}
          </nav> 
        </aside>
        <main className="flex-grow p-6">{children}</main>
      </div>
    </div>
  );
};

export default DashboardLayout;
