import { FC, useState } from "react";
import { TreatmentNotesProps } from "../types/patientTabs.types";
import TabButton from "./TabButton";

const TreatmentNotes: FC<TreatmentNotesProps> = ({ patientNotesInfo }) => {
  const [activeTab, setActiveTab] = useState<string>("allergies");

  const renderContent = () => {
    switch (activeTab) {
      case "allergies":
        return (
          <div className="p-6">
            {patientNotesInfo.allergies.length > 0 ? (
              <ul className="capitalize list-disc pl-5 text-gray-700">
                {patientNotesInfo.allergies.map((allergy, index) => (
                  <li key={index}>{allergy}</li>
                ))}
              </ul>
            ) : (
              <p className="text-gray-700">No se han registrado alergias.</p>
            )}
          </div>
        );
      case "disabilities":
        return (
          <div className="p-6">
            {patientNotesInfo.disabilities.length > 0 ? (
              <ul className="list-disc pl-5 text-gray-700">
                {patientNotesInfo.disabilities.map((disability, index) => (
                  <li key={index}>{disability}</li>
                ))}
              </ul>
            ) : (
              <p className="text-gray-700">
                No se han registrado incapacidades.
              </p>
            )}
          </div>
        );
      default:
        return (
          <div className="p-6">
            Selecciona una pestaña para ver la información
          </div>
        );
    }
  };

  return (
    <div className="w-full mx-auto my-4 flex">
      <div className="flex flex-col gap-2 w-2/5 bg-white rounded-lg overflow-hidden p-4">
        <TabButton
          label="Alergias"
          isActive={activeTab === "allergies"}
          onClick={() => setActiveTab("allergies")}
        />
        <TabButton
          label="Incapacidad"
          isActive={activeTab === "disabilities"}
          onClick={() => setActiveTab("disabilities")}
        />
      </div>

      <div className="w-2/3 bg-white rounded-lg ml-4">{renderContent()}</div>
    </div>
  );
};

export default TreatmentNotes;
