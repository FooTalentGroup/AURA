import { createContext } from "react";
import { AuthContextType } from "../../../store/authStore";

export const AuthContext = createContext<AuthContextType | undefined>(
  undefined
);
