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
  const [formData, setFormData] = useState<UserUpdateData>(data);
  const [originalData, setOriginalData] = useState<UserUpdateData>(data);
  const [errors, setErrors] = useState<{ [key: string]: string }>({});

  const validators: { [key: string]: (value: string) => string | null } = {
    name: (value) => {
      if (!value.trim()) return "El nombre es obligatorio";
      if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñüÜ\s'-]+$/.test(value))
        return "Solo caracteres válidos";
      return null;
    },

    lastName: (value) => {
      if (!value.trim()) return "El apellido es obligatorio";
      if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñüÜ\s'-]+$/.test(value))
        return "Solo caracteres válidos";
      return null;
    },

    birthDate: (value) =>
      !value ? "La fecha de nacimiento es obligatoria" : null,

    dni: (value) => {
      if (!value) return "El DNI es obligatorio";
      if (!/^[0-9]{7,8}$/.test(value)) return "El DNI debe tener 7 u 8 números";
      return null;
    },

    email: (value) => {
      if (!value) return "El correo es obligatorio";
      if (!/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/.test(value))
        return "Correo inválido";
      return null;
    },

    phoneNumber: (value) => {
      if (!value) return "El teléfono es obligatorio";
      if (!/^\+?\d{10,15}$/.test(value.replace(/\s/g, "")))
        return "Teléfono inválido";
      return null;
    },

    address: (value) => (!value.trim() ? "La dirección es obligatoria" : null),
  };

  const handleChange = (key: keyof UserUpdateData, value: string) => {
    setFormData({ ...formData, [key]: value });
    if (validators[key]) {
      const error = validators[key](value);
      setErrors((prev) => ({ ...prev, [key]: error || "" }));
    }
  };

  const handleSave = () => {
    const newErrors: { [key: string]: string } = {};

    fields.forEach((field) => {
      const value = formData[field.key] || "";
      if (validators[field.key]) {
        const error = validators[field.key](value);
        if (error) newErrors[field.key] = error;
      }
    });

    setErrors(newErrors);

    if (Object.keys(newErrors).length > 0) return;

    onSave(formData);
    setOriginalData(formData);
    setIsEditMode(false);
  };

  const handleBack = () => {
    setFormData(originalData);
    setIsEditMode(false);
    setErrors({});
  };

  return (
    <article className="border border-gray-300 rounded-xl  overflow-hidden">
      <header className="flex justify-between items-center p-4 px-6 border-b border-gray-300">
        <h2 className="text-lg font-medium text-gray-700">{title}</h2>
        {!isEditMode ? (
          <button
            onClick={() => setIsEditMode(true)}
            className="flex gap-2 items-center border border-black rounded-full p-2 px-4 text-[#0072c3] hover:bg-blue-50 cursor-pointer transition-colors duration-300"
          >
            <PencilIcon />
            Editar
          </button>
        ) : (
          <div className="flex justify-end space-x-4 bg-gray-50">
            <button
              onClick={handleBack}
              className="border border-black rounded-full p-2 px-6 text-[#0072c3] hover:bg-blue-50 cursor-pointer transition-colors duration-300"
            >
              Atrás
            </button>
            <button
              onClick={handleSave}
              className="border border-[#0072c3] bg-[#0072c3] rounded-full p-2 px-6 text-white hover:bg-[#147dc8] hover:shadow-md cursor-pointer transition-colors duration-300"
            >
              Guardar
            </button>
          </div>
        )}
      </header>

      <div className="p-6 pb-8">
        <form className="grid grid-cols-2 gap-4 gap-x-6">
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
                    onChange={(e) => {
                      if (field.key === "dni") {
                        const val = e.target.value.replace(/[^0-9]/g, "");
                        handleChange(field.key, val);
                      } else if (field.key === "phoneNumber") {
                        const val = e.target.value.replace(/[^0-9+\s]/g, "");
                        handleChange(field.key, val);
                      } else {
                        handleChange(field.key, e.target.value);
                      }
                    }}
                    className={`w-full p-3 border rounded focus:ring-blue-500 focus:border-blue-500 mt-3 ${
                      errors[field.key] ? "border-red-500" : ""
                    }`}
                  />
                  {errors[field.key] && (
                    <span className="text-red-500 text-xs mt-1 block">
                      {errors[field.key]}
                    </span>
                  )}
                </div>
              ) : (
                <div>
                  <p className="pl-2 text-sm font-medium text-[#0072c3]">
                    {field.label}
                  </p>
                  <p className="mt-2 p-2 bg-gray-100 rounded-lg">
                    {formData[field.key] || "No especificado"}
                  </p>
                </div>
              )}
            </div>
          ))}
        </form>
      </div>
    </article>
  );
};
