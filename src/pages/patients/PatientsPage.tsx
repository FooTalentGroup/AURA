
import DashboardLayout from "../../layouts/DashboardLayout";
import { usePatients } from '../../core/hooks/patients/usePatients';
import { PatientRow } from '../../core/components/patients/PatientRow.tsx';
import { FiSearch, FiUserPlus } from 'react-icons/fi';
import { IconType } from 'react-icons';
import { useState } from "react";

const Icons: Record<string, IconType> = {
  search: FiSearch,
  addPatient: FiUserPlus,
};
const PatientsPage = () => {
  const { patients, loading } = usePatients();
  const [query, setQuery] = useState('');
  const SearchIcon = Icons.search;
  const AddIcon = Icons.addPatient;

  const filtered = patients.filter(p =>
    p.name.toLowerCase().includes(query.toLowerCase()) ||
    p.dni.includes(query)
  );

  const handleView = (id: string) => {
    console.log('Ver paciente', id);
  };


  return (
    <DashboardLayout>
     <div className="max-w-[100rem] mx-auto px-2 py-8 bg-gray-50">
        {/* Header */}
        <div className="flex flex-col md:flex-row items-start md:items-center justify-between mb-6">
          <h1 className="text-2xl font-semibold text-gray-800 mb-4 md:mb-0">Lista de pacientes</h1>
          <div className="flex items-center space-x-4 w-full md:w-auto">
            <div className="relative w-full md:w-64">
              <input
                type="text"
                value={query}
                onChange={e => setQuery(e.target.value)}
                placeholder="Buscar..."
                className="h-12 w-full rounded-full border border-gray-300 pl-12 pr-4 focus:outline-none focus:ring-2 focus:ring-[#65558F]"
              />
              <SearchIcon className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 text-lg" />
            </div>
            <button className="h-12 bg-[#65558F] hover:bg-opacity-90 text-white px-5 rounded-full flex items-center space-x-2 transition">
              <AddIcon className="text-xl" />
              <span>Agregar paciente</span>
            </button>
          </div>
        </div>

        {/* Column Headers */}
        <div className="grid grid-cols-[2fr_1fr_1fr_1fr_2fr_1fr] text-sm text-gray-500 mb-4 px-4">
          <span>Paciente</span>
          <span>DNI</span>
          <span>Próxima sesión</span>
          <span>Última sesión</span>
          <span>Contacto</span>
          <span> Historia clinica</span>
        </div>

        {/* Patient Rows */}
        {loading ? (
          <p className="text-center text-gray-600">Cargando pacientes...</p>
        ) : (
          filtered.map(p => <PatientRow key={p.id} patient={p} onView={handleView} />)
        )}
      </div>
    </DashboardLayout>
  );
};

export default PatientsPage;



