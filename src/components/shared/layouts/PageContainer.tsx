import React from "react";
import { FiSearch, FiUserPlus } from "react-icons/fi";
import DashboardLayout from "../../../layouts/DashboardLayout";
import { useContextAuth } from "../../../features/auth/hooks/useContextAuth";

interface PageContainerProps {
  title: string;
  description: string;
  query: string;
  onQueryChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onAdd: () => void;
  addLabel: string;
  children: React.ReactNode;
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
  children
}) => {
  const { isAdmin, isProfessional} = useContextAuth();
  return (
    
    <DashboardLayout>
    <div className="container mx-auto px-4 py-8 bg-gray-50">
      {/* Header */}
      <div className="flex flex-col md:flex-row items-start md:items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold text-gray-800 mb-4 md:mb-0">
          {title}
        </h1>
        <div className="flex items-center space-x-4 w-full md:w-auto">
          <div className="relative w-full md:w-64">
            <input
              type="text"
              value={query}
              onChange={onQueryChange}
              placeholder={`Buscar ${description.toLowerCase()}...`}
              className="h-12 w-full rounded-full border border-gray-300 pl-12 pr-4 focus:outline-none focus:ring-2 focus:ring-[#0072C3]"
              />
            <FiSearch className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 text-lg" />
          </div>
          {isAdmin || isProfessional  ? (<button
            onClick={onAdd}
            className="h-12 bg-[#0072C3] hover:bg-[#005A9E] text-white px-5 rounded-full flex items-center space-x-2 transition"
            >
            <FiUserPlus className="text-xl" />
            <span>{addLabel}</span>
          </button> ) : null}
        </div>
      </div>

      {/* Content */}
      {children}
    </div>
  </DashboardLayout>
);
};
