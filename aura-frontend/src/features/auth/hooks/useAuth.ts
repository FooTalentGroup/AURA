import { useContextAuth as useAuthStore } from "./useContextAuth";

export const useAuth = () => {
  const auth = useAuthStore();

  const isAdmin = () => {
    return auth.state.user !== null;
  };

  return {
    ...auth,
    isAdmin,
  };
};
