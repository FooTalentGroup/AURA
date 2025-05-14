import { PencilIcon } from "../../../components/shared/ui/Icons";
import { ContactTabProps } from "../types/patientTabs.types";

function ContactTab({ patient, school }: ContactTabProps) {
  return (
    <section className="grid grid-cols-3 gap-4 bg-gray-200/60 p-4 rounded-b-2xl rounded-t-2xl">
      <article className="h-fit bg-white py-2 rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">Tutor 1</h3>
          <button className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer">
            <PencilIcon />
          </button>
        </header>
        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2" action="">
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Nombre y apellido
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patient.tutorName}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Relación</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patient.relationToPatient}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Teléfono</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patient.phoneNumber}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">Domicilio</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patient.address}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Correo electrónico
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patient.email}
            </p>
          </div>
        </form>
      </article>

      <article className="bg-white py-2 h-fit rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">Institución</h3>
          <button className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer">
            <PencilIcon />
          </button>
        </header>

        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">Nombre</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {school.schoolName}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Correo electrónico
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {school.emailSchool}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">Teléfono</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {school.phoneSchool}
            </p>
          </div>
        </form>
      </article>
    </section>
  );
}

export default ContactTab;
