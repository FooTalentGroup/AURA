import React, { useState } from "react";
import { useProfessionals } from "../../features/professional/hooks/useProfessionals";
import { ProfessionalRow } from "../../features/professional/components/ProfessionalRow";
import { PageContainer } from "../../components/shared/layouts/PageContainer";
import { useNavigate } from "react-router-dom";
import { Modal } from "../../layouts/Modal";
import { professionalsService } from "../../features/professional/services/ProfessionalService";

const ProfessionalPage: React.FC = () => {
  const [query, setQuery] = useState("");
  const { professionals, loading, error, reload } = useProfessionals(
    0,
    20,
    query
  );
  const navigate = useNavigate();
  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setQuery(e.target.value);
  };
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleAdd = () => {
    setIsModalOpen(true);
  };
  const closeModal = () => {
    setIsModalOpen(false);
  };

  const chooseType = (type: "administrativo" | "profesional") => {
    setIsModalOpen(false);
    if (type === "administrativo") {
      navigate("/Rregister");
    } else {
      navigate("/Pregister");
    }
  };

  const handleView = (id: number) => {
    console.log("Ver paciente", id);
    //  un navigate(`/patients/${id}`)
  };

  return (
    <>
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
          <p className="text-center text-gray-600">
            No se encontraron usuarios
          </p>
        ) : (
          professionals.map((u) => (
            <ProfessionalRow
              key={u.id}
              professional={u}
              onView={handleView}
              onViewSchedule={() => navigate(`/professionals/${u.id}/schedule`)}
              onDelete={async (id) => {
                try {
                  await professionalsService.delete(id);
                  // una vez borrado, recargamos la lista
                  await reload();
                } catch (e: unknown) {
                  if (e instanceof Error) {
                    console.error(
                      "Error al eliminar el profesional:",
                      e.message
                    );
                    // si quieres propagar el error al modal de la fila,
                    // lanza de nuevo la excepción para que lo capture ProfessionalRow
                    throw e;
                  } else {
                    console.error(
                      "Error desconodico al eliminar el profesional"
                    );
                    throw new Error("Error desconocido");
                  }
                }
              }}
            />
          ))
        )}
      </PageContainer>
      <Modal isOpen={isModalOpen} onClose={closeModal}>
        <div className="text-center space-y-6">
          <h2 className="text-lg font-medium">
            ¿Qué usuario queres registrar?
          </h2>
          <div className="flex justify-center gap-4">
            <button
              onClick={() => chooseType("administrativo")}
              className="px-4 py-2 border border-gray-500 text-blue-500 rounded-full hover:bg-blue-50"
            >
              Administrativo
            </button>
            <button
              onClick={() => chooseType("profesional")}
              className="px-6 py-2 bg-[#E5F6FF] text-blue-500  rounded-full hover:bg-[#c9e2f0]"
            >
              Profesional
            </button>
          </div>
        </div>
      </Modal>
    </>
  );
};

export default ProfessionalPage;
