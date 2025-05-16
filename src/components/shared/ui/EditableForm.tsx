import { useState } from "react";
import { PencilIcon } from "./Icons";
import {
  EditableField,
  UserUpdateData,
} from "../../../features/profile/types/profile.type";

export const EditableForm = ({
  title,
  fields,
  data,
  onSave,
}: {
  title: string;
  fields: EditableField[];
  data: UserUpdateData;
  onSave: (updatedData: UserUpdateData) => void;
}) => {
  const [isEditMode, setIsEditMode] = useState(false);
  const [formData, setFormData] = useState<any>(data);
  const [originalData, setOriginalData] = useState<any>(data);

  const handleChange = (key: string, value: string) => {
    setFormData({ ...formData, [key]: value });
  };

  const handleSave = () => {
    onSave(formData);
    setOriginalData(formData);
    setIsEditMode(false);
  };

  const handleBack = () => {
    setFormData(originalData);
    setIsEditMode(false);
  };

  return (
    <div className="border border-gray-300 rounded-xl  overflow-hidden">
      <div className="flex justify-between items-center p-4 px-6 border-b border-gray-300">
        <h2 className="text-lg font-medium text-gray-700">{title}</h2>
        {!isEditMode ? (
          <button
            onClick={() => setIsEditMode(true)}
            className="flex gap-2 items-center border border-black rounded-full p-2 px-4 text-blue-600 hover:bg-blue-50 cursor-pointer transition-colors duration-300"
          >
            <PencilIcon />
            Editar
          </button>
        ) : (
          <div className="flex justify-end space-x-4 bg-gray-50">
            <button
              onClick={handleBack}
              className="border border-black rounded-full p-2 px-6 text-blue-600 hover:bg-blue-50 cursor-pointer transition-colors duration-300"
            >
              Atrás
            </button>
            <button
              onClick={handleSave}
              className="border border-blue-600 bg-blue-600 rounded-full p-2 px-6 text-white hover:bg-blue-700 hover:shadow-md cursor-pointer transition-colors duration-300"
            >
              Guardar
            </button>
          </div>
        )}
      </div>

      <div className="p-6 pb-8">
        <div className="grid grid-cols-2 gap-4 gap-x-6">
          {fields.map((field) => (
            <div key={field.key}>
              {isEditMode ? (
                <div className="relative">
                  <label className="absolute block text-sm font-medium bg-white px-1 text-gray-500 top-0 left-2">
                    {field.label}
                  </label>
                  <input
                    type={field.type || "text"}
                    value={formData[field.key] || ""}
                    onChange={(e) => handleChange(field.key, e.target.value)}
                    className="w-full p-3 border rounded focus:ring-blue-500 focus:border-blue-500 mt-3"
                  />
                </div>
              ) : (
                <div>
                  <p className="pl-2 text-sm font-medium text-blue-600">
                    {field.label}
                  </p>
                  <p className="mt-2 p-2 bg-gray-100 rounded-lg">
                    {formData[field.key] || "No especificado"}
                  </p>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

// Componente principal que muestra ambos formularios
// export default function UserProfilePage() {
//   // Estado para los datos del usuario
//   // const [userData, setUserData] = useState<User>({
//   //   id: 1,
//   //   email: "admin@example.com",
//   //   name: "Ryan",
//   //   lastName: "Gonzales",
//   //   birthDate: null,
//   //   dni: null,
//   //   roles: ["ADMIN"],
//   //   phone: "+54 11 4901 2345", // Para ejemplo
//   //   address: "Tucumán 680, Quilmes", // Para ejemplo
//   // });

//   // Campos para el formulario de información básica
//   const personalFields = [
//     { key: "name", label: "Nombre" },
//     { key: "lastName", label: "Apellido" },
//     { key: "birthDate", label: "Fecha de nacimiento", type: "date" },
//     { key: "dni", label: "DNI" },
//   ];

//   // Campos para el formulario de contacto
//   const contactFields = [
//     { key: "email", label: "Correo electrónico", type: "email" },
//     { key: "phone", label: "Teléfono" },
//     { key: "address", label: "Dirección" },
//   ];

//   // Función para actualizar información básica
//   const handleSavePersonalInfo = (updatedData: Partial<User>) => {
//     console.log("Actualizando información básica:", updatedData);

//     // Aquí harías la llamada PUT a tu API
//     // axios.put(`/api/users/${userData.id}`, {
//     //   name: updatedData.name,
//     //   lastName: updatedData.lastName,
//     //   birthDate: updatedData.birthDate,
//     //   dni: updatedData.dni
//     // })
//     // .then(response => {
//     //   setUserData({...userData, ...updatedData});
//     // });

//     // Por ahora actualizamos directamente el estado
//     setUserData({ ...userData, ...updatedData });
//   };

//   // Función para actualizar información de contacto
//   const handleSaveContactInfo = (updatedData: Partial<User>) => {
//     console.log("Actualizando información de contacto:", updatedData);

//     // Aquí harías la llamada PUT a tu API
//     // axios.put(`/api/users/${userData.id}`, {
//     //   email: updatedData.email,
//     //   phone: updatedData.phone,
//     //   address: updatedData.address
//     // })
//     // .then(response => {
//     //   setUserData({...userData, ...updatedData});
//     // });

//     // Por ahora actualizamos directamente el estado
//     setUserData({ ...userData, ...updatedData });
//   };

//   return (
//     <div className="max-w-4xl mx-auto py-8 px-4">
//       <h1 className="text-3xl font-bold mb-8 text-center">Perfil de Usuario</h1>

//       <div className="space-y-6">
//         <div>
//           <h2 className="text-2xl font-bold mb-4 text-center">Formulario 1</h2>
//           <EditableForm
//             title="Información básica"
//             fields={personalFields}
//             data={userData}
//             onSave={handleSavePersonalInfo}
//           />
//         </div>

//         <div>
//           <h2 className="text-2xl font-bold mb-4 text-center">Formulario 2</h2>
//           <EditableForm
//             title="Información de contacto"
//             fields={contactFields}
//             data={userData}
//             onSave={handleSaveContactInfo}
//           />
//         </div>
//       </div>
//     </div>
//   );
// }
