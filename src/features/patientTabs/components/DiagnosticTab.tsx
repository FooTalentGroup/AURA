import { useState, useEffect } from "react";
import { PencilIcon } from "../../../components/shared/ui/Icons";
import {
  DiagnosticTabProps,
  PatientDiagnosesProps,
  updatedDiagnosis,
} from "../types/patientTabs.types";
import { formatDate } from "../utils/utils";
import { api } from "../../../core/services/api";

type EditableDiagnosis = updatedDiagnosis & {
  date: string;
  idProfessional: number;
};

interface DiagnosticTabEditableProps extends DiagnosticTabProps {
  onUpdate: (updated: PatientDiagnosesProps) => void;
}

export default function DiagnosticTab({
  diagnoses,
  onUpdate,
}: DiagnosticTabEditableProps) {
  const [isEditing, setIsEditing] = useState(false);
  const [form, setForm] = useState<EditableDiagnosis>({
    date: diagnoses?.date || "",
    idProfessional: diagnoses?.idProfessional || 0,
    title: diagnoses?.title || "",
    details: diagnoses?.details || "",
  });

  useEffect(() => {
    if (diagnoses) {
      setForm({
        date: diagnoses.date,
        idProfessional: diagnoses.idProfessional,
        title: diagnoses.title,
        details: diagnoses.details,
      });
    }
  }, [diagnoses]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: name === "idProfessional" ? Number(value) : value,
    }));
  };

  const save = async () => {
    if (!diagnoses) return;
    try {
      const updated = await api.updateDiagnosis(diagnoses.id, {
        title: form.title,
        details: form.details,
      });
      // Optionally update local date and professional id if backend returns
      onUpdate(updated);
      setIsEditing(false);
    } catch (error) {
      console.error("Error updating diagnóstico:", error);
    }
  };

  return (
    <section className="bg-gray-200/60 p-4 rounded-b-2xl rounded-t-2xl">
      <article className="h-fit w-3/6 bg-white py-2 rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">Diagnóstico</h3>
          {!isEditing && (
            <button
              onClick={() => setIsEditing(true)}
              className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer"
            >
              <PencilIcon />
            </button>
          )}
        </header>
        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          <div>
            <label className="font-semibold text-blue-600">Fecha</label>
            {isEditing ? (
              <input
                type="date"
                name="date"
                value={form.date.slice(0, 10)}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
                disabled
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {diagnoses?.date
                  ? formatDate(diagnoses.date)
                  : "No especificado"}
              </p>
            )}
          </div>

          <div>
            <label className="font-semibold text-blue-600">
              Id del Profesional
            </label>
            {isEditing ? (
              <input
                type="number"
                name="idProfessional"
                value={form.idProfessional}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
                disabled
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {diagnoses?.idProfessional || "No especificado"}
              </p>
            )}
          </div>

          <div className="col-span-2">
            <label className="font-semibold text-blue-600">Título</label>
            {isEditing ? (
              <input
                name="title"
                value={form.title}
                onChange={handleChange}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {diagnoses?.title || "No especificado"}
              </p>
            )}
          </div>

          <div className="col-span-2">
            <label className="font-semibold text-blue-600">Detalles</label>
            {isEditing ? (
              <textarea
                name="details"
                value={form.details}
                onChange={handleChange}
                rows={4}
                className="mt-2 p-2 border rounded w-full"
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {diagnoses?.details || "No especificado"}
              </p>
            )}
          </div>

          {isEditing && (
            <div className="col-span-2 flex justify-end gap-2 mt-4">
              <button
                type="button"
                onClick={() => setIsEditing(false)}
                className="px-4 py-2 border rounded hover:bg-gray-100"
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={save}
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
