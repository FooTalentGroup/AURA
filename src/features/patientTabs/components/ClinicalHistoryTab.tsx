import { ClinicalHistoryTabProps } from "../types/patientTabs.types";
import AppointmentTable from "./AppointmentTable";

function ClinicalHistoryTab({
  medicalFilters,
  followEntries,
  onSetAppointmentId,
}: ClinicalHistoryTabProps) {
  return (
    <section className="bg-gray-200/60 flex gap-4 p-4 rounded-b-2xl rounded-t-2xl">
      <AppointmentTable
        appointments={medicalFilters}
        onSetAppointmentId={onSetAppointmentId}
      />
      <article className="bg-white py-2 w-full border border-gray-400">
        <form className="text-sm px-4 pb-2">
          <div className="p-4 rounded-md">
            <div className="mb-6">
              <label className="font-semibold">Observaciones</label>
              <p className="text-gray-700 mt-2">
                {followEntries?.observations}
              </p>
            </div>

            <div className="mb-6">
              <label className="font-semibold">Intervenciones realizadas</label>
              <div className="mt-2">
                <p className="text-gray-700 mt-2">
                  {followEntries?.interventions}
                </p>
              </div>
            </div>

            <div>
              <label className="font-semibold">
                Indicaciones para la próxima sesión
              </label>
              <div className="mt-2">
                <p className="text-gray-700 mt-2">
                  {followEntries?.nextSessionInstructions}
                </p>
              </div>
            </div>
          </div>
        </form>
      </article>
    </section>
  );
}

export default ClinicalHistoryTab;
