import { Patient } from "../../../features/patients/types/patient.types";

interface Props {
  patient: Patient;
  onView: (id: number) => void;
}

export const PatientRow: React.FC<Props> = ({ patient, onView }) => {
  const initial =
    patient.name.charAt(0).toUpperCase() +
    patient.lastName.charAt(0).toUpperCase();
  const nextS = patient.nextSession || "—";
  const lastS = patient.lastSession || "—";
  const age =
    new Date().getFullYear() - new Date(patient.birthDate).getFullYear();

  return (
    <div className="grid grid-cols-[2fr_1fr_1fr_1fr_1fr_1fr_1fr_1fr] items-center bg-white rounded-xl shadow-sm px-4 py-3 mb-3">
      {/* 1. Paciente + edad */}
      <div className="flex items-center space-x-4">
        <div className="w-10 h-10 rounded-full bg-blue-100 text-blue-700 flex items-center justify-center font-bold">
          {initial}
        </div>
        <div>
          <p className="font-medium text-gray-800">
            {patient.name} {patient.lastName}
          </p>
          <p className="text-xs text-gray-400">{age} años</p>
        </div>
      </div>

      {/* 2. DNI + botón Ver (HC) */}

      <span className="text-gray-700">{patient.dni}</span>

      {/* 3. Próx. sesión */}
      <span className="text-gray-700">{nextS}</span>

      {/* 4. Última sesión */}
      <span className="text-gray-700">{lastS}</span>

      {/* 5. Contacto (tutorName) */}
      <span className="text-gray-700">{patient.tutorName}</span>

      {/* 6. Relación (tutorRelation) */}
      <span className="text-gray-700">{patient.tutorRelation}</span>

      {/* 7. Teléfono */}
      <span className="text-gray-700">{patient.phoneNumber}</span>

      {/* 8. Acción principal */}
      <div className="flex justify-end">
        <button
          onClick={() => onView(patient.id)}
          className="text-[#0072C3] border border-[#0072C3] rounded-full px-4 py-1 text-sm font-medium hover:bg-[#0072C3] hover:text-white transition"
        >
          Ver Paciente
        </button>
      </div>
    </div>
  );
};
