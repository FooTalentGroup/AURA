import { PencilIcon } from "../../../components/shared/ui/Icons";
import { PatientTabsProps } from "../types/patientTabs.types";
import { api } from "../../../core/services/api";
import { useState, useEffect } from "react";
type FormData = {
  name: string;
  lastName: string;
  birthDate: string;
  dni: string;
  genre: string;
  insuranceName: string;
  insurancePlan: string;
  memberShipNumber: string | null;
};

function PatientInfoTab({
  patient,
  onUpdate,
}: PatientTabsProps & { onUpdate: (p: any) => void }) {
  const [formData, setFormData] = useState<FormData>({
    name: "",
    lastName: "",
    birthDate: "",
    dni: "",
    genre: "",
    insuranceName: "",
    insurancePlan: "",
    memberShipNumber: null,
  });

  const [isEditingPersonal, setIsEditingPersonal] = useState(false);
  const [isEditingInsurance, setIsEditingInsurance] = useState(false);

  useEffect(() => {
    if (!patient) return;
    setFormData({
      name: patient.name,
      lastName: patient.lastName,
      birthDate: patient.birthDate,
      dni: patient.dni,
      genre: patient.genre,
      insuranceName: patient.insuranceName,
      insurancePlan: patient.insurancePlan,
      memberShipNumber: patient.memberShipNumber,
    });
  }, [patient]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setFormData((f) => ({ ...f, [name]: value }));
  };

const handleSavePersonal = async () => {
  if (!patient?.id) return;
  const payload = {
    name: formData.name,
    lastName: formData.lastName,
    birthDate: formData.birthDate,
    dni: formData.dni,
    genre: formData.genre,
    insuranceName: patient.insuranceName ?? "",
    insurancePlan: patient.insurancePlan ?? "",
    memberShipNumber: patient.memberShipNumber ?? "",
    hasInsurance: patient.hasInsurance ?? false,
    relationToPatient: patient.relationToPatient ?? "",
    schoolId: patient.schoolId ?? null,
    professionalIds: patient.professionalIds ?? [],
    address: patient.address ?? "",
    phoneNumber: patient.phoneNumber ?? "",
    email: patient.email ?? "",
    tutorName: patient.tutorName ?? "",
    // agrega aquí cualquier otro campo requerido por el backend
  };
  const updated = await api.updatePatient(patient.id, payload);
  onUpdate(updated);
  setIsEditingPersonal(false);
};

const handleSaveInsurance = async () => {
  if (!patient?.id) return;
  const payload = {
    name: patient.name ?? "",
    lastName: patient.lastName ?? "",
    birthDate: patient.birthDate ?? "",
    dni: patient.dni ?? "",
    genre: patient.genre ?? "",
    insuranceName: formData.insuranceName,
    insurancePlan: formData.insurancePlan,
    memberShipNumber: formData.memberShipNumber ?? "",
    hasInsurance: patient.hasInsurance ?? false,
    relationToPatient: patient.relationToPatient ?? "",
    schoolId: patient.schoolId ?? null,
    professionalIds: patient.professionalIds ?? [],
    address: patient.address ?? "",
    phoneNumber: patient.phoneNumber ?? "",
    email: patient.email ?? "",
    tutorName: patient.tutorName ?? "",
    // agrega aquí cualquier otro campo requerido por el backend
  };
  const updated = await api.updatePatient(patient.id, payload);
  onUpdate(updated);
  setIsEditingInsurance(false);
};

  return (
    <section className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4 bg-gray-200/60 p-4 rounded-b-2xl rounded-tr-2xl">
      {/* Datos personales */}
      <article className="h-fit bg-white py-2 rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">
            Datos personales
          </h3>
          {!isEditingPersonal && (
            <button
              onClick={() => setIsEditingPersonal(true)}
              className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer"
            >
              <PencilIcon />
            </button>
          )}
        </header>

        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          {/* Nombre y Apellido */}
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Nombre y Apellido
            </label>
            {isEditingPersonal ? (
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {patient?.name} {patient?.lastName}
              </p>
            )}
          </div>

          {/* Fecha de nacimiento */}
          <div>
            <label className="font-semibold text-blue-600">
              Fecha de nacimiento
            </label>
            {isEditingPersonal ? (
              <input
                type="date"
                name="birthDate"
                value={formData.birthDate}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {patient?.birthDate}
              </p>
            )}
          </div>

          {/* DNI */}
          <div>
            <label className="font-semibold text-blue-600">DNI</label>
            {isEditingPersonal ? (
              <input
                type="text"
                name="dni"
                value={formData.dni}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {patient?.dni}
              </p>
            )}
          </div>

          {/* Sexo */}
          <div>
            <label className="font-semibold text-blue-600">Sexo</label>
            {isEditingPersonal ? (
              <select
                name="genre"
                value={formData.genre}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              >
                <option value="femenino">Femenino</option>
                <option value="masculino">Masculino</option>
              </select>
            ) : (
              <p className="capitalize py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {patient?.genre}
              </p>
            )}
          </div>

          {/* Guardar/Cancelar Datos personales */}
          {isEditingPersonal && (
            <div className="col-span-2 flex justify-end gap-2 mt-4">
              <button
                type="button"
                onClick={() => setIsEditingPersonal(false)}
                className="px-4 py-2 border rounded hover:bg-gray-100"
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={handleSavePersonal}
                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
              >
                Guardar
              </button>
            </div>
          )}
        </form>
      </article>

      {/* Obra social */}
      <article className="bg-white py-2 h-fit rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">Obra social</h3>
          {!isEditingInsurance && (
            <button
              onClick={() => setIsEditingInsurance(true)}
              className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer"
            >
              <PencilIcon />
            </button>
          )}
        </header>

        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          {/* Nombre */}
          <div>
            <label className="font-semibold text-blue-600">Nombre</label>
            {isEditingInsurance ? (
              <input
                type="text"
                name="insuranceName"
                value={formData.insuranceName}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {patient?.insuranceName}
              </p>
            )}
          </div>

          {/* Plan */}
          <div>
            <label className="font-semibold text-blue-600">Plan</label>
            {isEditingInsurance ? (
              <input
                type="text"
                name="insurancePlan"
                value={formData.insurancePlan}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {patient?.insurancePlan}
              </p>
            )}
          </div>

          {/* Número de afiliado */}
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Número de afiliado
            </label>
            {isEditingInsurance ? (
              <input
                type="text"
                name="memberShipNumber"
                value={formData.memberShipNumber ?? ""}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {patient?.memberShipNumber ?? "Particular"}
              </p>
            )}
          </div>

          {/* Guardar/Cancelar Obra social */}
          {isEditingInsurance && (
            <div className="col-span-2 flex justify-end gap-2 mt-4">
              <button
                type="button"
                onClick={() => setIsEditingInsurance(false)}
                className="px-4 py-2 border rounded hover:bg-gray-100"
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={handleSaveInsurance}
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

export default PatientInfoTab;