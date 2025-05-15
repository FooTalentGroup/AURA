// TODO eliminar los componentes que elimine el equipo de UX/UI antes de componetizar
// import { useContextAuth } from "../../features/auth/hooks/useContextAuth";
import { FaChevronLeft, FaChevronRight } from "react-icons/fa";
import DashboardLayout from "../../layouts/DashboardLayout";
import PatientItemList from "../../features/dashboard/components/PatientItemList";
import MedicalHistory from "../../features/dashboard/components/MedicalHistory";
import PatientNotes from "../../features/dashboard/components/PatientNotes";
import MedicalHistoryTable from "../../features/dashboard/components/MedicalHistoryTable";

const DashboardPage = () => {
  // const { state } = useContextAuth();

  return (
    <DashboardLayout>
      {/* <h1 className="text-2xl font-semibold pb-8 pt-2">
        Bienvenido {state.user?.email}
      </h1> */}
      <div className="grid grid-cols-7 gap-4 rounded-lg">
        <article className="col-span-7 flex flex-col gap-4 bg-white rounded-2xl">
          <h2 className="text-2xl pt-6 pb-6 px-4 border-b-2 border-gray-300/90">
            Turnos de hoy, <span className="text-blue-600">Sábado 5</span>
          </h2>

          <div className="flex flex-col bg-white rounded-2xl p-6">
            <div className="flex justify-between items-center border- border-gray-400 pb-6">
              <PatientItemList />
              <div className="flex gap-4">
                <p className="flex items-center gap-2">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 24 24"
                    fill="currentColor"
                    className="size-5"
                  >
                    <path d="M4.5 6.375a4.125 4.125 0 1 1 8.25 0 4.125 4.125 0 0 1-8.25 0ZM14.25 8.625a3.375 3.375 0 1 1 6.75 0 3.375 3.375 0 0 1-6.75 0ZM1.5 19.125a7.125 7.125 0 0 1 14.25 0v.003l-.001.119a.75.75 0 0 1-.363.63 13.067 13.067 0 0 1-6.761 1.873c-2.472 0-4.786-.684-6.76-1.873a.75.75 0 0 1-.364-.63l-.001-.122ZM17.25 19.128l-.001.144a2.25 2.25 0 0 1-.233.96 10.088 10.088 0 0 0 5.06-1.01.75.75 0 0 0 .42-.643 4.875 4.875 0 0 0-6.957-4.611 8.586 8.586 0 0 1 1.71 5.157v.003Z" />
                  </svg>
                  <span className="font-semibold text-gray-500">1/12</span>
                </p>
                <button className="text-gray-500 hover:text-gray-900 cursor-pointer">
                  <FaChevronLeft />
                </button>
                <button className="text-gray-500 hover:text-gray-900 cursor-pointer">
                  <FaChevronRight />
                </button>
              </div>
            </div>
            <div className="py-6">
              <MedicalHistory children={<MedicalHistoryTable />} />
            </div>
            <div className="py-6">
              <PatientNotes />
            </div>
          </div>
        </article>
      </div>
    </DashboardLayout>
  );
};

export default DashboardPage;
