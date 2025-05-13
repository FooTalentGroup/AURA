import React from 'react';

export type FieldOption = {
  value: string | number;
  label: string;
};

export type Field = {
  name: string;
  label: string;
  type: 'text' | 'select' | 'number' | 'email' | 'tel' | 'date' | 'password' ;
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
  onNext: () => void;
  onBack?: () => void;
  onSubmit?: () => void;
  extraAction?: React.ReactNode;         
  nextLabel?: string;
  backLabel?: string;
  submitLabel?: string;
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
  nextLabel = 'Siguiente',
  backLabel = 'Atrás',
  submitLabel = 'Enviar',
}) => {
  const isLast = currentStep === totalSteps;
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (isLast && onSubmit) {
      onSubmit();
    } else {
      onNext();
    }
  };

  // porcentaje de progreso basado en segmentos completados
  const progressPercent = totalSteps > 1
    ? ((currentStep - 1) / (totalSteps - 1)) * 100
    : 0;

  return (
    <div className="bg-white rounded-t-2xl p-6 shadow-sm max-w-4xl mx-auto">
      {/* Título */}
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-2xl font-medium">{title}</h2>
      </div>

      {/* Progress bar + step circles */}
      <div className="relative mb-6">
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
                  borderColor: done || active ? '#198038' : '#D1D5DB',
                  backgroundColor: done ? '#198038' : active ? '#FFFFFF' : '#FFFFFF',
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
                    <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
                  </svg>
                ) : (
                  <span
                    className="text-sm font-medium"
                    style={{ color: active ? '#198038' : '#9CA3AF' }}
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
      {subtitle && <p className="mb-4 text-gray-600">{subtitle}</p>}

      {/* Formulario */}
      <form onSubmit={handleSubmit} className="space-y-5">
        {fields.map((f) => (
          <div key={f.name} className="flex flex-col">
            <label htmlFor={f.name} className="mb-2 text-sm font-medium">
              {f.label}
            </label>
            {f.type === 'select' && f.options ? (
              <select
                id={f.name}
                value={values[f.name] || ''}
                onChange={(e) => onChange(f.name, e.target.value)}
                className="border border-gray-300 rounded px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-200"
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
                value={values[f.name] || ''}
                onChange={(e) => onChange(f.name, e.target.value)}
                className="border border-gray-300 rounded px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-200"
              />
            )}
          </div>
        ))}

        {extraAction && <div className="text-sm  pointer-events: none">{extraAction}</div>}

        {/* Botonera */}
        <div className="flex items-center mt-6 pt-4 border-t border-gray-200">
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
              isLast ? 'bg-[#E5F6FF] hover:bg-[#7ab3d1]' : 'bg-[#E5F6FF] hover:bg-[#7ab3d1]'
            }`}
          >
            {isLast ? submitLabel : nextLabel}
          </button>
        </div>
      </form>
    </div>
  );
};
