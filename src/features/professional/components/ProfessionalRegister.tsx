import React, { useState } from 'react';
import { RegisterForm, Field } from '../../../components/shared/layouts/RegisterForm';
import DashboardLayout from '../../../layouts/DashboardLayout';
import { RegisterProfessionalPayload } from '../../auth/types/auth.types';
import { useRegisterProfessional } from '../hooks/useRegisterProfessional';
import { validatePassword, PasswordErrorList } from '../../../core/utils/passwordvalidator';
import { Modal, LoadingResultModal } from '../../../layouts/Modal';

// Campos por paso
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
    { name: 'specialty', label: 'Especialidad', type: 'select', options: [
      { value: 'Occupational Therapy', label: 'Terapia Ocupacional' },
      { value: 'Psychology', label: 'Psicología' },
      { value: 'psychopedagogy', label: 'psicopedagogía' },
      { value: 'fonoaudiology', label: 'fonoaudiologia' },

    ] },
    { name: 'licenseNumber', label: 'Matrícula', type: 'text' },
  ],
  4: [
    { name: 'password', label: 'Contraseña', type: 'password' },
  ],
};



export const UserRegister: React.FC = () => {
  const totalSteps = 4;
  const [currentStep, setCurrentStep] = useState(1);
  const [values, setValues] = useState<Partial<RegisterProfessionalPayload>>({ patientIds: [] });

  const { register, loading, error  } = useRegisterProfessional();

  const handleChange = (name: string, value: any) => {
    setValues(prev => ({ ...prev, [name]: value }));
  };
  const handleNext = () => setCurrentStep(s => Math.min(s + 1, totalSteps));
  const handleBack = () => setCurrentStep(s => Math.max(s - 1, 1));

  const password = values.password ?? '';
  const { length, uppercase, lowercase, number } = validatePassword(password);
  const passwordValid = length && uppercase && lowercase && number;
  const [modalOpen, setModalOpen] = useState(false);
const [success, setSuccess] = useState(false);


  const handleSubmit = async () => {
     if (!passwordValid) return;
      setModalOpen(true); 
    try {
    await register(values as RegisterProfessionalPayload);
    setSuccess(true);      // ← marcas éxito
  } catch {
    // opcional: setSuccess(false) o manejar error
  }
};

  const subtitles = ['Datos personales', 'Datos de contacto', 'Datos laborales', 'Crear contraseña'];

  return (<DashboardLayout>
    <>
      <RegisterForm
        title="Alta de nuevo profesional"
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

      {/* Modal de loading / éxito */}
      <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)}>
        <LoadingResultModal
          loading={loading}
          success={success}
          errorMessage={error}
          message="¡Listo! El usuario ya está registrado."
          onSecondaryAction={{
            label: 'Registrar otro',
            callback: () => {
              setModalOpen(false);
              setCurrentStep(1);
              setValues({ patientIds: [] });
              setSuccess(false);
            },
          }}
          
        />
      </Modal>
    </>
  </DashboardLayout>
  );
};
