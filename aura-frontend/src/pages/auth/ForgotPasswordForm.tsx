import React, { useState, FormEvent } from "react";
import { Link } from "react-router-dom";
import LoginLayout from "../../layouts/LoginLayout";

const validateEmail = (email: string) =>
  /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

const ForgotPasswordForm: React.FC = () => {
  const [email, setEmail] = useState("");
  const [touched, setTouched] = useState(false);
  const [isLoading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setTouched(true);
    setError(null);

    if (!validateEmail(email)) return;

    setLoading(true);
    try {
      const res = await fetch("/api/forgot-password", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email }),
      });
      if (!res.ok) throw new Error(await res.text());
      setSuccess(true);
    } catch (err: unknown) {
      if (err instanceof Error) {
        setError(err.message || "Ocurrió un error, intenta más tarde");
      } else {
        setError("Error desconocido");
      }
    } finally {
      setLoading(false);
    }
  };

  const emailError = touched && !validateEmail(email);

  return (
    <LoginLayout>
      <form onSubmit={handleSubmit} className="space-y-6">
        <h1 className="text-2xl font-medium text-gray-800 text-center">
          Olvidé mi contraseña
        </h1>

        <p className="text-sm text-gray-600 text-center">
          Por favor, ingresa la dirección de correo electrónico a la que te
          gustaría que enviemos el enlace para restablecer tu contraseña.
        </p>

        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-2 rounded">
            {error}
          </div>
        )}
        {success && (
          <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-2 rounded">
            Te enviamos un enlace a <strong>{email}</strong>. Revisa tu bandeja.
          </div>
        )}

        <div className="relative">
          <label
            htmlFor="email"
            className="absolute -top-2 left-4 bg-[#F7F8FA] px-1 text-xs text-gray-600"
          >
            Correo electrónico
          </label>
          <input
            id="email"
            type="email"
            placeholder="ejemplo@gmail.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            onBlur={() => setTouched(true)}
            required
            className={`w-full h-12 border rounded-lg px-4 placeholder-gray-400 focus:outline-none ${
              emailError
                ? "border-red-500 focus:border-red-500"
                : "border-gray-400 focus:border-blue-600"
            }`}
            disabled={success}
          />
          {emailError && (
            <p className="text-xs text-red-600 mt-1">
              Por favor, ingresa un correo electrónico válido
            </p>
          )}
        </div>

        <button
          type="submit"
          disabled={isLoading || success}
          className="w-full h-12 flex items-center justify-center font-semibold rounded-full bg-[#006DE0] hover:bg-[#0059B2] text-white transition disabled:opacity-50"
        >
          {isLoading
            ? "Enviando..."
            : success
            ? "¡Enlace enviado!"
            : "Enviar enlace"}
        </button>

        <div className="text-center">
          <Link to="/login" className="text-sm text-[#006DE0] hover:underline">
            Volver a inicio de sesión
          </Link>
        </div>
      </form>
    </LoginLayout>
  );
};

export default ForgotPasswordForm;
