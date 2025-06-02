import {
  personalFields,
  contactFields,
  professionalFields,
  passwordField,
} from "../../config/fieldsConfig";
import type { Field } from "../../components/shared/layouts/RegisterForm";

export const professionalStepFields: Record<number, Field[]> = {
  1: personalFields,
  2: contactFields,
  3: professionalFields,
  4: passwordField,
};
