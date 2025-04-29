import { useContextAuth } from "../../features/auth/hooks/useContextAuth";
import DashboardLayout from "../../layouts/DashboardLayout";
import { FiEdit2 } from "react-icons/fi";

const DashboardPage = () => {
  const { state } = useContextAuth();

  return (
    <DashboardLayout>
      <h1 className="text-2xl font-semibold pb-8 pt-2">
        Bienvenido {state.user?.email}
      </h1>
      <div className="grid grid-cols-3 gap-4 rounded-lg">
        <div className="col-span-1 bg-white p-4 mb-6">
          <div>
            <header className="flex justify-between">
              <h2 className="">Turnos de hoy</h2>
              <p>Lunes 5</p>
            </header>
            <footer className="flex justify-between">
              <p>0000</p>
              <p>00/00</p>
            </footer>
          </div>
          <div></div>
        </div>

        <article className="col-span-2">
          <div className="mb-6 bg-white p-4">
            <ul className="flex justify-between">
              <li className="">Nombre del Paciente</li>
              <li className="">[00.000.000]</li>
              <li className="">[00 mes a las 00.00]</li>
              <li className="">Asistencia</li>
              <li className="">Llamar</li>
            </ul>
          </div>

          <div className="bg-white p-4">
            <header className="flex justify-between items-center mb-4">
              <div className="flex items-center gap-2">
                <span className="text-lg font-semibold w-8 h-8 flex justify-center items-center p-3 rounded-full border hover:bg-gray-100 text-gray-800">
                  P
                </span>
                <h3 className="text-lg font-semibold">Nombre del paciente</h3>
              </div>
              <button>
                <FiEdit2 size={20} />
              </button>
            </header>

            <form className="grid grid-cols-2 gap-4">
              <div className="col-span-1">
                <label className="block text-sm font-normal text-gray-500 mb-1">
                  Fecha de nacimiento
                </label>
                <input
                  type="text"
                  name="birthDate"
                  placeholder="00 / 00 / 0000"
                  className="w-full py-2 px-3 bg-gray-100 rounded text-gray-700"
                />
              </div>

              <div className="col-span-1">
                <label className="block text-sm font-normal text-gray-500 mb-1">
                  Edad
                </label>
                <input
                  type="text"
                  name="age"
                  placeholder="00"
                  className="w-full py-2 px-3 bg-gray-100 rounded text-gray-700"
                />
              </div>

              <div className="col-span-2">
                <label className="block text-sm font-normal text-gray-500 mb-1">
                  DNI
                </label>
                <input
                  type="text"
                  name="dni"
                  placeholder="00.000.000"
                  className="w-full py-2 px-3 bg-gray-100 rounded text-gray-700"
                />
              </div>
            </form>

            {/* <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="border rounded p-4 hover:bg-gray-50 cursor-pointer">
                <h4 className="font-medium">Gestionar Perfil</h4>
                <p className="text-sm text-gray-500">
                  Actualiza tu información personal
                </p>
              </div>
              <div className="border rounded p-4 hover:bg-gray-50 cursor-pointer">
                <h4 className="font-medium">Configuración</h4>
                <p className="text-sm text-gray-500">
                  Ajusta las preferencias de tu cuenta
                </p>
              </div>
            </div> */}
          </div>
        </article>
      </div>
    </DashboardLayout>
  );
};

export default DashboardPage;
