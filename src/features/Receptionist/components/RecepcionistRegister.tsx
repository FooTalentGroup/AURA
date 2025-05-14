import React, { useState } from 'react';
import DashboardLayout from '../../../layouts/DashboardLayout';
import { RegisterForm, Field } from '../../../components/shared/layouts/RegisterForm';
import { validatePassword, PasswordErrorList } from '../../../core/utils/passwordvalidator';
import { Modal } from '../../../layouts/Modal';
import { LoadingResultModal } from '../../../layouts/Modal';
import { useRegisterReceptionist } from '../hooks/useRegisterReceptionist';
import type { RegisterReceptionistPayload } from '../../auth/types/auth.types';

// Definición de campos por paso para recepcionista
const stepFields: Record<number, Field[]> = {
  1: [
    { name: 'name', label: 'Nombre', type: 'text' },
    { name: 'lastName', label: 'Apellido', type: 'text' },
    { name: 'birthDate', label: 'Fecha de nacimiento', type: 'date' },
    { name: 'dni', label: 'DNI', type: 'text' },
  ],
  2: [
    { name: 'email', label: 'Correo electrónico', type: 'email' },
    { name: 'phoneNumber', label: 'Teléfono', type: 'tel' },
    { name: 'address', label: 'Dirección', type: 'text' },
    { name: 'locality', label: 'Localidad', type: 'text' },
  ],
  3: [
    { name: 'cuil', label: 'CUIL/CUIT', type: 'text' },
  ],
  4: [
    { name: 'password', label: 'Contraseña', type: 'password' },
  ],
};

export const ReceptionistRegister: React.FC = () => {
  const totalSteps = 4;
  const [currentStep, setCurrentStep] = useState(1);
  const [values, setValues] = useState<Partial<RegisterReceptionistPayload>>({});
  const { register, loading, error } = useRegisterReceptionist();
  const [modalOpen, setModalOpen] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleChange = (name: string, value: any) => {
    setValues(prev => ({ ...prev, [name]: value }));
  };
  const handleNext = () => setCurrentStep(s => Math.min(s + 1, totalSteps));
  const handleBack = () => setCurrentStep(s => Math.max(s - 1, 1));

  const password = (values.password as string) || '';
  const { length, uppercase, lowercase, number } = validatePassword(password);
  const passwordValid = length && uppercase && lowercase && number;

  const subtitles = ['Datos personales', 'Datos de contacto', 'CUIL', 'Crear contraseña'];

  const handleSubmit = async () => {
    if (!passwordValid) return;
    setModalOpen(true);
    try {
      await register(values as RegisterReceptionistPayload);
      setSuccess(true);
    } catch {
      setSuccess(false);
    }
  };

  return (
    <DashboardLayout>
      <>
        <RegisterForm
          title="Alta de nueva recepcionista"
          onClose={() => history.back()}
          subtitle={subtitles[currentStep - 1]}
          currentStep={currentStep}
          totalSteps={totalSteps}
          fields={stepFields[currentStep]}
          values={values}
          onChange={handleChange}
          onBack={currentStep > 1 ? handleBack : undefined}
          onNext={currentStep < totalSteps ? handleNext : undefined}
          onSubmit={currentStep === totalSteps ? handleSubmit : undefined}
          backLabel="Atrás"
          nextLabel="Siguiente"
          submitLabel={loading ? 'Cargando...' : 'Crear Usuario'}
          extraAction={
            currentStep === 4 && password.length > 0 ? (
              <PasswordErrorList
                lengthValid={length}
                uppercaseValid={uppercase}
                lowercaseValid={lowercase}
                numberValid={number}
                skipUpper={false}
              />
            ) : undefined
          }
        />

        <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)}>
          <LoadingResultModal
            loading={loading}
            success={success}
            errorMessage={error || undefined}
            message="¡Listo! La recepcionista ya está registrada."
            onSecondaryAction={{
              label: 'Registrar otra',
              callback: () => {
                setModalOpen(false);
                setCurrentStep(1);
                setValues({});
                setSuccess(false);
              },
            }}
           
          />
        </Modal>
      </>
    </DashboardLayout>
  );
};
