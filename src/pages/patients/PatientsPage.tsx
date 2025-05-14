import React, { useState } from "react";
import { usePatients } from "../../features/patients/hooks/usePatients";
import { PatientRow } from "../../features/patients/components/PatientRow";
import { PageContainer } from "../../components/shared/layouts/PageContainer";
import { useNavigate } from "react-router-dom"; 

const PatientsPage: React.FC = () => {
  const [query, setQuery] = useState("");
  const { patients, loading, error, reload } = usePatients(0, 20);
 const navigate = useNavigate();
  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const q = e.target.value;
    setQuery(q);
    reload(q.trim());
  };

  const handleAdd = () => {
  navigate("/Sregister");
  };
  const handleView = (id: number) => { 
    navigate(`/patient/${id}`);
    };

  return (
    <PageContainer
    title="Lista de Pacientes"
    description="nombre o DNI"
    query={query}
    onQueryChange={handleSearchChange}
    onAdd={handleAdd}
    addLabel="Agregar paciente" 
  >
    {/* Encabezados: 8 columnas */}
    <div className="grid grid-cols-[2fr_1fr_1fr_1fr_1fr_1fr_1fr_1fr] items-end text-sm text-gray-500 mb-4 px-4">

        {/* 1. Paciente */}
      <div>
        <p className="font-medium text-gray-700">Paciente</p>
        <p className="text-xs text-gray-400">Edad</p>
      </div>

      {/* 2. DNI */}
      <div>
        <p className="font-medium text-gray-700">DNI</p>
        <p className="text-xs text-gray-400">Historia clínica</p>
      </div>

      {/* 3. Próx. sesión */}
      <span className="font-medium text-center">Próx. sesión</span>

      {/* 4. Última sesión */}
      <span className="font-medium text-center">Última sesión</span>

      {/* 5. Contacto */}
      <span className="font-medium text-gray-700">Contacto</span>

      {/* 6. Relación */}
      <span className="font-medium text-gray-700">Relación</span>

      {/* 7. Teléfono */}
      <span className="font-medium text-gray-700">Teléfono</span>

      {/* 8. Acción */}
      <span className="font-medium text-gray-700 text-right">Historial clinico</span>
    </div>

      {/* Filas */}
      {loading ? (
        <p className="text-center text-gray-600">Cargando pacientes…</p>
      ) : error ? (
        <p className="text-center text-red-500">Error: {error}</p>
      ) : patients.length === 0 ? (
        <p className="text-center text-gray-600">No se encontraron pacientes</p>
      ) : (
        patients.map(p => (
          <PatientRow key={p.id} patient={p} onView={handleView} />
        ))
      )}
    </PageContainer>
  );
};

export default PatientsPage;