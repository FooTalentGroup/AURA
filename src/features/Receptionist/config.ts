import {
  personalFields,
  contactFields,
  cuilField,
  passwordField,
} from "../../config/fieldsConfig";
import type { Field } from "../../components/shared/layouts/RegisterForm";

export const receptionistStepFields: Record<number, Field[]> = {
  1: personalFields,
  2: contactFields,
  3: cuilField,
  4: passwordField,
};
