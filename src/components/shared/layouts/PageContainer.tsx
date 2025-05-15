import React from "react";
import { FiSearch, FiUserPlus, FiUsers } from "react-icons/fi";
import DashboardLayout from "../../../layouts/DashboardLayout";
import { useContextAuth } from "../../../features/auth/hooks/useContextAuth";
import { useState } from "react";

interface PageContainerProps {
  title: string;
  description: string;
  query: string;
  onQueryChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onAdd: () => void;
  addLabel: string;
  children: React.ReactNode;
   count: number;    
}



/**
 * PageContainer envuelve el contenido en un layout centrado.
 * Usa la clase `container` de Tailwind para centrar el ancho.
 */
export const PageContainer:

React.FC<PageContainerProps> = ({
  title,
  query,
  description,
  onQueryChange,
  onAdd,
  addLabel,
   count,
  children
}) => {
  const { isAdmin, isProfessional} = useContextAuth();
  const [isFocused, setIsFocused] = useState(false);
  const bgClass = isFocused ? 'bg-white border border-gray-300 md:w-86 focus:ring-2 focus:ring-[#0F62FE14]' : 'bg-[#0F62FE14] md:w-86';
  const iconColor = isFocused ? 'text-gray-600' : 'text-[#0072C3]';
  return (
    <DashboardLayout>
      <div className="container mx-auto px-4 py-8 bg-gray-50 rounded-lg">
        {/* Header */}
        <div className="flex flex-col md:flex-row items-start md:items-center justify-between mb-4 pb-4 border-b border-gray-200">
          <div className="flex items-center space-x-2 mb-4 md:mb-0">
            <div className="flex items-center space-x-2 mb-4 md:mb-0 border-[#D9FBFB] rounded-xl border-4 bg-[#D9FBFB]">
            <FiUsers className="text-[#007D79] text-xl" />
            <span className="inline-flex items-center justify-center  text-[#007D79] font-medium text-sm px-2 py-0.5 rounded-full">
              {count}
            </span>
</div>
            <h1 className="text-2xl font-semibold text-gray-800">
              {title}
            </h1>
          </div>

          <div className="flex items-center space-x-4 w-full md:w-auto">
            <div className="relative w-full focus:md:w-96 ">
              <input
                type="text"
                value={query}
                onChange={onQueryChange}
                placeholder={`Buscá pacientes por ${description.toLowerCase()}`}
               onFocus={() => setIsFocused(true)}
        onBlur={() => setIsFocused(false)}
        className={`
          h-12 w-full rounded-full pl-12 pr-4 text-[#002D9C]
          outline-none transition-colors duration-200
          ${bgClass}
        `}
      />
              <FiSearch
        className={`absolute left-4 top-1/2 -translate-y-1/2 text-lg ${iconColor}`}
      />
            </div>
            {(isAdmin ) && (
              <button
                onClick={onAdd}
                className="cursor-pointer h-12 bg-[#0072C3] hover:bg-[#005A9E] text-white px-5 rounded-full flex items-center space-x-2 transition"
              >
                <FiUserPlus className="text-xl" />
                <span>{addLabel}</span>
              </button>
            )}
          </div>
        </div>

        {/* Content */}
        {children}
      </div>
    </DashboardLayout>
  );
};