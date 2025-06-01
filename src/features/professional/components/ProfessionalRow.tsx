import React, { useState } from "react";
import { RxDotsVertical } from "react-icons/rx";
import { Modal } from "../../../layouts/Modal";
import { Professional } from "../types/Professional.types";
import { getSpecialtyColors } from "../../patientTabs/utils/utils";

interface Props {
  professional: Professional;
  onView?: (id: number) => void;
  onViewSchedule?: (id: number) => void;
  onDelete?: (id: number) => Promise<void>;
}

export const ProfessionalRow: React.FC<Props> = ({
  professional,
  onDelete,
}) => {
  const getInitial = (name: string) => name.charAt(0).toUpperCase();

  // Estados para menú y confirmación
  const [menuOpen, setMenuOpen] = useState(false);
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleDelete = async () => {
    if (!onDelete) return;
    setDeleting(true);
    setError(null);
    try {
      await onDelete(professional.id);
      setConfirmOpen(false);
      setMenuOpen(false);
    } catch (err: unknown) {
      if (err instanceof Error) {
        setError(err.message || "Error eliminando usuario");
      } else {
        setError("Error desconocido eliminando usuario");
      }
    } finally {
      setDeleting(false);
    }
  };

  return (
    <>
      <div className=" grid grid-cols-[2fr_1fr_1fr_1fr_1fr_1fr] items-center bg-white rounded-xl shadow-sm px-4 py-3 mb-3">
        {/* Avatar e info básica */}
        <div className="flex items-center space-x-4">
          <div className={`w-10 h-10 rounded-full flex items-center justify-center font-bold ${getSpecialtyColors(professional.specialty)}`}>
            {getInitial(professional.name) + getInitial(professional.lastName)}
          </div>
          <div>
            <div className="font-medium text-gray-800">
              {professional.name} {professional.lastName}
            </div>
            <button
              className={`text-xs px-2 py-1 rounded-lg font-semibold cursor-pointer border-0 ${getSpecialtyColors(professional.specialty)}`}
              type="button"
            >
              {professional.specialty}
            </button>
          </div>
        </div>

        {/* Otros datos */}
        <span className="text-gray-700">{professional.dni}</span>
        <span className="text-gray-700">{professional.specialty}</span>
        <span className="text-gray-700">{professional.phoneNumber}</span>
        <span className="text-gray-700">{professional.email}</span>

        {/* Acciones */}
        <div className="col-start-7 col-end-9 flex justify-end items-center space-x-2 relative">
    
          {/* Menú ⋮ */}
          <button
            onClick={() => setMenuOpen((o) => !o)}
            className="p-2 rounded-full hover:bg-gray-100"
          >
            <RxDotsVertical className="h-5 w-5 text-gray-500" />
          </button>
          {menuOpen && (
            <div
              className="absolute right-0 top-full mt-1 w-40 bg-white rounded-xl shadow-lg z-10"
              onMouseLeave={() => setMenuOpen(false)}
            >
              <button
                onClick={() => setConfirmOpen(true)}
                className="w-full text-left px-4 py-2 text-red-600 rounded-full hover:bg-red-50"
              >
                Eliminar usuario
              </button>
            </div>
          )}
        </div>
      </div>

      {/* Modal de confirmación de eliminación */}
      <Modal isOpen={confirmOpen} onClose={() => setConfirmOpen(false)}>
        <h3 className="text-xl font-medium mb-4">¿Estás seguro?</h3>
        <p className="mb-6">
          Esta acción eliminará al usuario definitivamente.
        </p>
        {error && <p className="text-sm text-red-600 mb-4">{error}</p>}
        <div className="flex justify-end space-x-4">
          <button
            onClick={() => setConfirmOpen(false)}
            disabled={deleting}
            className="px-4 py-2 bg-gray-200 text-gray-700 rounded-full hover:bg-gray-300"
          >
            Cancelar
          </button>
          <button
            onClick={handleDelete}
            disabled={deleting}
            className="px-4 py-2 bg-red-600 text-white rounded-full hover:bg-red-700 flex items-center"
          >
            {deleting ? (
              <span className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
            ) : (
              "Eliminar"
            )}
          </button>
        </div>
      </Modal>
    </>
  );
};
