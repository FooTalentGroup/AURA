// TODO eliminar los componentes que elimine el equipo de UX/UI antes de componetizar
import { useContextAuth } from "../../features/auth/hooks/useContextAuth";
import DashboardLayout from "../../layouts/DashboardLayout";
import { FaChevronLeft, FaChevronRight } from "react-icons/fa";

const PatientItemList = () => {
  return (
    <div className="text-neutral-600">
      <ul className="flex justify-between items-center">
        <li className="flex gap-2">
          <span className="flex items-center font-semibold text-sm bg-gray-200/60 px-3 py-[1px] rounded-xl">
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
        <li className="">OSPLAD</li>
        <li className="">
          <button className="text-blue-600 py-2 px-5 border border-black rounded-4xl hover:bg-gray-200 cursor-pointer">
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
      <h2 className="flex items-center gap-2 text-xl font-semibold mb-4">
        {" "}
        <ClipBoardIcon /> Historial clínico
      </h2>
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
        <h2 className="flex gap-2 items-center text-xl font-semibold">
          <ClipBoardIcon /> Notas
        </h2>
        <button className="text-blue-600 py-2 px-5 border border-black rounded-4xl hover:bg-gray-200 cursor-pointer">
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

const ClipBoardIcon = () => {
  return (
    <div className="bg-purple-200 p-2 rounded-full">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        strokeWidth={1.5}
        stroke="currentColor"
        className="size-6"
      >
        <path
          strokeLinecap="round"
          strokeLinejoin="round"
          d="M15.666 3.888A2.25 2.25 0 0 0 13.5 2.25h-3c-1.03 0-1.9.693-2.166 1.638m7.332 0c.055.194.084.4.084.612v0a.75.75 0 0 1-.75.75H9a.75.75 0 0 1-.75-.75v0c0-.212.03-.418.084-.612m7.332 0c.646.049 1.288.11 1.927.184 1.1.128 1.907 1.077 1.907 2.185V19.5a2.25 2.25 0 0 1-2.25 2.25H6.75A2.25 2.25 0 0 1 4.5 19.5V6.257c0-1.108.806-2.057 1.907-2.185a48.208 48.208 0 0 1 1.927-.184"
        />
      </svg>
    </div>
  );
};

const CalendarIcon = () => {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      fill="none"
      viewBox="0 0 24 24"
      strokeWidth={1.5}
      stroke="currentColor"
      className="size-6 text-blue-600"
    >
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 0 1 2.25-2.25h13.5A2.25 2.25 0 0 1 21 7.5v11.25m-18 0A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75m-18 0v-7.5A2.25 2.25 0 0 1 5.25 9h13.5A2.25 2.25 0 0 1 21 11.25v7.5m-9-6h.008v.008H12v-.008ZM12 15h.008v.008H12V15Zm0 2.25h.008v.008H12v-.008ZM9.75 15h.008v.008H9.75V15Zm0 2.25h.008v.008H9.75v-.008ZM7.5 15h.008v.008H7.5V15Zm0 2.25h.008v.008H7.5v-.008Zm6.75-4.5h.008v.008h-.008v-.008Zm0 2.25h.008v.008h-.008V15Zm0 2.25h.008v.008h-.008v-.008Zm2.25-4.5h.008v.008H16.5v-.008Zm0 2.25h.008v.008H16.5V15Z"
      />
    </svg>
  );
};

const CheckIcon = () => {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 24 24"
      fill="currentColor"
      className="size-6 text-blue-600"
    >
      <path
        fillRule="evenodd"
        d="M2.25 12c0-5.385 4.365-9.75 9.75-9.75s9.75 4.365 9.75 9.75-4.365 9.75-9.75 9.75S2.25 17.385 2.25 12Zm13.36-1.814a.75.75 0 1 0-1.22-.872l-3.236 4.53L9.53 12.22a.75.75 0 0 0-1.06 1.06l2.25 2.25a.75.75 0 0 0 1.14-.094l3.75-5.25Z"
        clipRule="evenodd"
      />
    </svg>
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
            <h2 className="text-xl mb-6">Turnos de hoy</h2>

            <div className="flex justify-between px-4">
              <article className="">
                <p className="text-blue-600 text-sm mb-2">Agendados</p>
                <p className="flex gap-2 w-32 font-semibold border border-gray-300 p-2 rounded-xl">
                  <CalendarIcon />
                  12
                </p>
              </article>
              <article className="">
                <p className="text-blue-600 text-sm mb-2">Atendidos</p>
                <p className="flex gap-2 w-32 font-semibold border border-gray-300 p-2 rounded-xl">
                  <CheckIcon /> 1/12
                </p>
              </article>
            </div>
          </div>
        </div>

        <article className="col-span-5 flex flex-col gap-4">
          <div className="flex justify-between items-center bg-white py-6 px-4 rounded-2xl">
            <h2 className="text-xl">Paciente asignado</h2>

            <div className="flex gap-4">
              <span className="font-semibold text-gray-500">1/12</span>
              <button className="text-gray-500 hover:text-gray-900 cursor-pointer">
                <FaChevronLeft />
              </button>
              <button className="text-gray-500 hover:text-gray-900 cursor-pointer">
                <FaChevronRight />
              </button>
            </div>
          </div>
          <div className="flex flex-col bg-white rounded-2xl p-6">
            <div className="border-b border-gray-400 pb-6">
              <PatientItemList />
            </div>
            <div className="border-b border-gray-400 py-6">
              <MedicalHistory />
            </div>
            <div className="border-b border-gray-400 py-6">
              <PatientNotes />
            </div>
            <footer className="flex justify-end gap-4 pt-6">
              <select
                className="border border-gray-400 px-2 rounded-lg cursor-pointer"
                name=""
                id=""
              >
                <option value="">Asistencia</option>
                <option value="">Presente</option>
                <option value="">Ausente</option>
              </select>
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
