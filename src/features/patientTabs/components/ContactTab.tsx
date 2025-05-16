import { useState, useEffect } from "react";
import { PencilIcon } from "../../../components/shared/ui/Icons";
import { ContactTabProps } from "../types/patientTabs.types";
import { api } from "../../../core/services/api";

type ContactForm = {
  tutorName: string;
  relationToPatient: string;
  phoneNumber: string;
  address: string;
  email: string;
  schoolName: string;
  emailSchool: string;
  phoneSchool: string;
};

function ContactTab({
  patient,
  school,
  onUpdatePatient,
  onUpdateSchool,
}: ContactTabProps & {
  onUpdatePatient: (p: any) => void;
  onUpdateSchool: (s: any) => void;
}) {
  const [formData, setFormData] = useState<ContactForm>({
    tutorName: "",
    relationToPatient: "",
    phoneNumber: "",
    address: "",
    email: "",
    schoolName: "",
    emailSchool: "",
    phoneSchool: "",
  });

  const [isEditingTutor, setIsEditingTutor] = useState(false);
  const [isEditingInstitution, setIsEditingInstitution] = useState(false);

  useEffect(() => {
    if (!patient || !school) return;
    setFormData({
      tutorName: patient.tutorName,
      relationToPatient: patient.relationToPatient,
      phoneNumber: patient.phoneNumber,
      address: patient.address,
      email: patient.email,
      schoolName: school.schoolName,
      emailSchool: school.emailSchool,
      phoneSchool: school.phoneSchool,
    });
  }, [patient, school]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { name, value } = e.target;
    setFormData((f) => ({ ...f, [name]: value }));
  };

const saveTutor = async () => {
  if (!patient || !patient.id) return;
  const payload = {
    name: patient.name ?? "",
    lastName: patient.lastName ?? "",
    birthDate: patient.birthDate ?? "",
    dni: patient.dni ?? "",
    genre: patient.genre ?? "",
    insuranceName: patient.insuranceName ?? "",
    insurancePlan: patient.insurancePlan ?? "",
    memberShipNumber: patient.memberShipNumber ?? "",
    hasInsurance: patient.hasInsurance ?? false,
    relationToPatient: formData.relationToPatient,
    schoolId: patient.schoolId ?? null,
    professionalIds: patient.professionalIds ?? [],
    address: formData.address,
    phoneNumber: formData.phoneNumber,
    email: formData.email,
    tutorName: formData.tutorName,
    // agrega aquí cualquier otro campo requerido por el backend
  };
  const updated = await api.updatePatient(patient.id, payload);
  onUpdatePatient(updated);
  setIsEditingTutor(false);
};

const saveInstitution = async () => {
  if (!school || !school.id) return;
  const updated = await api.updateSchool(school.id, {
    schoolName: formData.schoolName,
    emailSchool: formData.emailSchool,
    phoneSchool: formData.phoneSchool,
  });
  onUpdateSchool(updated);
  setIsEditingInstitution(false);
};

  return (
    <section className="grid grid-cols-3 gap-4 bg-gray-200/60 p-4 rounded-b-2xl rounded-t-2xl">
      {/* Tutor 1 */}
      <article className="h-fit bg-white py-2 rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">Tutor 1</h3>
          {!isEditingTutor && (
            <button
              onClick={() => setIsEditingTutor(true)}
              className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer"
            >
              <PencilIcon />
            </button>
          )}
        </header>

        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          {/* Nombre y apellido */}
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Nombre y apellido
            </label>
            {isEditingTutor ? (
              <input
                name="tutorName"
                value={formData.tutorName}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {patient?.tutorName}
              </p>
            )}
          </div>

          {/* Relación */}
          <div>
            <label className="font-semibold text-blue-600">Relación</label>
            {isEditingTutor ? (
              <input
                name="relationToPatient"
                value={formData.relationToPatient}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {patient?.relationToPatient}
              </p>
            )}
          </div>

          {/* Teléfono */}
          <div>
            <label className="font-semibold text-blue-600">Teléfono</label>
            {isEditingTutor ? (
              <input
                name="phoneNumber"
                value={formData.phoneNumber}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {patient?.phoneNumber}
              </p>
            )}
          </div>

          {/* Domicilio */}
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">Domicilio</label>
            {isEditingTutor ? (
              <input
                name="address"
                value={formData.address}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {patient?.address}
              </p>
            )}
          </div>

          {/* Correo electrónico */}
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Correo electrónico
            </label>
            {isEditingTutor ? (
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {patient?.email}
              </p>
            )}
          </div>

          {/* Botones Tutor */}
          {isEditingTutor && (
            <div className="col-span-2 flex justify-end gap-2 mt-4">
              <button
                type="button"
                onClick={() => setIsEditingTutor(false)}
                className="px-4 py-2 border rounded hover:bg-gray-100"
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={saveTutor}
                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
              >
                Guardar
              </button>
            </div>
          )}
        </form>
      </article>

      {/* Institución */}
      <article className="bg-white py-2 h-fit rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">Institución</h3>
          {!isEditingInstitution && (
            <button
              onClick={() => setIsEditingInstitution(true)}
              className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer"
            >
              <PencilIcon />
            </button>
          )}
        </header>

        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          {/* Nombre */}
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">Nombre</label>
            {isEditingInstitution ? (
              <input
                name="schoolName"
                value={formData.schoolName}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {school?.schoolName}
              </p>
            )}
          </div>

          {/* Correo electrónico */}
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Correo electrónico
            </label>
            {isEditingInstitution ? (
              <input
                type="email"
                name="emailSchool"
                value={formData.emailSchool}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {school?.emailSchool}
              </p>
            )}
          </div>

          {/* Teléfono */}
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">Teléfono</label>
            {isEditingInstitution ? (
              <input
                name="phoneSchool"
                value={formData.phoneSchool}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {school?.phoneSchool}
              </p>
            )}
          </div>

          {/* Botones Institución */}
          {isEditingInstitution && (
            <div className="col-span-2 flex justify-end gap-2 mt-4">
              <button
                type="button"
                onClick={() => setIsEditingInstitution(false)}
                className="px-4 py-2 border rounded hover:bg-gray-100"
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={saveInstitution}
                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
              >
                Guardar
              </button>
            </div>
          )}
        </form>
      </article>
    </section>
  );
}

export default ContactTab;