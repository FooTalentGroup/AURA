import React, { useState } from "react";
// import { useProfessionals } from "";
// import { ProfessionalRow } from "";
import { PageContainer } from "../../components/shared/layouts/PageContainer";

const ProfessionalPage: React.FC = () => {
  const [query, setQuery] = useState("");
  const { professionals, loading, error, reload } = useProfessionals(0, 20);

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const q = e.target.value;
    setQuery(q);
    reload(q.trim());
  };

  const handleAdd = () => {
    // lógica para crear usuario
  };

  const handleView = (id: number) => {
    console.log('Ver paciente', id);
    // aquí harías un navigate(`/patients/${id}`)
  };

  return (
    <PageContainer
      title="Personal del centro"
      query={query}
      onQueryChange={handleSearchChange}
      onAdd={handleAdd}
      addLabel="Crear Usuario"
    >
      {/* Aquí podrías insertar tabs (“Profesionales” / “Administrativo”) */}

      {/* Column Headers para profesionales */}
      <div className="grid grid-cols-[2fr_1fr_1fr_1fr_1fr_1fr] text-sm text-gray-500 mb-4 px-4">
        <span>Nombre</span>
        <span>DNI</span>
        <span>Especialidad</span>
        <span>Teléfono</span>
        <span>Email</span>
        <span>Acciones</span>
      </div>

      {/* Filas */}
      {loading ? (
        <p className="text-center text-gray-600">Cargando profesionales...</p>
      ) : error ? (
        <p className="text-center text-red-500">Error: {error}</p>
      ) : professionals.length === 0 ? (
        <p className="text-center text-gray-600">No se encontraron usuarios</p>
      ) : (
        professionals.map((u) => (
          <ProfessionalRow key={u.id} professional={u} onView={handleView} />
        ))
      )}
    </PageContainer>
  );
};

export default ProfessionalPage;