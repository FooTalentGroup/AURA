import DashboardLayout from "../../layouts/DashboardLayout";
import { usePatients } from '../../core/hooks/patients/usePatients';
import { PatientRow } from "../../core/components/patients/PatientRow";
import { FiSearch, FiUserPlus } from 'react-icons/fi';
import { useState } from "react";

const PatientsPage = () => {
  const [query, setQuery] = useState('')
  const { patients, loading, error, reload } = usePatients(0, 20)

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const q = e.target.value
    setQuery(q)
    reload(q.trim())
  }


  const handleView = (id: number) => {
    console.log('Ver paciente', id);
    // aquí harías un navigate(`/patients/${id}`)
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
                onChange={handleSearchChange}
                placeholder="Buscar por nombre o DNI..."
                className="h-12 w-full rounded-full border border-gray-300 pl-12 pr-4 focus:outline-none focus:ring-2 focus:ring-[#0072C3]"
              />
              <FiSearch className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 text-lg" />
            </div>
            <button className="h-12 bg-[#0072C3] hover:bg-[#005A9E] text-white px-5 rounded-full flex items-center space-x-2 transition">
              <FiUserPlus className="text-xl" />
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
          <span>Historia clínica</span>
        </div>

        {/* Patient Rows */}
        {loading ? (
          <p className="text-center text-gray-600">Cargando pacientes...</p>
        ) : error ? (
          <p className="text-center text-red-500">Error: {error}</p>
        ) : patients.length === 0 ? (
          <p className="text-center text-gray-600">No se encontraron pacientes</p>
        ) : (
          patients.map(p => (
            <PatientRow key={p.id} patient={p} onView={handleView} />
          ))
        )}
      </div>
    </DashboardLayout>
  );
};

export default PatientsPage;