import { useEffect, useState } from "react";
import { Modal } from "../../../layouts/Modal";
import { useProfessionalData } from "../hooks/useProfessional";
import { api } from "../../../core/services/api";

interface Props {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void;
  patientId: number;
}

export default function RegisterClinicalRecordModal({
  isOpen,
  onClose,
  onSuccess,
  patientId,
}: Props) {
  //  Datos del profesional
  const {
    professional,
    loading: loadingProf,
    error: errorProf,
  } = useProfessionalData();

  //  Datos de la historia clínica
  const [medicalRecordId, setMedicalRecordId] = useState<number | null>(null);
  const [loadingRecord, setLoadingRecord] = useState(false);
  const [errorRecord, setErrorRecord] = useState<string | null>(null);

  useEffect(() => {
    if (!isOpen || !professional) return;
    setLoadingRecord(true);
    setErrorRecord(null);

    api
      .getMedicalRecordByPatientId(patientId)
      .then((record) => {
        setMedicalRecordId(record.id);
      })
      .catch(async (err) => {
        // Mejor manejo del error para detectar 404
        const is404 =
          err?.response?.status === 404 ||
          err?.status === 404 ||
          (err.message && err.message.toLowerCase().includes("404"));

        if (is404) {
          try {
            const newRecord = await api.createMedicalRecord({
              patientId,
            });
            setMedicalRecordId(newRecord.id);
          } catch (createErr) {
            console.error("Error al crear historia clínica:", createErr);
            let msg = "No se pudo crear la historia clínica";
            if (createErr instanceof Error) msg = createErr.message;
            setErrorRecord(msg);
          }
        } else {
          console.error("Error al cargar historia clínica:", err);
          let msg = "No se pudo cargar la historia clínica";
          if (err instanceof Error && err.message) msg = err.message;
          setErrorRecord(msg);
        }
      })
      .finally(() => {
        setLoadingRecord(false);
      });
  }, [isOpen, patientId, professional]);

  //  Formulario y envío
  const [observations, setObservations] = useState("");
  const [interventions, setInterventions] = useState("");
  const [indications, setIndications] = useState("");

  const [submitting, setSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!professional || medicalRecordId == null) return;

    setSubmitting(true);
    setSubmitError(null);

    try {
      await api.createFollowUpEntry({
        medicalRecordId,
        observations,
        interventions,
        nextSessionInstructions: indications,
      });
      onSuccess();
    } catch (err: unknown) {
      if (err instanceof Error) {
        setSubmitError(err.message || "Error al guardar el registro clínico");
      } else {
        setSubmitError("Error desconocido");
      }
    } finally {
      setSubmitting(false);
    }
  };

  //  Render de loaders/errores
  if (loadingProf || loadingRecord) {
    return (
      <Modal isOpen={isOpen} onClose={onClose}>
        Cargando datos...
      </Modal>
    );
  }

  if (errorProf || errorRecord || !professional || medicalRecordId == null) {
    return (
      <Modal isOpen={isOpen} onClose={onClose}>
        <p className="text-red-600">
          {errorProf || errorRecord || "Datos incompletos"}
        </p>
      </Modal>
    );
  }

  //  Render principal
  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <h2 className="text-xl font-semibold mb-4">Nuevo registro clínico</h2>
      <p className="text-sm text-gray-700 mb-4">
        Profesional:{" "}
        <strong>
          {professional.name} {professional.lastName}
        </strong>{" "}
        | Especialidad: <strong>{professional.specialty}</strong>
      </p>

      <form className="flex flex-col gap-4" onSubmit={handleSubmit}>
        <label className="block">
          <span className="font-medium">Observaciones</span>
          <textarea
            className="mt-1 w-full border rounded p-2"
            rows={3}
            value={observations}
            onChange={(e) => setObservations(e.target.value)}
            required
          />
        </label>

        <label className="block">
          <span className="font-medium">Intervenciones</span>
          <textarea
            className="mt-1 w-full border rounded p-2"
            rows={3}
            value={interventions}
            onChange={(e) => setInterventions(e.target.value)}
            required
          />
        </label>

        <label className="block">
          <span className="font-medium">Indicaciones para próxima sesión</span>
          <textarea
            className="mt-1 w-full border rounded p-2"
            rows={3}
            value={indications}
            onChange={(e) => setIndications(e.target.value)}
            required
          />
        </label>

        {submitError && <p className="text-red-600 text-sm">{submitError}</p>}

        <div className="flex justify-end gap-3 mt-2">
          <button
            type="button"
            onClick={onClose}
            className="bg-gray-300 text-gray-800 px-4 py-2 rounded"
            disabled={submitting}
          >
            Cancelar
          </button>
          <button
            type="submit"
            className="bg-blue-600 text-white px-4 py-2 rounded"
            disabled={submitting}
          >
            {submitting ? "Guardando..." : "Guardar"}
          </button>
        </div>
      </form>
    </Modal>
  );
}
