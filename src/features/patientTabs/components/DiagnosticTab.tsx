import { useState, useEffect } from "react";
import { PencilIcon } from "../../../components/shared/ui/Icons";
import { DiagnosticTabEditableProps } from "../types/patientTabs.types";
import { formatDate } from "../utils/utils";
import { api } from "../../../core/services/api";
import { useContextAuth } from "../../auth/hooks/useContextAuth";

export default function DiagnosticTab({
  diagnoses,
  onUpdate,
}: DiagnosticTabEditableProps) {
  const {
    state: { user },
  } = useContextAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [form, setForm] = useState<{ title: string; details: string }>({
    title: diagnoses?.title || "",
    details: diagnoses?.details || "",
  });

  useEffect(() => {
    if (diagnoses) {
      setForm({
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
      [name]: value,
    }));
  };

  const save = async () => {
    if (!diagnoses || !user) {
      setError(
        "Error: No se puede actualizar el diagnóstico sin la sesión del usuario"
      );
      return;
    }

    if (!user.roles?.some((role) => ["ADMIN", "PROFESSIONAL"].includes(role))) {
      setError("No tiene los permisos necesarios para actualizar diagnósticos");
      return;
    }

    const sanitizedTitle = (form.title || "").trim();
    const sanitizedDetails = (form.details || "").trim();

    if (!sanitizedTitle) {
      setError("El título es requerido");
      return;
    }

    const payload = {
      title: sanitizedTitle,
      details: sanitizedDetails,
    };

    setIsLoading(true);
    setError(null);

    const maxRetries = 3;

    try {
      for (let i = 0; i < maxRetries; i++) {
        try {
          if (i > 0) {
            await new Promise((resolve) => setTimeout(resolve, 1000 * i));
          }

          const updated = await api.updateDiagnosis(diagnoses.id, payload);
          console.log("Respuesta exitosa:", updated);
          onUpdate(updated);
          setIsEditing(false);
          return;
        } catch (err) {
          if (i === maxRetries - 1) {
            console.error("Error al actualizar el diagnóstico:", err);
            throw err;
          }
        }
      }
    } catch (err) {
      const error = err as Error;
      let errorMessage = "Error actualizando el diagnóstico.";

      if (error.message) {
        if (error.message.includes("400")) {
          errorMessage =
            "Error en el formato de los datos. Por favor, verifique la información ingresada.";
        } else if (error.message.toLowerCase().includes("inválido")) {
          errorMessage =
            "Los datos ingresados no son válidos. Por favor, revise la información.";
        } else {
          errorMessage += ` ${error.message}`;
        }
      }

      setError(`${errorMessage} Por favor, intente nuevamente.`);
    } finally {
      setIsLoading(false);
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
                // value={form.date.slice(0, 10)}
                value={diagnoses?.date ? diagnoses.date.slice(0, 10) : ""}
                onChange={handleChange}
                className="mt-2 p-2 rounded w-full text-gray-600 bg-blue-50/80"
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
            <label className="font-semibold text-blue-600">Profesional</label>
            {isEditing ? (
              <input
                type="text"
                name="idProfessional"
                value={`${diagnoses?.professionalName || ""} ${
                  diagnoses?.professionalLastName || ""
                }`}
                onChange={handleChange}
                className="mt-2 p-2 rounded w-full text-gray-600 bg-blue-50/80 "
                disabled
              />
            ) : (
              <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
                {diagnoses?.professionalName && diagnoses?.professionalLastName
                  ? `${diagnoses.professionalName} ${diagnoses.professionalLastName}`
                  : "No especificado"}
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
          {error && (
            <div className="col-span-2 mt-2">
              <p className="text-red-500 text-sm">{error}</p>
            </div>
          )}
          {isEditing && (
            <div className="col-span-2 flex justify-end gap-2 mt-4">
              <button
                type="button"
                onClick={() => {
                  setIsEditing(false);
                  setError(null);
                }}
                className="px-4 py-2 border rounded hover:bg-gray-100"
                disabled={isLoading}
              >
                Cancelar
              </button>
              <button
                type="button"
                onClick={save}
                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:bg-blue-400"
                disabled={isLoading}
              >
                {isLoading ? "Guardando..." : "Guardar"}
              </button>
            </div>
          )}
        </form>
      </article>
    </section>
  );
}
