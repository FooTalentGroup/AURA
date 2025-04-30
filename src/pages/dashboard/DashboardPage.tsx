// TODO eliminar los componentes que elimine el equipo de UX/UI antes de componetizar
import { useContextAuth } from "../../features/auth/hooks/useContextAuth";
import DashboardLayout from "../../layouts/DashboardLayout";
import { FaChevronLeft, FaChevronRight } from "react-icons/fa";

const PatientItemList = () => {
  return (
    <div className="text-neutral-600">
      <ul className="flex justify-between items-center">
        <li className="flex gap-2">
          <span className="text-sm bg-gray-200 px-2 py-1 rounded-xl">
            12:00
          </span>
          <div className="flex items-center gap-2">
            <img
              className="w-8 h-8 rounded-full object-cover"
              src="https://images.unsplash.com/photo-1605783313291-1b996e9e7376?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
              alt=""
            />
            <ul className="leading-3 font-semibold">
              <li className="text-gray-700">Lucas Fernández</li>
              <li className="text-gray-500 text-sm">(13)</li>
            </ul>
          </div>
        </li>
        <li className="">44.617.293</li>
        <li className="">[Obra Social]</li>
        <li className="">
          <button className="py-2 px-5 border rounded-4xl hover:bg-gray-300 cursor-pointer">
            Ver paciente
          </button>
        </li>
      </ul>
    </div>
  );
};

const MedicalHistory = () => {
  return (
    <article>
      <h2 className="text-xl font-semibold mb-4">Historial clinico</h2>
      <ul className="[&>li]:mb-2 text-sm">
        <li>05/04/2025 — Audiometría infantil — Normal</li>
        <li>20/03/2025 — Evaluación neurológica — Seguimiento recomendado</li>
        <li>12/02/2025 — Test de lenguaje — Déficit leve</li>
      </ul>
    </article>
  );
};

const PatientNotes = () => {
  return (
    <article>
      <header className="flex justify-between mb-4">
        <h2 className="text-xl font-semibold">Notas</h2>
        <button className="text-neutral-600 py-2 px-5 border rounded-4xl hover:bg-gray-300 cursor-pointer">
          Agregar Nota
        </button>
      </header>
      <p>
        Paciente de 13 años con déficit leve en lenguaje, evaluado en febrero.
        Seguimiento neurológico recomendado en marzo por posibles dificultades
        de aprendizaje. Audiometría normal en abril, sin alteraciones auditivas.
        Se sugiere terapia de lenguaje y control neurológico periódico para
        monitorear evolución. Familia comprometida con el proceso.
      </p>
    </article>
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
          <div className="bg-white p-4 rounded-2xl">
            <h2 className="text-center mb-6">Turnos de hoy</h2>

            <div className="flex justify-between px-4">
              <article className="">
                <p className="text-sm mb-2">Agendados</p>
                <p className="w-32 font-semibold border border-gray-300 p-2 rounded-2xl">
                  0005
                </p>
              </article>
              <article className="">
                <p className="text-sm mb-2">Atendidos</p>
                <p className="w-32 font-semibold border border-gray-300 p-2 rounded-2xl">
                  00/00
                </p>
              </article>
            </div>
          </div>
        </div>

        <article className="col-span-5 flex flex-col gap-4">
          <div className="flex justify-between items-center bg-white py-6 px-4 rounded-2xl">
            <h2 className="text-xl">Paciente asignado</h2>

            <div className="flex gap-4">
              <span className="font-semibold text-gray-500">11/12</span>
              <button className="text-gray-500 hover:text-gray-900 cursor-pointer">
                <FaChevronLeft />
              </button>
              <button className="text-gray-500 hover:text-gray-900 cursor-pointer">
                <FaChevronRight />
              </button>
            </div>
          </div>
          <div className="flex flex-col gap-4 bg-white rounded-2xl p-4">
            <PatientItemList />
            <MedicalHistory />
            <PatientNotes />
            <footer className="flex justify-end gap-2">
              <button className="text-neutral-600 py-2 px-5 border rounded-4xl hover:bg-gray-300 cursor-pointer">
                Asistencia
              </button>
              <button className="text-white py-2 px-5 bg-blue-500 rounded-4xl hover:bg-blue-700 cursor-pointer">
                Llamar
              </button>
            </footer>
          </div>
        </article>
      </div>
    </DashboardLayout>
  );
};

export default DashboardPage;
