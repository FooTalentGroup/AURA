import React, { useState } from "react";
import DashboardLayout from "../../../layouts/DashboardLayout";
import {
  RegisterForm,
} from "../../../components/shared/layouts/RegisterForm";
import { Modal, LoadingResultModal } from "../../../layouts/Modal";
import { useRegisterPatient } from "../hooks/useRegisterPatient";
import type { PatientPayload } from "../types/patient.types";
import { patientStepFields as stepFields } from "../config";

export const PatientRegister: React.FC = () => {
  const totalSteps = 4;
  const [currentStep, setCurrentStep] = useState(1);
  const [values, setValues] = useState<
    Partial<
      PatientPayload & {
        schoolName?: string;
        emailSchool?: string;
        phoneSchool?: string;
      }
    >
  >({
    hasInsurance: true,
    professionalIds: [],
  });
  const { register, loading, error } = useRegisterPatient();
  const [modalOpen, setModalOpen] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleChange = (name: string, value: any) => {
    setValues((prev) => ({ ...prev, [name]: value }));
  };
  const handleNext = () => setCurrentStep((s) => Math.min(s + 1, totalSteps));
  const handleBack = () => setCurrentStep((s) => Math.max(s - 1, 1));

  const skipInsurance = () => {
    setValues((prev) => ({
      ...prev,
      hasInsurance: false,
      insuranceName: "Particular",
      insurancePlan: "",
      memberShipNumber: "",
    }));
    handleNext();
  };

  const subtitles = [
    "Datos personales",
    "Obra Social",
    "Tutor",
    "Datos de la institución",
  ];

  const handleSubmit = async () => {
    setModalOpen(true);
    try {
      await register(values as any);
      setSuccess(true);
    } catch {
      setSuccess(false);
    }
  };

  return (
    <DashboardLayout>
      <>
        <RegisterForm
          title="Alta de nuevo paciente"
          subtitle={subtitles[currentStep - 1]}
          currentStep={currentStep}
          totalSteps={totalSteps}
          fields={stepFields[currentStep]}
          values={values}
          onChange={handleChange}
          onBack={currentStep > 1 ? handleBack : undefined}
          onNext={
            currentStep === 2
              ? handleNext
              : currentStep < totalSteps
              ? handleNext
              : undefined
          }
          onSubmit={currentStep === totalSteps ? handleSubmit : undefined}
          backLabel="Atrás"
          nextLabel="Siguiente"
          submitLabel={
            loading
              ? "Cargando..."
              : currentStep === totalSteps
              ? "Registrar paciente"
              : "Siguiente"
          }
          extraAction={
            currentStep === 2 ? (
              <span
                className="text-blue-600 cursor-pointer"
                onClick={skipInsurance}
              >
                Omitir
              </span>
            ) : undefined
          }
        />
        <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)}>
          <LoadingResultModal
            loading={loading}
            success={success}
            errorMessage={error ?? undefined}
            message="¡Paciente registrado con éxito!"
            onSecondaryAction={{
              label: "Registrar otro",
              callback: () => {
                setModalOpen(false);
                setCurrentStep(1);
                setValues({ hasInsurance: true, professionalIds: [] });
                setSuccess(false);
              },
            }}
          />
        </Modal>
      </>
    </DashboardLayout>
  );
};
