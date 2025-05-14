import React, { useState, FormEvent } from 'react';
import { FiEye, FiEyeOff } from 'react-icons/fi';
import { useNavigate } from 'react-router-dom';
import { useContextAuth } from '../hooks/useContextAuth';

const validateEmail = (email: string) =>
  /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

export const LoginForm: React.FC = () => {
  const [show, setShow] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [touchedEmail, setTouchedEmail] = useState(false);
  const { login, state } = useContextAuth();
  const navigate = useNavigate();

  const trimmedEmail = email.trim();
  const hasSpaces = email !== trimmedEmail;
  const emailFormatInvalid = touchedEmail && !validateEmail(trimmedEmail);
  const emailSpaceError = touchedEmail && hasSpaces;
  const canSubmit = !hasSpaces && validateEmail(trimmedEmail) && password.trim() !== '';

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setTouchedEmail(true);

    if (!canSubmit) return;

    const success = await login(trimmedEmail, password);
    if (success) navigate("/patients");
  };

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
        <label htmlFor="email" className="absolute -top-2 left-4 bg-white px-1 text-xs text-gray-600">
          Correo electrónico
        </label>
        <input
          id="email"
          type="text"
          inputMode="email"
          pattern="^\S+@\S+\.\S+$"
          placeholder="ejemplo@gmail.com"
          value={email}
          onChange={e => setEmail(e.target.value)}
          onBlur={() => setTouchedEmail(true)}
          required
          className={`w-full h-12 border rounded-lg px-4 placeholder-gray-400 focus:outline-none focus:border-blue-600 ${
            (emailFormatInvalid || emailSpaceError) ? 'border-red-500' : 'border-gray-300'
          }`}
        />
        {emailSpaceError && (
          <p className="text-xs text-red-600 mt-1">
            No se permiten espacios antes o después del correo
          </p>
        )}
        {!emailSpaceError && emailFormatInvalid && (
          <p className="text-xs text-red-600 mt-1">
            Por favor, ingresa un correo válido
          </p>
        )}
      </div>

      {/* Contraseña */}
      <div className="relative">
        <label htmlFor="password" className="absolute -top-2 left-4 bg-white px-1 text-xs text-gray-600">
          Contraseña
        </label>
        <input
          id="password"
          type={show ? 'text' : 'password'}
          placeholder="********"
          value={password}
          onChange={e => setPassword(e.target.value)}
          onBlur={() => setTouchedEmail(true)}
          required
          className="w-full h-12 border border-gray-300 rounded-lg px-4 pr-12 placeholder-gray-400 focus:outline-none focus:border-blue-600"
        />
        <button
          type="button"
          onClick={() => setShow(s => !s)}
          className="absolute inset-y-0 right-4 flex items-center text-gray-500"
        >
          {show ? <FiEye /> : <FiEyeOff />}
        </button>
      </div>

      <div className="text-left">
        <a href="/forgot-password" className="text-sm text-blue-600 hover:underline">
          Olvidé mi contraseña
        </a>
      </div>

      <button
        type="submit"
        disabled={!canSubmit || state.isLoading}
        className="w-full h-12 flex items-center justify-center font-semibold rounded-full bg-blue-600 hover:bg-blue-700 text-white transition disabled:opacity-50"
      >
        {state.isLoading ? 'Cargando...' : 'Iniciar sesión'}
      </button>
    </form>
  );
};

export default LoginForm;