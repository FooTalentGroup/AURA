import { request } from "../../../core/services/api"; // asegurate de tener el helper
import { Admin } from "../../auth/types/auth.types";

export const getAdmin = () =>
  request<Admin[]>(`/user/all_admin`, { method: "GET" });