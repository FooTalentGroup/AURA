import { useEffect, useState } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import {
  AppointmentProps,
  FollowEntriesProps,
  PatientDiagnosesProps,
  PatientNotesInfo,
  PatientProps,
  SchoolProps,
  TabId,
  tabs,
} from "../../features/patientTabs/types/patientTabs.types";
import {
  ArrowLeftIcon,
  EmailIcon,
  PlusIcon,
} from "../../components/shared/ui/Icons";
import { Link, useNavigate, useParams } from "react-router-dom";
import { api } from "../../core/services/api";
import PatientInfoTab from "../../features/patientTabs/components/PatientInfoTab";
import ContactTab from "../../features/patientTabs/components/ContactTab";
import DiagnosticTab from "../../features/patientTabs/components/DiagnosticTab";
import ClinicalHistoryTab from "../../features/patientTabs/components/ClinicalHistoryTab";
import MedicalBackgroundTab from "../../features/patientTabs/components/MedicalBackgroundTab";
import Loader from "../../components/shared/ui/Loader";

// Componente principal
export default function PatientTabs() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [activeTab, setActiveTab] = useState<TabId>("paciente");
  const [patientDB, setPatient] = useState<PatientProps | null>(null);
  const [patientSchool, setPatientSchool] = useState<SchoolProps | null>(null);
  const [followEntries, setFollowEntries] = useState<FollowEntriesProps | null>(
    null
  );
  const [patientDiagnoses, setPatientDiagnoses] =
    useState<PatientDiagnosesProps | null>(null);
  const [medicalRecordFilters, setMedicalRecordFilters] = useState<
    AppointmentProps[] | null
  >(null);
  const [medicalBackgrounds, setMedicalBackgrounds] =
    useState<PatientNotesInfo>();

  useEffect(() => {
    const fetchPatient = async () => {
      try {
        if (!id) return;
        const patientID = Number(id);
        const patientData = await api.getPatientById(patientID);
        const schoolsList = await api.listSchoolsPaginated();
        const medicalRecordPatientId = await api.getMedicalRecordByPatientId(
          patientID
        );
        const patientDiagnosesId = medicalRecordPatientId.diagnosisIds[0];
        const medicalId = medicalRecordPatientId.id;
        const patientFollowEntries = await api.getFollowEntriesById(medicalId);
        const patientDiagnosesData = await api.getDiagnosesById(
          patientDiagnosesId
        );
        const medicalRecordFilter = await api.getMedicalRecordFilter();
        const medicalBackgrounds = await api.getMedicalBackgroundsById(
          patientID
        );

        // Busco la escuela correspondiente a cada paciente
        const school = schoolsList.content.find(
          (item) => item.id === patientData.schoolId
        );

        setPatient(patientData);
        setPatientSchool(school || null);
        setPatientDiagnoses(patientDiagnosesData);
        setFollowEntries(patientFollowEntries);
        setMedicalRecordFilters(medicalRecordFilter);
        setMedicalBackgrounds(medicalBackgrounds);
      } catch (err) {
        console.error("Error al cargar el paciente:", err);
      }
    };
    fetchPatient();
  }, [id]);

  const handleTabChange = (tabId: TabId) => {
    setActiveTab(tabId);
  };

  // Renderizar el contenido según la pestaña activa
  const renderTabContent = () => {
    switch (activeTab) {
      case "paciente":
        if (!patientDB) {
          return <div>Cargando datos del paciente...</div>;
        }
        return <PatientInfoTab patient={patientDB} />;
      case "contacto":
        if (!patientDB || !patientSchool) {
          return <div>Cargando datos del paciente...</div>;
        }
        return <ContactTab patient={patientDB} school={patientSchool} />;
      case "diagnostico":
        if (!patientDiagnoses) {
          return <div>Cargando datos del paciente...</div>;
        }
        return <DiagnosticTab diagnoses={patientDiagnoses} />;
      case "historial":
        if (!medicalRecordFilters || !followEntries) {
          return <div>Cargando datos del paciente...</div>;
        }
        return (
          <ClinicalHistoryTab
            medicalFilters={medicalRecordFilters}
            followEntries={followEntries}
          />
        );
      case "antecedentes":
        if (!medicalBackgrounds) {
          return <div>Cargando datos del paciente...</div>;
        }
        return <MedicalBackgroundTab medicalBackgrounds={medicalBackgrounds} />;
      default:
        return null;
    }
  };

  return (
    <DashboardLayout>
      <section className="bg-white rounded-2xl shadow-sm">
        <div className="p-4 border-b-2 border-gray-300/90 flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link to="#" onClick={() => navigate(-1)} title="Atrás">
              <ArrowLeftIcon />
            </Link>
            {!patientDB ? (
              <p className="text-xl">Cargando paciente...</p>
            ) : (
              <>
                <div className="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-medium">
                  {patientDB?.name.charAt(0).toUpperCase()}
                  {patientDB?.lastName.charAt(0).toUpperCase()}
                </div>
                <h2 className="text-2xl font-medium text-gray-800">
                  {patientDB?.name} {patientDB?.lastName}
                </h2>
              </>
            )}
          </div>
          <div className="flex gap-3">
            <button className="flex items-center gap-2 px-4 py-2 border border-black rounded-full text-blue-600 hover:bg-blue-50 cursor-pointer">
              <EmailIcon />
              Crear Informe
            </button>
            <button className="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-full hover:bg-blue-700 cursor-pointer">
              <PlusIcon />
              Agregar registro
            </button>
          </div>
        </div>

        {!patientDB ? (
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
                    onClick={() => handleTabChange(tab.id)}
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

            {/* Contenido de la pestaña activa */}

            <main className="px-6 pb-6">{renderTabContent()}</main>
          </>
        )}
      </section>
    </DashboardLayout>
  );
}
