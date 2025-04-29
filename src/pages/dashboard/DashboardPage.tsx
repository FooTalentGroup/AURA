import { useContextAuth } from "../../features/auth/hooks/useContextAuth";
import DashboardLayout from "../../layouts/DashboardLayout";
import {
  FiEdit2,
  FiChevronLeft,
  FiChevronRight,
  FiMoreHorizontal,
} from "react-icons/fi";

interface DiagnosisFormProps {
  title: string;
  day: boolean;
}

const DiagnosisForm = ({ title, day }: DiagnosisFormProps) => {
  return (
    <div className="w-full mx-auto bg-white p-4 mt-2">
      <div className="flex items-center justify-between mb-5">
        <h2 className="text-sm text-gray-800">{title}</h2>
        <button className="text-gray-700 hover:text-black focus:outline-none cursor-pointer">
          <FiMoreHorizontal size={20} />
        </button>
      </div>
      {day && <h3 className="text-sm text-gray-800 mb-3">Día</h3>}

      <div className="relative mb-2">
        <input
          type="text"
          placeholder="Ingrese una descripción"
          className="peer w-full border border-gray-900 rounded px-3 pt-3 pb-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <label className="absolute left-3 top-1 text-gray-500 text-sm bg-white px-1 peer-placeholder-shown:-top-2 peer-placeholder-shown:text-xs peer-placeholder-shown:text-gray-400 transition-all duration-200">
          Descripción
        </label>
      </div>
    </div>
  );
};

const DashboardPage = () => {
  const { state } = useContextAuth();

  return (
    <DashboardLayout>
      <h1 className="text-2xl font-semibold pb-8 pt-2">
        Bienvenido {state.user?.email}
      </h1>
      <div className="grid grid-cols-7 gap-4 rounded-lg">
        <div className="col-span-2 mb-6">
          <div className="bg-white p-4">
            <header className="flex justify-between">
              <h2 className="">Turnos de hoy</h2>
              <p>Lunes 5</p>
            </header>
            <footer className="flex justify-between">
              <p>0000</p>
              <p>00/00</p>
            </footer>
          </div>
        </div>

        <article className="col-span-5">
          <div className="bg-white flex justify-between items-center mb-6 text-neutral-600 py-6 px-6">
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

          <article className="bg-white grid grid-cols-7">
            <div className="col-span-3">
              <div className="p-4">
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
                    <label className="block text-sm font-semibold text-gray-500 mb-2">
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
                    <label className="block text-sm font-semibold text-gray-500 mb-2">
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
                    <label className="block text-sm font-semibold text-gray-500 mb-2">
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

              <div className="p-4">
                <form className="">
                  <header className="flex justify-between items-center mb-4">
                    <h2 className="">Diagnóstico</h2>
                    <button>
                      <FiEdit2 size={20} />
                    </button>
                  </header>
                  {/* <input
                    type="text"
                    name="birthDate"
                    placeholder="Escriba el diagnóstico"
                    value={
                      "Paciente masculino de 37 años con antecedentes de hipertensión arterial controlada. Presenta dolor lumbar crónico irradiado a pierna izquierda, asociado a hernia de disco L4-L5 confirmada por resonancia. Refiere limitación funcional moderada. En tratamiento con kinesiología y AINEs."
                    }
                    className="w-full py-2 px-3 bg-gray-100 rounded text-gray-700"
                  /> */}
                  <p className="py-1 px-3 bg-gray-100 text-gray-700 line-clamp-4">
                    Paciente masculino de 37 años con antecedentes de
                    hipertensión arterial controlada. Presenta dolor lumbar
                    crónico irradiado a pierna izquierda, asociado a hernia de
                    disco L4-L5 confirmada por resonancia. Refiere limitación
                    funcional moderada. En tratamiento con kinesiología y AINEs.
                  </p>
                </form>
              </div>
            </div>
            <div className="col-span-2">
              <DiagnosisForm title="Diagnóstico" day={false} />
              <DiagnosisForm title="Observaciones" day={true} />
            </div>
            <div className="col-span-2">
              <DiagnosisForm title="Sesiones" day={true} />
            </div>
          </article>
        </article>
      </div>
    </DashboardLayout>
  );
};

export default DashboardPage;
