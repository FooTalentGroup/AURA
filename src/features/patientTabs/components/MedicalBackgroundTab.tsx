import { MedicalBackgroundTabProps } from "../types/patientTabs.types";
import TreatmentNotes from "./TreatmentNotes";

function MedicalBackgroundTab({
  medicalBackgrounds,
}: MedicalBackgroundTabProps) {
  return (
    <div className="bg-gray-200/60 py-1 px-4 rounded-b-2xl rounded-t-2xl">
      <TreatmentNotes patientNotesInfo={medicalBackgrounds} />
    </div>
  );
}

export default MedicalBackgroundTab;
