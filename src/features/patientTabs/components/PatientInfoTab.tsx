import { PencilIcon } from "../../../components/shared/ui/Icons";
import { PatientTabsProps } from "../types/patientTabs.types";

function PatientInfoTab({ patient }: PatientTabsProps) {
  return (
    <section className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4 bg-gray-200/60 p-4 rounded-b-2xl rounded-tr-2xl">
      <article className="h-fit bg-white py-2 rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">
            Datos personales
          </h3>
          <button className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer">
            <PencilIcon />
          </button>
        </header>
        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Nombre y Apellido
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patient?.name} {patient?.lastName}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">
              Fecha de nacimiento
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patient?.birthDate}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Edad</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patient?.age}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">DNI</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patient?.dni}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Sexo</label>
            <p className="capitalize py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patient?.genre}
            </p>
          </div>
        </form>
      </article>

      <article className="bg-white py-2 h-fit rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">Obra social</h3>
          <button className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer">
            <PencilIcon />
          </button>
        </header>

        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          <div>
            <label className="font-semibold text-blue-600">Nombre</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patient?.insuranceName}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Plan</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patient?.insurancePlan}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Número de afiliado
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patient?.memberShipNumber == null && "Particular"}
            </p>
          </div>
        </form>
      </article>
    </section>
  );
}

export default PatientInfoTab;
