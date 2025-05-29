import React, { useEffect, useState } from "react";
import { usePatients } from "../../features/patients/hooks/usePatients";
import { PatientRow } from "../../features/patients/components/PatientRow";
import { PageContainer } from "../../components/shared/layouts/PageContainer";
import { useNavigate } from "react-router-dom";
import { patientService } from "../../features/patients/services/patientService";
import { Patient } from "../../features/patients/types/patient.types";


const PatientsPage: React.FC = () => {
  const [query, setQuery] = useState("");
  const { patients, loading, error, reload } = usePatients(0, 20);
  const [filteredPatients, setFilteredPatients] = useState<Patient[]>([]);
  const navigate = useNavigate();

  // Cuando cambie la lista “maestra”, reseteamos el filtrado
  useEffect(() => {
    setFilteredPatients(patients);
  }, [patients]);

  const handleSearchChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const q = e.target.value;
    setQuery(q);
    const trimmed = q.trim();
    if (!trimmed) {
      reload("");
      return;
    }

    if (/^\d+$/.test(trimmed)) {
      try {
        const patient = await patientService.searchByDni(trimmed);
        setFilteredPatients(patient ? [patient] : []);
      } catch (error) {
        console.error("Error searching by DNI:", error);
        setFilteredPatients([]);
      }
    } else {
      // Cualquier otra cosa => búsqueda por nombre
      try {
        const byName = await patientService.searchByName(trimmed);
        setFilteredPatients(byName);
      } catch (error) {
        console.error("Error searching by name:", error);
        setFilteredPatients([]);
      }
    }
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
      count={filteredPatients.length}
    >
      <div className="grid grid-cols-[auto_2fr_1fr_1fr_1fr_1fr_1fr_1fr_1fr] items-end text-sm text-gray-500 mb-4 px-4">
        <span />
        <span className="col-start-2  font-medium text-[#4D5358]">Paciente</span>
        <span className="font-medium text-[#4D5358]">DNI</span>
        <span className="font-medium text-[#4D5358]">Contacto</span>
        <span className="col-start-9 col-end-10 justify-end font-medium text-[#4D5358]">
          Historial clínico
        </span>
      </div>

      {loading ? (
        <p className="text-center text-gray-600">Cargando pacientes…</p>
      ) : error ? (
        <p className="text-center text-red-500">Error: {error}</p>
      ) : filteredPatients.length === 0 ? (
        <p className="text-center text-gray-600">No se encontraron pacientes</p>
      ) : (
        filteredPatients.map((p) => (
          <PatientRow key={p.id} patient={p} onView={handleView}  />
        ))
      )}
    </PageContainer>
  );
};

export default PatientsPage;