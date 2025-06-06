import React from "react";
import { FiSearch,  FiUsers, FiX } from "react-icons/fi";
import DashboardLayout from "../../../layouts/DashboardLayout";
import { useContextAuth } from "../../../features/auth/hooks/useContextAuth";
import { useState } from "react";
import { useLocation } from 'react-router-dom';

interface PageContainerProps {
  title: string;
  description: string;
  query: string;
  onQueryChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onAdd: () => void;
  addLabel: string;
  count?: number;    
  children: React.ReactNode;
}



/**
 * PageContainer envuelve el contenido en un layout centrado.
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
  const { isAdmin} = useContextAuth();
  const [isFocused, setIsFocused] = useState(false);
  const bgClass = isFocused ? 'bg-white border text-gray-800 border-gray-300 md:w-86 focus:ring-2 focus:ring-[#0F62FE14]' : 'bg-[#0F62FE14] md:w-86 ';
  const iconColor = isFocused ? 'text-gray-600' : 'text-[#0072C3]';
    const location = useLocation();
  const isPatientPage = location.pathname === '/patients'
    || location.pathname.startsWith('/patients');
  return (
    <DashboardLayout>
      <div className="  px-4 py-8 bg-gray-50 rounded-[2rem]">
        {/* Header */}
        <div className="flex flex-col md:flex-row items-start md:items-center justify-between mb-4 pb-4 border-b border-gray-200">
          <div className="flex items-center space-x-2 mb-4 md:mb-0">
        {isPatientPage&&(   <div className="flex items-center space-x-2 mb-4 md:mb-0 border-[#D9FBFB] rounded-xl border-4 bg-[#D9FBFB]">
            <FiUsers className="text-[#007D79] text-xl" />
            <span className="inline-flex items-center justify-center  text-[#007D79] font-medium text-sm px-2 py-0.5 rounded-full">
              {count}
            </span>
</div>)} 
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
              {isFocused && (
                <FiX
                  className="absolute right-4 top-1/2 -translate-y-1/2 text-lg text-gray-800 cursor-pointer hover:text-black"
                  onMouseDown={e => { e.preventDefault(); onQueryChange({ target: { value: '' } } as React.ChangeEvent<HTMLInputElement>); }}
                />
              )}
            </div>
            {(isAdmin ) && (
              <button
                onClick={onAdd}
                className="cursor-pointer h-12 bg-[#0072C3] hover:bg-[#005A9E] text-white px-5 rounded-full flex items-center space-x-2 transition"
              >
                <span className="whitespace-nowrap">{addLabel}</span>
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