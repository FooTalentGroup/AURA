import React, { useState } from "react";
import { useProfessionals } from "../../features/professional/hooks/useProfessionals";
import { ProfessionalRow } from "../../features/professional/components/ProfessionalRow";
import { PageContainer } from "../../components/shared/layouts/PageContainer";
import { useNavigate } from "react-router-dom";

const ProfessionalPage: React.FC = () => {
  const [query, setQuery] = useState("");
  const { professionals, loading, error } = useProfessionals(
    0,
    20,
    query
  );
const navigate = useNavigate();
  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setQuery(e.target.value);
  };


  const handleAdd = () => {
    {navigate("/Uregister")};  };

  const handleView = (id: number) => {
    console.log('Ver paciente', id);
    //  un navigate(`/patients/${id}`)
  };

  return (
    <PageContainer
      title="Personal del centro"
      description="Nombre o Especialidad"
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
          <ProfessionalRow key={u.id} professional={u} onView={handleView}  onViewSchedule={() => console.log('Ver horario', u.id)} />
        ))
      )}
    </PageContainer>
  );
};

export default ProfessionalPage;