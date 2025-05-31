import {
  personalFields,
  insuranceFields,
  tutorFields,
  schoolFields,
} from "../../config/fieldsConfig";
import type { Field } from "../../components/shared/layouts/RegisterForm";

export const patientStepFields: Record<number, Field[]> = {
  1: personalFields,
  2: insuranceFields,
  3: tutorFields,
  4: schoolFields,
};
