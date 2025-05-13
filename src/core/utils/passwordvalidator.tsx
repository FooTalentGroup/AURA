import React from 'react';

/**
 * validatePassword: valida criterios de complejidad de contraseña.
 * Retorna un objeto con flags: length, uppercase, lowercase, number.
 */
export const validatePassword = (password: string) => ({
  length: password.length >= 8,
  uppercase: /[A-Z]/.test(password),
  lowercase: /[a-z]/.test(password),
  number: /[0-9]/.test(password),
});

interface PasswordErrorListProps {
  lengthValid: boolean;
  lowercaseValid: boolean;
  uppercaseValid: boolean;
  numberValid: boolean;
  skipUpper?: boolean;
}

/**
 * PasswordErrorList: muestra la lista de errores de complejidad.
 * Úsalo en formularios de creación o cambio de contraseña.
 */
export const PasswordErrorList: React.FC<PasswordErrorListProps> = ({
  lengthValid,
  lowercaseValid,
  uppercaseValid,
  numberValid,
  skipUpper = false,
}) => (
  <ul className="text-xs text-red-600 mt-2 space-y-1 list-disc list-inside ">
    <li className={lengthValid ? 'text-green-600' : ''}>
      Debe tener mínimo 8 caracteres
    </li>
    <li className={lowercaseValid ? 'text-green-600' : ''}>
      Al menos una letra minúscula
    </li>
    {!skipUpper && (
      <li className={uppercaseValid ? 'text-green-600' : ''}>
        Al menos una letra mayúscula
      </li>
    )}
    <li className={numberValid ? 'text-green-600' : ''}>
      Al menos un número
    </li>
  </ul>
);