import { useContextAuth as useAuthStore } from "./useContextAuth";

// Este hook es un wrapper alrededor del hook del store para poder extenderlo
// con funcionalidades adicionales específicas de autenticación
export const useAuth = () => {
  const auth = useAuthStore();

  // Podemos añadir funciones útiles aquí
  const isAdmin = () => {
    // Verificar si el usuario tiene rol de admin (podría extraerse del token)
    return auth.state.user !== null;
  };

  return {
    ...auth,
    isAdmin,
  };
};
