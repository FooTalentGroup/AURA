import React from 'react';
import { Patient } from '../../../types/patient.ts';

interface Props {
    patient: Patient;
    onView: (id: string) => void;
  }
  
  export const PatientRow: React.FC<Props> = ({ patient, onView }) => (
    <div className="grid grid-cols-[2fr_1fr_1fr_1fr_2fr_1fr] items-center bg-white border border-gray-200 rounded-2xl px-4 py-4 mb-4">
      <div className="flex items-center space-x-4">
        <img
          src={patient.avatarUrl}
          alt={patient.name}
          className="w-12 h-12 rounded-full bg-gray-100"
        />
        <div className="text-sm">
          <p className="font-medium text-gray-800">{patient.name}</p>
          <p className="text-gray-500">{patient.age} años</p>
        </div>
      </div>
      <span className="text-center text-sm text-gray-600">{patient.dni}</span>
      <span className="text-center text-sm text-gray-600">{patient.nextSession}</span>
      <span className="text-center text-sm text-gray-600">{patient.lastSession}</span>
      <span className="text-center text-sm text-gray-600">{patient.contact}</span>
      <button
        onClick={() => onView(patient.id)}
        className="mx-auto text-[#65558F] border border-[#65558F] rounded-full px-4 py-1 text-sm hover:bg-[#65558F] hover:text-white transition"
      >
        Ver paciente
      </button>
    </div>
  );
