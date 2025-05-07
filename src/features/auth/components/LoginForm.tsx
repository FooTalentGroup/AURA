import React, { useState, FormEvent } from 'react';
import { FiEye, FiEyeOff, FiAlertCircle } from 'react-icons/fi';
import { useNavigate } from 'react-router-dom';
import { useContextAuth } from '../hooks/useContextAuth';

const validateEmail = (email: string) =>
  /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

const validatePassword = (password: string) => ({
  length: password.length >= 8,
  uppercase: /[A-Z]/.test(password),
  lowercase: /[a-z]/.test(password),
  number: /[0-9]/.test(password),
});

const LoginForm: React.FC = () => {
  const [show, setShow] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [touched, setTouched] = useState({ email: false, password: false });
  const { login, state } = useContextAuth();
  const navigate = useNavigate();

  // <-- Aquí pones el/los email(s) que quieres eximir de la regla de mayúscula
  const exemptUsers = ['admin@example.com', "Dario@example.com", "secretarias@example.com"];

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setTouched({ email: true, password: true });

    const isEmailValid = validateEmail(email);
    const pwdVal = validatePassword(password);

    // si está eximido, forzamos uppercase=true
    const skipUpper = exemptUsers.includes(email.toLowerCase());
    const passUpper = skipUpper ? true : pwdVal.uppercase;

    // validación final
    const isPasswordValid =
      pwdVal.length && pwdVal.lowercase && pwdVal.number && passUpper;

    if (!isEmailValid || !isPasswordValid) return;

    await login(email, password);
    if (state.isAuthenticated) navigate('/dashboard');
  };

  const pwdVal = validatePassword(password);
  const emailError = touched.email && !validateEmail(email);
  const skipUpper = exemptUsers.includes(email.toLowerCase());
  const passwordError =
    touched.password &&
    (!pwdVal.length || !pwdVal.lowercase || !pwdVal.number || (!skipUpper && !pwdVal.uppercase));

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <h1 className="text-2xl font-medium text-gray-800 text-center">
        Bienvenido a AURA
      </h1>

      {state.error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-2 rounded">
          {state.error}
        </div>
      )}

      {/* Email */}
      <div className="relative">
        <label htmlFor="email" className="absolute -top-2 left-4 bg-[#F7F8FA] px-1 text-xs text-gray-600">
          Correo electrónico
        </label>
        <input
          id="email"
          type="email"
          placeholder="ejemplo@gmail.com"
          value={email}
          onChange={e => setEmail(e.target.value)}
          onBlur={() => setTouched(t => ({ ...t, email: true }))}
          required
          className={`w-full h-12 border rounded-lg px-4 placeholder-gray-400 focus:outline-none ${
            emailError ? 'border-red-500 focus:border-red-500' : 'border-gray-400 focus:border-blue-600'
          }`}
        />
        {emailError && (
          <p className="text-xs text-red-600 mt-1">
            Por favor, ingresa un correo electrónico válido
          </p>
        )}
      </div>

      {/* Contraseña */}
      <div className="relative">
        <label htmlFor="password" className="absolute -top-2 left-4 bg-[#F7F8FA] px-1 text-xs text-gray-600">
          Contraseña
        </label>
        <input
          id="password"
          type={show ? 'text' : 'password'}
          placeholder="********"
          value={password}
          onChange={e => setPassword(e.target.value)}
          onBlur={() => setTouched(t => ({ ...t, password: true }))}
          required
          className={`w-full h-12 border rounded-lg px-4 pr-12 placeholder-gray-400 focus:outline-none ${
            passwordError ? 'border-red-500 focus:border-red-500' : 'border-gray-400 focus:border-blue-600'
          }`}
        />
        <button
          type="button"
          onClick={() => setShow(s => !s)}
          className="absolute inset-y-0 right-10 flex items-center text-gray-500"
        >
          {show ? <FiEye size={20} /> : <FiEyeOff size={20} />}
        </button>

        {passwordError && (
          <>
            <FiAlertCircle className="absolute right-3 top-3 text-red-500" size={20} />
            <ul className="text-xs text-red-600 mt-2 space-y-1 list-disc list-inside">
              <li className={pwdVal.length ? 'text-green-600' : ''}>
                Deberá contener mínimo 8 caracteres
              </li>
              <li className={pwdVal.lowercase ? 'text-green-600' : ''}>
                Al menos uno en minúscula
              </li>
              {/* Sólo mostramos esta regla si NO es usuario eximido */}
              {!skipUpper && (
                <li className={pwdVal.uppercase ? 'text-green-600' : ''}>
                  Al menos uno en mayúscula
                </li>
              )}
              <li className={pwdVal.number ? 'text-green-600' : ''}>
                Al menos un número
              </li>
            </ul>
          </>
        )}
      </div>

      <div className="text-left">
        <a href="/forgot-password" className="text-sm text-[#006DE0] hover:underline">
          Olvidé mi contraseña
        </a>
      </div>

      <button
        type="submit"
        disabled={state.isLoading}
        className="w-full h-12 flex items-center justify-center font-semibold rounded-full bg-[#006DE0] hover:bg-[#0059B2] text-white transition disabled:opacity-50"
      >
        {state.isLoading ? 'Cargando...' : 'Iniciar sesión'}
      </button>
    </form>
  );
};

export default LoginForm;