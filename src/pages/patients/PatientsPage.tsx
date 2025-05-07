import React, { useState } from "react";
import { usePatients } from "../../features/patients/hooks/usePatients";
import { PatientRow } from "../../features/patients/components/PatientRow";
import { PageContainer } from "../../components/shared/layouts/PageContainer";

const PatientsPage: React.FC = () => {
  const [query, setQuery] = useState("");
  const { patients, loading, error, reload } = usePatients(0, 20);

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const q = e.target.value;
    setQuery(q);
    reload(q.trim());
  };

  const handleAdd = () => {
    // lógica para agregar paciente
  };

  const handleView = (id: number) => {
    console.log('Ver paciente', id);
    // aquí harías un navigate(`/patients/${id}`)
  };

  return (
    <PageContainer
      title="pacientes"
      query={query}
      onQueryChange={handleSearchChange}
      onAdd={handleAdd}
      addLabel="Agregar paciente"
    >
      {/* Column Headers */}
      <div className="grid grid-cols-[2fr_1fr_1fr_1fr_2fr_1fr] text-sm text-gray-500 mb-4 px-4">
        <span>Paciente</span>
        <span>DNI</span>
        <span>Próxima sesión</span>
        <span>Última sesión</span>
        <span>Contacto</span>
        <span>Historia clínica</span>
      </div>

      {/* Filas */}
      {loading ? (
        <p className="text-center text-gray-600">Cargando pacientes...</p>
      ) : error ? (
        <p className="text-center text-red-500">Error: {error}</p>
      ) : patients.length === 0 ? (
        <p className="text-center text-gray-600">No se encontraron pacientes</p>
      ) : (
        patients.map((p) => (
          <PatientRow key={p.id} patient={p} onView={handleView} />
        ))
      )}
    </PageContainer>
  );
};

export default PatientsPage;