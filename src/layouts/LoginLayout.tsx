import React from 'react';

interface Props { children: React.ReactNode; }

const LoginLayout: React.FC<Props> = ({ children }) => (
  <div className="flex">
    {/* IZQUIERDA: 40% */}
    <div className="w-full md:w-2/5 h-screen bg-white flex items-center justify-center p-8 ">
      <div className="w-full max-w-md">
        {children}
      </div>
    </div>

    {/* DERECHA: 60% */}
    <div className="hidden md:block md:w-3/5 h-screen ">
      <img
        src="/holding-hands.jpg"
        alt="Manos unidas"
        className="w-full h-full object-cover object-center"
        draggable="false"
      />
    </div>
  </div>
);

export default LoginLayout;