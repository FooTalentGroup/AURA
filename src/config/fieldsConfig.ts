import type { Field } from "../components/shared/layouts/RegisterForm";

export const personalFields: Field[] = [
  { name: "name",      label: "Nombre",                type: "text"    },
  { name: "lastName",  label: "Apellido",              type: "text"    },
  { name: "birthDate", label: "Fecha de nacimiento",   type: "date"    },
  { name: "dni",       label: "DNI",                   type: "text"    },
];

export const contactFields: Field[] = [
  { name: "email",      label: "Correo electrónico", type: "email" },
  { name: "phoneNumber", label: "Teléfono",          type: "tel"   },
  { name: "address",     label: "Dirección",         type: "text"  },
  { name: "locality",    label: "Localidad",         type: "text"  },
];

export const cuilField: Field[] = [
  { name: "cuil", label: "CUIL/CUIT", type: "text" },
];

export const passwordField: Field[] = [
  { name: "password", label: "Contraseña", type: "password" },
];

export const insuranceFields: Field[] = [
  { name: "insuranceName",     label: "Nombre",      type: "text"   },
  { name: "insurancePlan",     label: "Plan",        type: "text"   },
  { name: "memberShipNumber",  label: "Nro afiliado", type: "text"   },
];

export const tutorFields: Field[] = [
  { name: "tutorName",        label: "Nombre del tutor", type: "text" },
  {
    name: "relationToPatient", label: "Relación",
    type: "select",
    options: [
      { value: "padre", label: "Padre" },
      { value: "madre", label: "Madre" },
      { value: "tutor", label: "Tutor" },
    ],
  },
  { name: "phoneNumber",      label: "Teléfono",         type: "tel"   },
  { name: "email",            label: "Correo Electrónico", type: "email" },
  { name: "address",          label: "Dirección",        type: "text"  },
];

export const schoolFields: Field[] = [
  { name: "schoolName",  label: "Nombre institución", type: "text"  },
  { name: "emailSchool", label: "Email institución",  type: "email" },
  { name: "phoneSchool", label: "Teléfono institución", type: "text" },
];

export const professionalFields: Field[] = [
  { name: "cuil",          label: "CUIL/CUIT",      type: "text"   },
  {
    name: "specialty",    label: "Especialidad",
    type: "select",
    options: [
      { value: "Occupational Therapy", label: "Terapia Ocupacional" },
      { value: "Psychology",            label: "Psicología"          },
      { value: "psychopedagogy",        label: "Psicopedagogía"      },
      { value: "fonoaudiology",         label: "Fonoaudiología"      },
    ],
  },
  { name: "licenseNumber", label: "Matrícula",       type: "text"   },
];
