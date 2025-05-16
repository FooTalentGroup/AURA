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
    <article className="border border-gray-300 rounded-xl  overflow-hidden">
      <header className="flex justify-between items-center p-4 px-6 border-b border-gray-300">
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
      </header>

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
    </article>
  );
};
