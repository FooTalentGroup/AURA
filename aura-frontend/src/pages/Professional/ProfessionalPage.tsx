import React, { useState, useMemo } from "react";
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

  // Filtro de especialidad
  const [activeSpecialty, setActiveSpecialty] = useState<string>("");
  const specialties = useMemo(() => {
    const set = new Set(professionals.map((p) => p.specialty));
    return Array.from(set);
  }, [professionals]);
  const filteredProfessionals = useMemo(() => {
    if (!activeSpecialty) return professionals;
    return professionals.filter((p) => p.specialty === activeSpecialty);
  }, [professionals, activeSpecialty]);

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
        {/* Column Headers para profesionales */}
        <div className="grid grid-cols-[2fr_1fr_1fr_1fr_1fr_1fr] text-sm text-gray-500 mb-4 px-4 items-center">
          <span>Nombre</span>
          <span>DNI</span>
          <span>
            <select
              className="bg-white border border-gray-300 rounded  px-2 py-1 text-sm"
              value={activeSpecialty}
              onChange={(e) => setActiveSpecialty(e.target.value)}
            >
              <option value="">Especialidad</option>
              {specialties.map((spec) => (
                <option key={spec} value={spec}>
                  {spec}
                </option>
              ))}
            </select>
          </span>
          <span>Teléfono</span>
          <span>Email</span>
        </div>

        {/* Filas */}
        {loading ? (
          <p className="text-center text-gray-600">Cargando profesionales...</p>
        ) : error ? (
          <p className="text-center text-red-500">Error: {error}</p>
        ) : filteredProfessionals.length === 0 ? (
          <p className="text-center text-gray-600">No se encontraron usuarios</p>
        ) : (
          filteredProfessionals.map((u) => (
            <ProfessionalRow
              key={u.id}
              professional={u}
              onDelete={async (id) => {
                try {
                  await professionalsService.delete(id);
                  // Recarga la lista de profesionales tras eliminar uno
                  await reload();
                } catch (e: unknown) {
                  if (e instanceof Error) {
                    console.error(
                      "Error al eliminar el profesional:",
                      e.message
                    );
                    // Propaga el error para que el modal de la fila lo capture y muestre el mensaje
                    throw e;
                  } else {
                    console.error(
                      "Error desconocido al eliminar el profesional"
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
          <h2 className="text-lg font-medium">¿Qué usuario queres registrar?</h2>
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