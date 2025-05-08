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
  SearchIcon,
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

const patientData: PatientData = {
  name: "Olivia Curuchet",
  dateBirth: "12 / 05 / 2016",
  dni: "46.237.981",
  age: 8,
  genre: "Femenino",
  socialSecurity: {
    name: "OSDE",
    plan: "210",
    membershipNumber: "156150-06",
  },
};

interface ContactData {
  name: string;
  relationship: string;
  phone: string;
  residence: string;
  email: string;
  school: {
    name: string;
    level: string;
    shift: string;
    director: string;
    email: string;
    phone: string;
  };
}

const contactData: ContactData = {
  name: "Paula Curuchet",
  relationship: "Madre",
  phone: "+54 11 6789 2345",
  residence: "Av. San Martín 3500, Quilmes, Buenos Aires",
  email: "paula.curuchet@gmail.com",
  school: {
    name: "Colegio San Gabriel",
    level: "Primario - 2° Grado",
    shift: "Mañana",
    director: "Laura Domínguez",
    email: "lucianagomez@gmail.com",
    phone: "+54 11 6789 2345",
  },
};

interface DiagnosticData {
  title: string;
  paragraphs: string[];
  treatmentPlan: {
    title: string;
    items: string[];
  };
}

const diagnosticData: DiagnosticData = {
  title: "Trastorno del lenguaje",
  paragraphs: [
    "Olivia Curuchet, de 7 años, presenta un cuadro de dificultades en la adquisición y organización del lenguaje, manifestadas principalmente en el área de la expresión oral y la comprensión de consignas complejas. A nivel fonológico, se observa alteración en la articulación de fonemas (particularmente en grupos consonánticos y sonidos fricativos), lo que impacta en la inteligibilidad de su discurso.",
    "Desde el área psicopedagógica, se detectan dificultades en procesos atencionales sostenidos y selectivos, así como desafíos en la conciencia fonológica y procesamiento secuencial de la información verbal. Estas dificultades inciden en el rendimiento académico, especialmente en actividades de lectoescritura y resolución de consignas múltiples.",
    "La evaluación interdisciplinaria respalda la hipótesis de un trastorno específico del lenguaje (TEL) de tipo expresivo y mixto, asociado a un trastorno de aprendizaje con alteración en la lectura y escritura (Dislexia - Disgrafía incipiente).",
  ],
  treatmentPlan: {
    title: "Plan de tratamiento sugerido:",
    items: [
      "Intervención fonoaudiológica específica para la rehabilitación articulatoria, estimulación de la comprensión verbal y enriquecimiento del vocabulario.",
      "Tratamiento psicopedagógico focalizado en estrategias de atención, memoria verbal de trabajo y programas de apoyo a la lectoescritura.",
      "Articulación con la escuela para la implementación de adaptaciones curriculares no significativas (tiempo extendido, consignas simples, uso de apoyos visuales).",
      "Revisión diagnóstica interdisciplinaria en seis meses para evaluar evolución y ajustar el plan terapéutico.",
    ],
  },
};

interface ClinicalHistoryData {
  observations: string;
  interventions: string[];
  indications: string[];
}

const clinicalHistoryData: ClinicalHistoryData = {
  observations:
    "Se trabajó conciencia fonológica (identificación de sílabas iniciales en palabras familiares). Olivia mostró mayor rapidez en la identificación de palabras monosílabas, aunque presentó dificultades en palabras compuestas. Se observó buena predisposición y participación activa durante la actividad de rimas.",
  interventions: [
    "Juegos de asociación sonora.",
    "Ejercicios de segmentación silábica con apoyo visual.",
  ],
  indications: [
    "Reforzar la construcción de frases simples utilizando imágenes.",
    "Incorporar ejercicios de discriminación auditiva.",
  ],
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
        return <DiagnosticTab diagnosticData={diagnosticData} />;
      case "historial":
        return <ClinicalHistoryTab clinicalHistoryData={clinicalHistoryData} />;
      case "antecedentes":
        return <MedicalBackgroundTab />;
      default:
        return null;
    }
  };

  return (
    <DashboardLayout>
      <section className="bg-white rounded-2xl shadow-sm">
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
      </section>
    </DashboardLayout>
  );
}

function PatientInfoTab({ patientData }: { patientData: PatientData }) {
  return (
    <section className="grid grid-cols-3 gap-4 bg-gray-200/60 p-4 rounded-b-2xl rounded-tr-2xl">
      <article className="h-fit bg-white py-2 rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">
            Datos personales
          </h3>
          <button className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer">
            <PencilIcon />
          </button>
        </header>
        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Nombre y Apellido
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patientData.name}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">
              Fecha de nacimiento
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patientData.dateBirth}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Edad</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patientData.age}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">DNI</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patientData.dni}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Sexo</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patientData.genre}
            </p>
          </div>
        </form>
      </article>

      <article className="bg-white py-2 h-fit rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">Obra social</h3>
          <button className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer">
            <PencilIcon />
          </button>
        </header>

        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          <div>
            <label className="font-semibold text-blue-600">Nombre</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patientData.socialSecurity.name}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Plan</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patientData.socialSecurity.plan}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Número de afiliado
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {patientData.socialSecurity.membershipNumber}
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

function ContactTab({ contactData }: { contactData: ContactData }) {
  return (
    <section className="grid grid-cols-3 gap-4 bg-gray-200/60 p-4 rounded-b-2xl rounded-t-2xl">
      <article className="h-fit bg-white py-2 rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">Tutor 1</h3>
          <button className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer">
            <PencilIcon />
          </button>
        </header>
        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2" action="">
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Nombre y apellido
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.name}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Relación</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.relationship}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Teléfono</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.phone}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">Domicilio</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.residence}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Correo electrónico
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.email}
            </p>
          </div>
        </form>
      </article>

      <article className="bg-white py-2 h-fit rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">Escuela</h3>
          <button className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer">
            <PencilIcon />
          </button>
        </header>

        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">Institución</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.school.name}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Nivel</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.school.level}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Turno</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.school.shift}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Directivo de referencia
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.school.director}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Correo electrónico
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.school.email}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">Teléfono</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.school.phone}
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

function DiagnosticTab({ diagnosticData }: { diagnosticData: DiagnosticData }) {
  return (
    <section className="bg-gray-200/60 p-4 rounded-b-2xl rounded-t-2xl">
      <article className="h-fit w-4/6 bg-white py-2 rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">Diagnóstico</h3>
          <button className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer">
            <PencilIcon />
          </button>
        </header>
        <form className="text-sm p-4 pb-2" action="">
          <div className="mb-4">
            <label className="font-semibold text-blue-600">Título</label>
            <p className="w-5/9 py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {diagnosticData.title}
            </p>
          </div>

          <div className="bg-blue-50/80 p-4 rounded-md">
            <div className="space-y-4 mb-6">
              {diagnosticData.paragraphs.map((parrafo, index) => (
                <p key={index} className="text-gray-700">
                  {parrafo}
                </p>
              ))}
            </div>

            <div className="mb-2">
              <h4 className="font-medium text-gray-800">
                {diagnosticData.treatmentPlan.title}
              </h4>
            </div>

            <ul className="list-disc pl-5 space-y-2">
              {diagnosticData.treatmentPlan.items.map((item, index) => (
                <li key={index} className="text-gray-700">
                  {item}
                </li>
              ))}
            </ul>
          </div>
        </form>
      </article>
    </section>
  );
}

function ClinicalHistoryTab({
  clinicalHistoryData,
}: {
  clinicalHistoryData: ClinicalHistoryData;
}) {
  return (
    <section className="bg-gray-200/60 flex p-4 rounded-b-2xl rounded-t-2xl">
      <article className="text-xs bg-white py-2 rounded-l-lg border-r border-gray-300/90">
        <header className="flex items-center gap-4 p-4 border-b border-gray-300/90">
          <div className="flex gap-2">
            <select
              className="w-fit p-2 border border-gray-400 rounded-lg text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500 cursor-pointer"
              name=""
              id=""
            >
              <option value="">Fecha</option>
              <option value="">22 Mayo 2025</option>
              <option value="">23 Mayo 2025</option>
              <option value="">24 Mayo 2025</option>
            </select>
            <select
              className="w-fit p-2 border border-gray-400 rounded-lg text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500 cursor-pointer"
              name=""
              id=""
            >
              <option value="">Especialidad</option>
              <option value="">Fonoaudiología</option>
              <option value="">Psicología</option>
              <option value="">Psicopedagogía</option>
              <option value="">Terapia ocupacional</option>
            </select>
            <select
              className="p-2 border border-gray-400 rounded-lg text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-500 cursor-pointer"
              name=""
              id=""
            >
              <option value="">Profesional</option>
              <option value="">Fonoaudiología</option>
              <option value="">Psicología</option>
              <option value="">Psicopedagogía</option>
              <option value="">Terapia ocupacional</option>
            </select>
          </div>
          <button
            title="Buscar"
            className="text-gray-600 hover:text-blue-600 cursor-pointer"
          >
            <SearchIcon />
          </button>
        </header>
        <div className="p-4">
          <ul className="text-sm flex justify-between p-4 border rounded-lg">
            <li>22 Mayo 2025</li>
            <li>Fonoaudiología</li>
            <li>Federico Duarte</li>
          </ul>
        </div>
      </article>
      <article className="h-fit bg-white py-2 rounded-r-lg">
        <header className="border-b border-gray-300/90 py-2 pb-4 px-4">
          <h3 className="text-xl font-medium text-gray-800">
            Historial clínico
          </h3>
        </header>
        <form className="text-sm p-4 pb-2" action="">
          <div className="bg-blue-50/80 p-4 rounded-md">
            <div className="mb-6">
              <label className="font-semibold">Observaciones</label>
              <p className="text-gray-700 mt-2">
                {clinicalHistoryData.observations}
              </p>
            </div>

            <div className="mb-6">
              <label className="font-semibold">Intervenciones realizadas</label>
              <div className="mt-2">
                {clinicalHistoryData.interventions.map((parrafo, index) => (
                  <p key={index} className="text-gray-700">
                    {parrafo}
                  </p>
                ))}
              </div>
            </div>

            <div>
              <label className="font-semibold">
                Indicaciones para la próxima sesión
              </label>
              <div className="mt-2">
                {clinicalHistoryData.indications.map((parrafo, index) => (
                  <p key={index} className="text-gray-700">
                    {parrafo}
                  </p>
                ))}
              </div>
            </div>
          </div>
        </form>
      </article>
    </section>
  );
}

function MedicalBackgroundTab() {
  return (
    <div className="grid grid-cols-3 gap-4 bg-gray-200/60 p-4 rounded-b-2xl rounded-t-2xl">
      <article className="h-fit bg-white py-2 rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">Tutor 1</h3>
          <button className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer">
            <PencilIcon />
          </button>
        </header>
        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2" action="">
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Nombre y apellido
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.name}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Relación</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.relationship}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Teléfono</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.phone}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">Domicilio</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.residence}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Correo electrónico
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.email}
            </p>
          </div>
        </form>
      </article>

      <article className="bg-white py-2 h-fit rounded-lg">
        <header className="flex justify-between items-center border-b border-gray-300/90 pb-2 px-4">
          <h3 className="text-xl font-medium text-gray-800">Escuela</h3>
          <button className="p-2 text-gray-600 hover:text-blue-600 cursor-pointer">
            <PencilIcon />
          </button>
        </header>

        <form className="text-sm grid grid-cols-2 gap-4 p-4 pb-2">
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">Institución</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.school.name}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Nivel</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.school.level}
            </p>
          </div>
          <div>
            <label className="font-semibold text-blue-600">Turno</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.school.shift}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Directivo de referencia
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.school.director}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">
              Correo electrónico
            </label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.school.email}
            </p>
          </div>
          <div className="col-span-2">
            <label className="font-semibold text-blue-600">Teléfono</label>
            <p className="py-2 px-3 text-gray-600 bg-blue-50/80 rounded-md mt-2">
              {contactData.school.phone}
            </p>
          </div>
        </form>
      </article>
    </div>
  );
}
