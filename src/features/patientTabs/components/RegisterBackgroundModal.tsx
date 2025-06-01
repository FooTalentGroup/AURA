import { useState, useEffect } from "react";
import { Modal } from "../../../layouts/Modal";
import { api } from "../../../core/services/api";
import { PatientNotesInfo } from "../types/patientTabs.types";

interface Props {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void;
  patientId: number;
}

export default function RegisterBackgroundModal({
  isOpen,
  onClose,
  onSuccess,
  patientId,
}: Props) {
  const [category, setCategory] = useState("");
  const [allergyDetail, setAllergyDetail] = useState("");
  const [disabilityType, setDisabilityType] = useState("");
  const [disabilityDetail, setDisabilityDetail] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [initialData, setInitialData] = useState<PatientNotesInfo | null>(null);
  const [backgroundId, setBackgroundId] = useState<number | null>(null);

  useEffect(() => {
    if (isOpen && patientId) {
      setLoading(true);
      setError(null); // Limpiar error al abrir
      api.getMedicalBackgroundsById(patientId)
        .then((data: PatientNotesInfo) => {
          setInitialData(data);
          setBackgroundId(data.id);
        })
        .catch((err) => {
          if (err?.errorCode === "RESOURCE-404" || (typeof err?.message === "string" && err.message.includes("no encontrado"))) {
            setInitialData(null);
            setBackgroundId(null);
            setError(null); // No mostrar error si no hay antecedentes
          } else {
            setError("Error al cargar antecedentes");
          }
        })
        .finally(() => setLoading(false));
    }
  }, [isOpen, patientId]);

  useEffect(() => {
    if (initialData) {
      // Si hay datos previos, los mostramos en los campos
      if (initialData.allergies.length > 0) {
        const [cat, ...rest] = initialData.allergies[0].split(": ");
        setCategory(cat || "");
        setAllergyDetail(rest.join(": ") || "");
      }
      if (initialData.disabilities.length > 0) {
        const [type, ...rest] = initialData.disabilities[0].split(": ");
        setDisabilityType(type || "");
        setDisabilityDetail(rest.join(": ") || "");
      }
    } else {
      setCategory("");
      setAllergyDetail("");
      setDisabilityType("");
      setDisabilityDetail("");
    }
  }, [initialData, isOpen]);

  const isEdit = backgroundId !== null;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      if (isEdit && backgroundId) {
        await api.updateMedicalBackgroundById(backgroundId, {
          patientId,
          allergies: category && allergyDetail ? [`${category}: ${allergyDetail}`] : [],
          disabilities: disabilityType && disabilityDetail ? [`${disabilityType}: ${disabilityDetail}`] : [],
        });
      } else {
        await api.createMedicalBackground({
          patientId,
          allergies: category && allergyDetail ? [`${category}: ${allergyDetail}`] : [],
          disabilities: disabilityType && disabilityDetail ? [`${disabilityType}: ${disabilityDetail}`] : [],
        });
      }
      onSuccess();
    } catch (err) {
      setError((err as Error).message || "Error al guardar");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <h2 className="text-lg font-semibold mb-4">
        {isEdit ? "Editar antecedentes" : "Nuevo registro de antecedente"}
      </h2>
      <form className="flex flex-col gap-4" onSubmit={handleSubmit}>
        <div>
          <label className="block font-medium mb-1">Alergias</label>
          <select
            className="w-full border rounded p-2 mb-2"
            value={category}
            onChange={e => setCategory(e.target.value)}
          >
            <option value="">Categoría</option>
            <option value="Alimentos">Alimentos</option>
            <option value="Ambiental">Ambiental</option>
          </select>
          <input
            className="w-full border rounded p-2"
            placeholder="Detalle"
            value={allergyDetail}
            onChange={e => setAllergyDetail(e.target.value)}
          />
        </div>
        <div>
          <label className="block font-medium mb-1">Incapacidad</label>
          <select
            className="w-full border rounded p-2 mb-2"
            value={disabilityType}
            onChange={e => setDisabilityType(e.target.value)}
          >
            <option value="">Tipo</option>
            <option value="Motora">Motora</option>
            <option value="Sensorial">Sensorial</option>
            <option value="Otra">Otra</option>
          </select>
          <input
            className="w-full border rounded p-2"
            placeholder="Detalle"
            value={disabilityDetail}
            onChange={e => setDisabilityDetail(e.target.value)}
          />
        </div>
        {error && <p className="text-red-600 text-sm">{error}</p>}
        <div className="flex justify-end gap-2 mt-2">
          <button
            type="button"
            onClick={onClose}
            className="px-4 py-2 border rounded hover:bg-gray-100"
            disabled={loading}
          >
            Cancelar
          </button>
          <button
            type="submit"
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
            disabled={loading}
          >
            {loading ? "Guardando..." : "Guardar"}
          </button>
        </div>
      </form>
    </Modal>
  );
}
