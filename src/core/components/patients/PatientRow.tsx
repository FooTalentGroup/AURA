import { Patient } from "../../../features/patients/types/patient.types";

interface Props {
  patient: Patient;
  onView: (id: number) => void;
}

export const PatientRow = ({ patient, onView }: Props) => {
  const getInitial = (name: string) => name.charAt(0).toUpperCase();

  // aquí podrías darle formato a las fechas con date-fns o dayjs
  const nextS = patient.nextSession || '—';
  const lastS = patient.lastSession || '—';

  return (
    <div className="grid grid-cols-[2fr_1fr_1fr_1fr_2fr_1fr] items-center bg-white rounded-xl shadow-sm px-4 py-3 mb-3">
      <div className="flex items-center space-x-4">
        <div className="w-10 h-10 rounded-full bg-blue-100 text-blue-700 flex items-center justify-center font-bold">
          {getInitial(patient.name)}
        </div>
        <div>
          <div className="font-medium text-gray-800">{patient.name} {patient.lastName}</div>
          <div className="text-xs text-gray-400">
            { /* calcula edad a partir de birthDate */ }
            {new Date().getFullYear() - new Date(patient.birthDate).getFullYear()} años
          </div>
        </div>
      </div>
      <span className="text-gray-700">{patient.dni}</span>
      <span className="text-gray-700">{nextS}</span>
      <span className="text-gray-700">{lastS}</span>
      <span className="text-gray-700">{patient.phoneNumber}</span>
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