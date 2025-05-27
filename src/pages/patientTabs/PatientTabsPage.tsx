import { useEffect, useState, useCallback } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import DashboardLayout from "../../layouts/DashboardLayout";
import Loader from "../../components/shared/ui/Loader";
import { ArrowLeftIcon, PlusIcon } from "../../components/shared/ui/Icons";
import RegisterClinicalRecordModal from "../../features/patientTabs/components/ClinicalObservationModal";
import ClinicalHistoryTab from "../../features/patientTabs/components/ClinicalHistoryTab";
import PatientInfoTab from "../../features/patientTabs/components/PatientInfoTab";
import ContactTab from "../../features/patientTabs/components/ContactTab";
import DiagnosticTab from "../../features/patientTabs/components/DiagnosticTab";
import MedicalBackgroundTab from "../../features/patientTabs/components/MedicalBackgroundTab";
import {
  AppointmentProps,
  ExtendedPatientDiagnosesProps,
  FollowEntriesProps,
  PatientNotesInfo,
  PatientProps,
  SchoolProps,
  TabId,
  tabs,
} from "../../features/patientTabs/types/patientTabs.types";
import { api } from "../../core/services/api";

export default function PatientTabsPage() {
  const navigate = useNavigate();
  const { id } = useParams();
  const patientID = Number(id);

  // --- State ---
  const [activeTab, setActiveTab] = useState<TabId>("paciente");
  const [patient, setPatient] = useState<PatientProps | null>(null);
  const [school, setSchool] = useState<SchoolProps | null>(null);
  const [diagnoses, setDiagnoses] =
    useState<ExtendedPatientDiagnosesProps | null>(null);
  const [followEntries, setFollowEntries] = useState<FollowEntriesProps | null>(
    null
  );
  const [appointments, setAppointments] = useState<AppointmentProps[]>([]);
  const [backgrounds, setBackgrounds] = useState<PatientNotesInfo | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  // --- Fetch all patient-related data ---
  const fetchPatient = useCallback(async () => {
    if (!patientID) return;
    try {
      // 1) Paciente
      const p = await api.getPatientById(patientID);
      setPatient(p);

      // 2) Escuela
      const schools = await api.listSchoolsPaginated();
      setSchool(schools.content.find((s) => s.id === p.schoolId) || null);

      // 3) Medical Record → citas (appointments) y follow entries
      let record;
      try {
        record = await api.getMedicalRecordByPatientId(patientID);
      } catch {
        console.warn(`Sin historial médico para patient ${patientID}`);
      }

      if (record) {
        // Turnos asociados a la historia
        try {
          const appts = await api.getMedicalRecordFilter(); // o bien api.getAppointmentsByMedicalRecordId(record.id) si lo tienes
          setAppointments(appts);
        } catch (err) {
          console.error("Error al obtener turnos asociados:", err);
        }

        // Follow-up entries
        try {
          const fe = await api.getFollowEntriesById(record.followUpIds[0]);
          setFollowEntries(fe);
        } catch {
          console.warn(`Sin follow entries para record ${record.id}`);
        }

        // Diagnósticos del paciente (tomando el primer diagnosisId)
        if (record.diagnosisIds?.length) {
          try {
            const diag = await api.getDiagnosesById(record.diagnosisIds[0]);

            try {
              const professional = await api.getProfessionalById(
                diag.idProfessional
              );

              const extendedDiag: ExtendedPatientDiagnosesProps = {
                ...diag,
                professionalName: professional.name,
                professionalLastName: professional.lastName,
              };

              setDiagnoses(extendedDiag);
            } catch {
              console.warn(
                `No se encontró el profesional con id: ${diag.idProfessional}`
              );
            }
          } catch {
            console.warn(
              `Sin diagnósticos para diagnosisId ${record.diagnosisIds[0]}`
            );
          }
        }
      }

      // 4) Antecedentes médicos
      try {
        const bg = await api.getMedicalBackgroundsById(patientID);
        setBackgrounds(bg);
      } catch {
        console.warn(`Sin antecedentes para patient ${patientID}`);
      }
    } catch (err) {
      console.error("Error al cargar datos del paciente:", err);
    }
  }, [patientID]);

  // Carga inicial y recarga cuando cambie patientID
  useEffect(() => {
    fetchPatient();
  }, [fetchPatient]);

  // Al crear un nuevo registro clínico en el modal
  const handleSuccess = () => {
    fetchPatient();
    setIsModalOpen(false);
  };

  return (
    <DashboardLayout>
      <section className="bg-white rounded-2xl shadow-sm">
        <div className="p-4 border-b-2 border-gray-300/90 flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link to="#" onClick={() => navigate(-1)} title="Atrás">
              <ArrowLeftIcon />
            </Link>
            {!patient ? (
              <p className="text-xl">Cargando paciente...</p>
            ) : (
              <>
                <div className="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-medium">
                  {patient.name.charAt(0).toUpperCase()}
                  {patient.lastName.charAt(0).toUpperCase()}
                </div>
                <h2 className="text-2xl font-medium text-gray-800">
                  {patient.name} {patient.lastName}
                </h2>
              </>
            )}
          </div>

          <button
            onClick={() => setIsModalOpen(true)}
            className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-full hover:bg-blue-700"
          >
            <PlusIcon /> Agregar registro
          </button>
        </div>

        {!patient ? (
          <div className="flex justify-center items-center h-96">
            <Loader />
          </div>
        ) : (
          <>
            <header className="mt-4 px-6">
              <nav className="flex">
                {tabs.map((tab) => (
                  <button
                    key={tab.id}
                    onClick={() => setActiveTab(tab.id)}
                    className={`relative px-8 py-4 rounded-t-2xl font-medium transition-colors cursor-pointer ${
                      activeTab === tab.id
                        ? "bg-gray-200/60 text-blue-600 after:content-[''] after:absolute after:bottom-0 after:left-1/2 after:-translate-x-1/2 after:w-16 after:h-0.5 after:bg-blue-600"
                        : "text-gray-500 hover:text-gray-700"
                    }`}
                  >
                    {tab.label}
                  </button>
                ))}
              </nav>
            </header>

            <main className="px-6 pb-6">
              {activeTab === "paciente" && (
                <PatientInfoTab
                  patient={patient}
                  onUpdate={(updated) => setPatient(updated)}
                />
              )}
              {activeTab === "contacto" && (
                <ContactTab
                  patient={patient}
                  school={school!}
                  onUpdatePatient={setPatient}
                  onUpdateSchool={setSchool}
                />
              )}
              {activeTab === "diagnostico" && (
                <DiagnosticTab
                  diagnoses={diagnoses!}
                  // onUpdate={(updated) => setDiagnoses(updated)}
                  onUpdate={(updatedDiagnosis) => {
                    setDiagnoses((prev) =>
                      prev ? { ...prev, ...updatedDiagnosis } : null
                    );
                  }}
                />
              )}
              {activeTab === "historial" && (
                <ClinicalHistoryTab
                  medicalFilters={appointments}
                  followEntries={followEntries!}
                />
              )}
              {activeTab === "antecedentes" &&
                (backgrounds ? (
                  <MedicalBackgroundTab medicalBackgrounds={backgrounds} />
                ) : (
                  <div className="bg-gray-200/60 p-4 rounded-xl">
                    <p className="text-lg">
                      No se encontraron antecedentes para este paciente.
                    </p>
                  </div>
                ))}
            </main>
          </>
        )}

        {/* Modal de registro clínico */}
        <RegisterClinicalRecordModal
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          onSuccess={handleSuccess}
          patientId={patientID}
        />
      </section>
    </DashboardLayout>
  );
}
