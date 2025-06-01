import React from "react";
export type FieldOption = {
  value: string | number;
  label: string;
};

export type Field = {
  name: string;
  label: string;
  type: "text" | "select" | "number" | "email" | "tel" | "date" | "password";
  options?: FieldOption[];
};

export interface GenericFormLayoutProps {
  title: string;
  subtitle?: string;
  currentStep: number;
  totalSteps: number;
  fields: Field[];
  values: Record<string, any>;
  onChange: (name: string, value: any) => void;
  onNext?: () => void;
  onBack?: () => void;
  onSubmit?: () => void;
  extraAction?: React.ReactNode;
  nextLabel?: string;
  backLabel?: string;
  submitLabel?: string;
  onClose?: () => void;
  backendErrors?: Record<string, string>;
}

export const RegisterForm: React.FC<GenericFormLayoutProps> = ({
  title,
  subtitle,
  currentStep,
  totalSteps,
  fields,
  values,
  onChange,
  onNext,
  onBack,
  onSubmit,
  extraAction,
  onClose,
  nextLabel = 'Siguiente',
  backLabel = 'Atrás',
  submitLabel = 'Enviar',
  backendErrors = {},
}) => {
  const isLast = currentStep === totalSteps;

  // Simulación de verificación de existencia previa (reemplazar por llamada real a API)
  const checkIfExists = async (name: string, value: string) => {
    // Aquí deberías hacer la consulta real a la API
    // Simulación: si el valor termina en 99, lo consideramos existente
    if (["dni", "email", "phoneNumber", "cuil", "cuit"].includes(name) && value && value.endsWith("99")) {
      return true;
    }
    return false;
  };

  const handleBlur = async (name: string, value: string) => {
    setTouched((prev) => ({ ...prev, [name]: true }));
    // Validación de requerido
    if (!value) {
      setFieldErrors((prev) => ({ ...prev, [name]: "Este campo no puede estar vacío" }));
      return;
    }
    // Validación de existencia previa
    if (["dni", "email", "phoneNumber", "cuil", "cuit"].includes(name)) {
      const exists = await checkIfExists(name, value);
      if (exists) {
        setFieldErrors((prev) => ({ ...prev, [name]: "Este dato ya está registrado" }));
        return;
      }
    }
    setFieldErrors((prev) => {
      const { [name]: _omit, ...rest } = prev;
      return rest;
    });
  };

  // Validar todos los campos del paso actual
  const validateAllFields = async () => {
    let hasError = false;
    const newTouched: Record<string, boolean> = { ...touched };
    const newErrors: Record<string, string> = { ...fieldErrors };
    for (const f of fields) {
      const value = values[f.name] || "";
      newTouched[f.name] = true;
      if (!value) {
        newErrors[f.name] = "Este campo no puede estar vacío";
        hasError = true;
        continue;
      }
      if (["dni", "email", "phoneNumber", "cuil", "cuit"].includes(f.name)) {
        const exists = await checkIfExists(f.name, value);
        if (exists) {
          newErrors[f.name] = "Este dato ya está registrado";
          hasError = true;
          continue;
        }
      }
      // Si no hay error, limpiar
      if (newErrors[f.name]) delete newErrors[f.name];
    }
    setTouched(newTouched);
    setFieldErrors(newErrors);
    return !hasError;
  };

  // Modificar el handleSubmit para validar antes de avanzar
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const valid = await validateAllFields();
    if (!valid) return;
    if (isLast && onSubmit) {
      onSubmit();
    } else {
      onNext?.();
    }
  };

  React.useEffect(() => {
    if (backendErrors) {
      setTouched((prev) => ({ ...prev, ...Object.keys(backendErrors).reduce((acc, k) => ({ ...acc, [k]: true }), {}) }));
      setFieldErrors((prev) => ({ ...prev, ...backendErrors }));
    }
  }, [backendErrors]);

  // porcentaje de progreso basado en segmentos completados
  const progressPercent = totalSteps > 1
    ? ((currentStep - 1) / (totalSteps - 1)) * 100
    : 0;

  const [touched, setTouched] = React.useState<Record<string, boolean>>({});
  const [fieldErrors, setFieldErrors] = React.useState<Record<string, string>>({});

  return (
  <div className="mt-8 bg-white rounded-3xl  border-gray-200  p-6 shadow-sm w-full min-h-[710px]">
      {/* Título */}
      <div className="flex justify-between items-center mb-10 border-b border-gray-200 p-4  ">
        <h2 className="text-2xl font-medium">{title}</h2>
        {/* Botón X para cerrar, a la derecha */}
        <button
          type="button"
          onClick={onClose || onBack}
          className="text-gray-400 hover:text-gray-600 ml-2"
          title="Cerrar"
        >
          <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
            <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <div className="max-w-xl mx-auto">
        {/* Progress bar + step circles */}
        <div className="relative mb-6 ">
          {/* Barra base y progreso */}
          <div className="absolute inset-0 flex items-center">
            <div className="w-full h-1 bg-gray-200 rounded" />
            <div
              className="absolute h-1 bg-[#198038] rounded"
              style={{ width: `${progressPercent}%` }}
            />
          </div>
          {/* Círculos */}
          <div className="relative flex justify-between">
            {Array.from({ length: totalSteps }).map((_, idx) => {
              const num = idx + 1;
              const done = num < currentStep;
              const active = num === currentStep;
              return (
                <div
                  key={num}
                  className="w-8 h-8 flex items-center justify-center rounded-full border-2 z-10"
                  style={{
                    borderColor: done || active ? "#198038" : "#D1D5DB",
                    backgroundColor: done
                      ? "#198038"
                      : active
                      ? "#FFFFFF"
                      : "#FFFFFF",
                  }}
                >
                  {done ? (
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      className="h-4 w-4 text-white"
                      fill="none"
                      viewBox="0 0 24 24"
                      stroke="currentColor"
                      strokeWidth={3}
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        d="M5 13l4 4L19 7"
                      />
                    </svg>
                  ) : (
                    <span
                      className="text-sm font-medium"
                      style={{ color: active ? "#198038" : "#9CA3AF" }}
                    >
                      {num}
                    </span>
                  )}
                </div>
              );
            })}
          </div>
        </div>

        {/* Subtítulo de sección */}
        {subtitle && (
          <div className="flex items-center  mb-4">
            {/* circulito */}
            <span className="inline-block w-2 h-2 border border-black-800 rounded-full mr-2 flex-shrink-0" />
            {/* texto */}
            <p className="text-black text-[1.75rem]">{subtitle}</p>
          </div>
        )}

        {/* Formulario */}
        <form onSubmit={handleSubmit} className="space-y-5">
          {fields.map((f) => {
            const error = touched[f.name] && fieldErrors[f.name];
            return (
              <div key={f.name} className="flex flex-col">
                <label htmlFor={f.name} className="mb-2 text-sm font-medium">
                  {f.label}
                </label>
                {f.type === "select" && f.options ? (
                  <select
                    id={f.name}
                    value={values[f.name] || ""}
                    onChange={(e) => onChange(f.name, e.target.value)}
                    onBlur={() => handleBlur(f.name, values[f.name] || "")}
                    className={`border rounded px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-200 ${error ? 'border-red-500' : 'border-gray-300'}`}
                  >
                    <option value="">-- Seleccione --</option>
                    {f.options.map((o) => (
                      <option key={o.value} value={o.value}>
                        {o.label}
                      </option>
                    ))}
                  </select>
                ) : (
                  <input
                    id={f.name}
                    type={f.type}
                    value={values[f.name] || ""}
                    onChange={(e) => onChange(f.name, e.target.value)}
                    onBlur={() => handleBlur(f.name, values[f.name] || "")}
                    className={`border rounded px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-200 ${error ? 'border-red-500' : 'border-gray-300'}`}
                  />
                )}
                {/* Mostrar error si existe */}
                {error && (
                  <p className="mt-2 text-sm text-red-600">
                    {fieldErrors[f.name]}
                  </p>
                )}
              </div>
            );
          })}

          {extraAction && (
            <div className="text-sm  pointer-events: none">{extraAction}</div>
          )}

          {/* Botonera */}
          <div className="flex items-center mt-6 pt-4">
            {onBack && (
              <button
                type="button"
                onClick={onBack}
                className="px-5 py-2 border border-gray-600 text-blue-400 rounded-full hover:bg-blue-50 cursor-pointer"
              >
                {backLabel}
              </button>
            )}
            <button
              type="submit"
              className={`ml-auto px-6 py-2 rounded-full text-[#00539A] shadow-sm cursor-pointer ${
                isLast
                  ? "bg-[#E5F6FF] hover:bg-[#7ab3d1]"
                  : "bg-[#E5F6FF] hover:bg-[#7ab3d1]"
              }`}
            >
              {isLast ? submitLabel : nextLabel}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
