import React, { useState } from 'react';
import { RegisterForm } from '../../../components/shared/layouts/RegisterForm';



export const UserRegister: React.FC = () => {
  const [values, setValues] = useState<Record<string, any>>({});
  const [step, setStep] = useState(1);

  const handleChange = (n: string, v: any) =>
    setValues((prev) => ({ ...prev, [n]: v }));

  return (
    <RegisterForm
       title="Alta de nuevo paciente"
  subtitle="Tutor"
  currentStep={3}
  totalSteps={4}
  fields={[
    { name: 'phone', label: 'Teléfono', type: 'tel' },
    { name: 'email', label: 'Correo electrónico', type: 'email' },
    { name: 'address', label: 'Domicilio', type: 'text' },
  ]}
  values={values}
  onChange={handleChange}
  onBack={() => setStep(2)}
  onNext={() => setStep(4)}
  onSubmit={() => console.log('Submit')}
  extraAction={<span onClick={() => setStep(2)}>Agregar Tutor</span>}
/>
  );
};