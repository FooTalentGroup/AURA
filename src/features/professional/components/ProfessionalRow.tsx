import React from 'react';
import { Professional } from '../types/Professional.types';

interface Props {
  professional: Professional;
  onView: (id: number) => void; 
  onViewUser?: (id: number) => void; 
  onViewSchedule: (id: number) => void; 
}

export const ProfessionalRow: React.FC<Props> = ({ professional, onViewSchedule, onView }) => {
  const getInitial = (name: string) => name.charAt(0).toUpperCase();

  return (
    <div className="grid grid-cols-[2fr_1fr_1fr_1fr_1fr_1fr] items-center bg-white rounded-xl shadow-sm px-4 py-3 mb-3">
      <div className="flex items-center space-x-4">
        <div className="w-10 h-10 rounded-full bg-green-100 text-green-700 flex items-center justify-center font-bold">
          {getInitial(professional.name)}
        </div>
        <div>
          <div className="font-medium text-gray-800">
            {professional.name} {professional.lastName}
          </div>
          <div className="text-xs text-gray-400">
            {professional.specialty}
          </div>
        </div>
      </div>
      <span className="text-gray-700">{professional.dni}</span>
      <span className="text-gray-700">{professional.specialty}</span>
      <span className="text-gray-700">{professional.phoneNumber}</span>
      <span className="text-gray-700">{professional.email}</span>
      <div className="flex justify-end space-x-2">
        <button
          onClick={() => onViewSchedule(professional.id)}
          className="text-[#0072C3] border border-[#0072C3] rounded-full px-3 py-1 text-sm font-medium hover:bg-[#0072C3] hover:text-white transition"
        >
          Ver Horario
        </button>
        <button
          onClick={() =>  onView(professional.id)}
          className="text-[#0072C3] border border-[#0072C3] rounded-full px-3 py-1 text-sm font-medium hover:bg-[#0072C3] hover:text-white transition"
        >
          Ver Usuario
        </button>
      </div>
    </div>
  );
};
