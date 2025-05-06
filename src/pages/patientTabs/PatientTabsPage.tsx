import { useState } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import {
  PatientData,
  TabId,
  TabItem,
} from "../../features/patientTabs/types/patientTabs.types";
import {
  ArrowLeftIcon,
  EmailIcon,
  PencilIcon,
  PlusIcon,
} from "../../components/shared/ui/Icons";
import { Link } from "react-router-dom";

// Definición de las pestañas disponibles
const tabs: TabItem[] = [
  { id: "paciente", label: "Paciente" },
  { id: "contacto", label: "Contacto" },
  { id: "diagnostico", label: "Diagnóstico" },
  { id: "historial", label: "Historial clínico" },
  { id: "antecedentes", label: "Antecedentes" },
];

// Datos de ejemplo
const patientData: PatientData = {
  nombre: "Olivia Curuchet",
  fechaNacimiento: "12 / 05 / 2016",
  dni: "46.237.981",
  edad: 8,
  sexo: "Femenino",
  obraSocial: {
    nombre: "OSDE",
    plan: "210",
    numeroAfiliado: "156150-06",
  },
};

interface ContactData {
  nombre: string;
  relacion: string;
  telefono: string;
  domicilio: string;
  email: string;
  escuela: {
    nombre: string;
    nivel: string;
    turno: string;
    directivo: string;
    email: string;
    telefono: string;
  };
}

const contactData: ContactData = {
  nombre: "Paula Curuchet",
  relacion: "Madre",
  telefono: "+54 11 6789 2345",
  domicilio: "Av. San Martín 3500, Quilmes, Buenos Aires",
  email: "paula.curuchet@gmail.com",
  escuela: {
    nombre: "Colegio San Gabriel",
    nivel: "Primario - 2° Grado",
    turno: "Mañana",
    directivo: "Laura Domínguez",
    email: "lucianagomez@gmail.com",
    telefono: "+54 11 6789 2345",
  },
};

// Componente principal
export default function PatientTabs() {
  const [activeTab, setActiveTab] = useState<TabId>("paciente");

  // Manejador para cambiar entre pestañas
  const handleTabChange = (tabId: TabId) => {
    setActiveTab(tabId);
  };

  // Renderizar el contenido según la pestaña activa
  const renderTabContent = () => {
    switch (activeTab) {
      case "paciente":
        return <PatientInfoTab patientData={patientData} />;
      case "contacto":
        return <ContactTab contactData={contactData} />;
      case "diagnostico":
        return <DiagnosticTab />;
      case "historial":
        return <ClinicalHistoryTab />;
      case "antecedentes":
        return <MedicalBackgroundTab />;
      default:
        return null;
    }
  };

  return (
    <DashboardLayout>
      <div className="bg-white rounded-2xl shadow-sm">
        {/* Header con información del paciente */}
        <div className="p-4 border-b-2 border-gray-300/90 flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link to="/">
              <ArrowLeftIcon />
            </Link>
            <div className="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-medium">
              OC
            </div>
            <h2 className="text-2xl font-medium text-gray-800">
              Olivia Curuchet
            </h2>
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

        {/* Navegación por pestañas */}
        <div className="mt-4 px-6">
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
        </div>

        {/* Contenido de la pestaña activa */}
        <div className="px-6 pb-6">{renderTabContent()}</div>
      </div>
    </DashboardLayout>
  );
}

// Componentes para cada pestaña
function PatientInfoTab({ patientData }: { patientData: PatientData }) {
  return (
    <section className="grid grid-cols-3 gap-4 bg-gray-200/60 p-4 rounded-b-2xl rounded-tr-2xl">
      <article className="h-fit bg-white py-2 rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-lg font-medium text-gray-800">
            Datos Personales
          </h3>
          <button className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer">
            <PencilIcon />
          </button>
        </header>
        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          <div className="col-span-2">
            <p className="font-semibold text-blue-600 mb-2">
              Nombre y Apellido
            </p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {patientData.nombre}
            </p>
          </div>
          <div>
            <p className="font-semibold text-blue-600 mb-2">
              Fecha de nacimiento
            </p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {patientData.fechaNacimiento}
            </p>
          </div>
          <div>
            <p className="font-semibold text-blue-600 mb-2">Edad</p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {patientData.edad}
            </p>
          </div>
          <div>
            <p className="font-semibold text-blue-600 mb-2">DNI</p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {patientData.dni}
            </p>
          </div>
          <div>
            <p className="font-semibold text-blue-600 mb-2">Sexo</p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {patientData.sexo}
            </p>
          </div>
        </form>
      </article>

      <article className="bg-white py-2 h-fit rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-lg font-medium text-gray-800">Obra social</h3>
          <button className="p-2 text-gray-600 hover:text-blue-600">
            <PencilIcon />
          </button>
        </header>

        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          <div>
            <p className="font-semibold text-blue-600 mb-2">Nombre</p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {patientData.obraSocial.nombre}
            </p>
          </div>
          <div>
            <p className="font-semibold text-blue-600 mb-2">Plan</p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {patientData.obraSocial.plan}
            </p>
          </div>
          <div className="col-span-2">
            <p className="font-semibold text-blue-600 mb-2">
              Número de afiliado
            </p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {patientData.obraSocial.numeroAfiliado}
            </p>
          </div>
        </form>
      </article>

      <button className="h-fit w-fit flex items-center gap-2 px-6 py-3 bg-sky-300/50 text-blue-900 rounded-lg hover:bg-sky-300 cursor-pointer transition-colors duration-300">
        <PlusIcon />
        Añadir información
      </button>
    </section>
  );
}

// Componentes de las otras pestañas
function ContactTab({ contactData }: { contactData: ContactData }) {
  return (
    <section className="grid grid-cols-3 gap-4 bg-gray-200/60 p-4 rounded-b-2xl rounded-t-2xl">
      <article className="h-fit bg-white py-2 rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-lg font-medium text-gray-800">Tutor 1</h3>
          <button className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer">
            <PencilIcon />
          </button>
        </header>
        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2" action="">
          <div className="col-span-2">
            <p className="font-semibold text-blue-600 mb-2">
              Nombre y Apellido
            </p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {contactData.nombre}
            </p>
          </div>
          <div>
            <p className="font-semibold text-blue-600 mb-2">Relación</p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {contactData.relacion}
            </p>
          </div>
          <div>
            <p className="font-semibold text-blue-600 mb-2">Teléfono</p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {contactData.telefono}
            </p>
          </div>
          <div className="col-span-2">
            <p className="font-semibold text-blue-600 mb-2">Domicilio</p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {contactData.domicilio}
            </p>
          </div>
          <div className="col-span-2">
            <p className="font-semibold text-blue-600 mb-2">
              Correo electrónico
            </p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {contactData.email}
            </p>
          </div>
        </form>
      </article>

      <article className="bg-white py-2 h-fit rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-lg font-medium text-gray-800">Escuela</h3>
          <button className="p-2 text-gray-600 hover:text-blue-600">
            <PencilIcon />
          </button>
        </header>

        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          <div className="col-span-2">
            <p className="font-semibold text-blue-600 mb-2">Institución</p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {contactData.escuela.nombre}
            </p>
          </div>
          <div>
            <p className="font-semibold text-blue-600 mb-2">Nivel</p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {contactData.escuela.nivel}
            </p>
          </div>
          <div>
            <p className="font-semibold text-blue-600 mb-2">Turno</p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {contactData.escuela.turno}
            </p>
          </div>
          <div className="col-span-2">
            <p className="font-semibold text-blue-600 mb-2">
              Directivo de referencia
            </p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {contactData.escuela.directivo}
            </p>
          </div>
          <div className="col-span-2">
            <p className="font-semibold text-blue-600 mb-2">
              Correo electrónico
            </p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {contactData.escuela.email}
            </p>
          </div>
          <div className="col-span-2">
            <p className="font-semibold text-blue-600 mb-2">Teléfono</p>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md">
              {contactData.escuela.telefono}
            </p>
          </div>
        </form>
      </article>

      <button className="h-fit w-fit flex items-center gap-2 px-6 py-3 bg-sky-300/50 text-blue-900 rounded-lg hover:bg-sky-300 cursor-pointer transition-colors duration-300">
        <PlusIcon />
        Añadir contacto
      </button>
    </section>
  );
}

function DiagnosticTab() {
  return (
    <div className="text-center bg-gray-200/60 p-4 rounded-b-2xl rounded-t-2xl">
      Información de diagnóstico del paciente
    </div>
  );
}

function ClinicalHistoryTab() {
  return (
    <div className="text-center bg-gray-200/60 p-4 rounded-b-2xl rounded-t-2xl">
      Historial clínico del paciente
    </div>
  );
}

function MedicalBackgroundTab() {
  return (
    <div className="text-center bg-gray-200/60 p-4 rounded-b-2xl rounded-t-2xl">
      Antecedentes médicos del paciente
    </div>
  );
}
