import React, { useState } from 'react';
import { RegisterForm } from '../../../components/shared/layouts/RegisterForm';
import DashboardLayout from '../../../layouts/DashboardLayout';
import { RegisterProfessionalPayload } from '../../auth/types/auth.types';
import { useRegisterProfessional } from '../hooks/useRegisterProfessional';
import { validatePassword, PasswordErrorList } from '../../../core/utils/passwordvalidator';
import { Modal, LoadingResultModal } from '../../../layouts/Modal';
import { professionalStepFields as stepFields } from '../config';

export const ProfessionalRegister: React.FC = () => {
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
    setSuccess(true);      
  } catch {
    //  error
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
          errorMessage={error ?? undefined}
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
