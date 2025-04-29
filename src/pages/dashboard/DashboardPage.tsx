import { useContextAuth } from "../../features/auth/hooks/useContextAuth";
import DashboardLayout from "../../layouts/DashboardLayout";
import { FiEdit2, FiChevronLeft, FiChevronRight } from "react-icons/fi";

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
          <div className="flex justify-between items-center mb-6 text-neutral-600 bg-white py-6 px-6">
            <button className="hover:text-black cursor-pointer">
              <FiChevronLeft size={22} />
            </button>
            <ul className="w-10/12 flex justify-between items-center">
              <li className="flex items-center gap-2 font-semibold">
                <img
                  className="w-8 h-8 rounded-full"
                  src="https://randomuser.me/api/portraits/med/men/7.jpg"
                  alt=""
                />
                Lucas Fernández (37)
              </li>
              <li className="">34.617.293</li>
              <li className="">05 mes a las 14.00</li>
              <li className="">Asistencia</li>
              <li className="">Llamar</li>
            </ul>
            <button className="hover:text-black cursor-pointer">
              <FiChevronRight size={22} />
            </button>
          </div>

          <article>
            <div className="bg-white p-4">
              <header className="flex justify-between items-center mb-4">
                <div className="flex items-center gap-2">
                  <img
                    src="https://randomuser.me/api/portraits/med/men/7.jpg"
                    className="w-12 h-12 rounded-full"
                  />

                  <h3 className="text-lg font-semibold">Lucas Fernández</h3>
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
                    value={"15/03/1987"}
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
                    value={37}
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
                    value={"34.617.293"}
                    className="w-full py-2 px-3 bg-gray-100 rounded text-gray-700"
                  />
                </div>
              </form>
            </div>

            <div className="bg-white p-4">
              <form className="">
                <div className="col-span-1">
                  <label className="block text-sm font-normal text-gray-500 mb-1">
                    Diagnóstico
                  </label>
                  <input
                    type="text"
                    name="birthDate"
                    placeholder="Escriba el diagnóstico"
                    value={
                      "Paciente masculino de 37 años con antecedentes de hipertensión arterial controlada. Presenta dolor lumbar crónico irradiado a pierna izquierda, asociado a hernia de disco L4-L5 confirmada por resonancia. Refiere limitación funcional moderada. En tratamiento con kinesiología y AINEs."
                    }
                    className="w-full py-2 px-3 bg-gray-100 rounded text-gray-700"
                  />
                </div>
              </form>
            </div>
          </article>
        </article>
      </div>
    </DashboardLayout>
  );
};

export default DashboardPage;
