import { PencilIcon } from "../../../components/shared/ui/Icons";
import { DiagnosticTabProps } from "../types/patientTabs.types";

function DiagnosticTab({ diagnoses }: DiagnosticTabProps) {
  return (
    <section className="bg-gray-200/60 p-4 rounded-b-2xl rounded-t-2xl">
      <article className="h-fit w-3/6 bg-white py-2 rounded-lg mx-auto">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">Diagnóstico</h3>
          <button className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer">
            <PencilIcon />
          </button>
        </header>
        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          <div className="">
            <label className="font-semibold text-blue-600">Fecha</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {diagnoses.date}
            </p>
          </div>

          <div className="">
            <label className="font-semibold text-blue-600">
              Id del Profesional
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {diagnoses.idProfessional}
            </p>
          </div>
          <div className="col-span-2 ">
            <label className="font-semibold text-blue-600">Título</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {diagnoses.title}
            </p>
          </div>
          <div className="col-span-2 px-3 rounded-md">
            <div>{diagnoses.details}</div>
          </div>
        </form>
      </article>
    </section>
  );
}

export default DiagnosticTab;
