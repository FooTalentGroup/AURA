import { Patient } from "../../../features/patients/types/patient.types";
import { RxDotsVertical } from "react-icons/rx";
import { Modal } from "../../../layouts/Modal";
import { useState } from "react";

interface Props {
  patient: Patient;
  onView: (id: number) => void;
  onDelete: (id: number) => Promise<void>;
}

export const PatientRow: React.FC<Props> = ({ patient, onView, onDelete }) => {
  const initial =
    patient.name.charAt(0).toUpperCase() +
    patient.lastName.charAt(0).toUpperCase();

  const age =
    new Date().getFullYear() - new Date(patient.birthDate).getFullYear();
    const [menuOpen, setMenuOpen] = useState(false);
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const [error, setError] = useState<string>();

  const handleDelete = async () => {
    setDeleting(true);
    try {
      await onDelete(patient.id);
      setConfirmOpen(false);
      setMenuOpen(false);
    } catch (err: any) {
      setError(err.message || "Error desconocido");
    } finally {
      setDeleting(false);
    }
  };

  return (<>
    <div className="grid grid-cols-[2fr_1fr_1fr_1fr_1fr_1fr_1fr_1fr] items-center bg-white rounded-xl shadow-sm px-4 py-3 mb-3">
      {/* 1. Paciente + edad */}
      <div className="flex items-center space-x-4">
        <div className="w-10 h-10 rounded-full bg-blue-100 text-blue-700 flex items-center justify-center font-bold">
          {initial}
        </div>
        <div>
          <p className="font-medium text-gray-800">
            {patient.name} {patient.lastName}
          </p>
          <p className="text-xs text-gray-400">{age} años</p>
        </div>
      </div>

      {/* 2. DNI + botón Ver (HC) */}

      <span className="text-gray-700">{patient.dni}</span>



      {/* 3. Contacto (tutorName) */}
      <span className="text-gray-700">{patient.tutorName}</span>

      {/* 4. Relación (tutorRelation) */}
      <span className="text-gray-700">{patient.relationToPatient}</span>

      {/* 5. Teléfono */}
      <span className="text-gray-700">{patient.phoneNumber}</span>

      {/* 6. Acción principal */}
      <div className="col-start-8 col-end-10 flex justify-end items-center space-x-2">
        <button
          onClick={() => onView(patient.id)}
          className="cursor-pointer text-[#0072C3] border border-[#757780] rounded-full px-4 py-1 text-sm font-medium hover:border-[#0072C3] hover:bg-[#0072C3] hover:text-white transition"
        >
          Ver Paciente
        </button>
        <div className="relative">
        <button
          onClick={() => setMenuOpen((o) => !o)}
          className="cursor-pointer p-2 rounded-full hover:bg-gray-100"
        ><RxDotsVertical className="h-5 w-5 text-gray-500" />
        </button>
        
         {/* Menú desplegable */}
        {menuOpen && (
          <div
            className="absolute right-0 top-full mt-1 w-40 bg-white border-white rounded-full  shadow-lg z-10
                         transform translate-y-1"
            onMouseLeave={() => setMenuOpen(false)}
          >
            <button
              onClick={() => setConfirmOpen(true)}
              className="w-full text-left px-4 py-2 text-sm text-red-600 rounded-full hover:bg-red-50"
            >
              Eliminar usuario
            </button>
          </div>
        )}
        </div>
      </div>
      </div>
       {/* Modal de confirmación */}
      <Modal isOpen={confirmOpen} onClose={() => setConfirmOpen(false)}>
        <h3 className="text-xl font-medium mb-4">¿Estás seguro?</h3>
        <p className="mb-6">Esta acción eliminará al usuario definitivamente.</p>

        {error && (
          <p className="text-sm text-red-600 mb-4">{error}</p>
        )}

        <div className="flex justify-end space-x-4">
          <button
            onClick={() => setConfirmOpen(false)}
            className="px-4 py-2 bg-gray-200 text-gray-700 rounded-full hover:bg-gray-300"
            disabled={deleting}
          >
            Cancelar
          </button>
          <button
            onClick={handleDelete}
            className="px-4 py-2 bg-red-600 text-white rounded-full hover:bg-red-700 flex items-center"
            disabled={deleting}
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
