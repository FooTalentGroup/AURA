import { useState } from "react";
import DashboardLayout from "../../layouts/DashboardLayout";
import ToggleSwitch from "../../components/shared/ui/ToggleSwitch";
import {
  ChevronRightIcon,
  QuestionIcon,
  SquareArrowUpRightIcon,
  UsersIcon,
} from "../../components/shared/ui/Icons";

interface ProfileFormProps {
  title: string;
  type?: string;
  id: string;
  placeholder: string;
}

const ProfileForm = ({
  title,
  type = "text",
  id,
  placeholder,
}: ProfileFormProps) => {
  return (
    <div className="">
      <form action="">
        <div className="relative w-full">
          <input
            type={type}
            id={id}
            placeholder={placeholder}
            className="bg-white autofill:bg-white peer w-full border border-gray-400 rounded-sm px-3 pt-4 pb-2 text-base focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
          <label className="absolute left-3 -top-3 text-gray-500 text-sm bg-white px-1 peer-placeholder-shown:-top-3 peer-placeholder-shown:text-sm peer-placeholder-shown:text-gray-500 transition-all duration-200">
            {title}
          </label>
        </div>
      </form>
    </div>
  );
};

const ProfileList = () => {
  return (
    <ul className="[&>li>a]:rounded-md [&>li>a]:bg-gray-200/60 [&>li>a]:p-5 [&>li>a]:mb-4 [&>li>a]:flex [&>li>a]:justify-between [&>li>a]:hover:px-7 [&>li>a]:transition-all [&>li>a]:duration-300">
      <li>
        <a href="#">
          ¿Cómo registrar un nuevo paciente?
          <ChevronRightIcon />
        </a>
      </li>
      <li>
        <a href="#">
          ¿Dónde veo mi agenda de turnos?
          <ChevronRightIcon />
        </a>
      </li>
      <li>
        <a href="#">
          ¿Dónde veo los historiales clínicos?
          <ChevronRightIcon />
        </a>
      </li>
      <li>
        <a href="#">
          ¿Cómo registro una observación?
          <ChevronRightIcon />
        </a>
      </li>
      <li>
        <a href="#">
          Se me cerró la sesión ¿Qué hago?
          <ChevronRightIcon />
        </a>
      </li>
    </ul>
  );
};

const InformationHelp = () => {
  return (
    <a href="#" className="flex bg-blue-200/50 p-4 rounded-xl mt-6">
      <div className="w-11/12 text-xs">
        <h2 className="font-medium">¿Esta Información fue de utilidad?</h2>
        <p>
          Si no es así, visita la documentación de este software para más
          información.
        </p>
      </div>
      <SquareArrowUpRightIcon />
    </a>
  );
};

function ProfilePage() {
  const [switchState, setSwitchState] = useState(false);

  return (
    <DashboardLayout>
      <section className="bg-white pt-8 pb-12 px-4 rounded-2xl">
        <header className="flex justify-end items-center gap-4 mb-6">
          <h1 className="text-2xl">Modo Admin</h1>
          <ToggleSwitch isOn={switchState} onChange={setSwitchState} />
        </header>
        <article className="border border-gray-400 grid grid-cols-8 rounded-lg">
          <article className="p-8 col-span-5">
            <header className="flex justify-between items-center mb-12">
              <h2 className="text-2xl">Información de la cuenta</h2>
              <div className="flex gap-4">
                <button className="text-red-600 py-2 px-5 border border-red-600 rounded-4xl hover:bg-gray-200 cursor-pointer">
                  Cerrar sesión
                </button>
                <button className="text-white py-2 px-5 bg-blue-500 rounded-4xl hover:bg-blue-700 cursor-pointer">
                  Editar
                </button>
              </div>
            </header>
            <div>
              <header className="flex justify-between items-center mb-12">
                <img
                  className="h-14 w-14 object-cover rounded-full"
                  src="https://plus.unsplash.com/premium_photo-1661580574627-9211124e5c3f?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8ZG9jdG9yYXxlbnwwfHwwfHx8MA%3D%3D"
                  alt=""
                />
                <h2 className="text-lg flex items-center gap-2">
                  <UsersIcon /> Rol: Profesional
                </h2>
              </header>
              <div className="grid grid-cols-2 gap-y-8 gap-x-20">
                <ProfileForm title="Nombre" id="nombre" placeholder="Analía" />
                <ProfileForm
                  title="Apellido"
                  id="apellido"
                  placeholder="López"
                />
                <ProfileForm
                  title="Email"
                  type="email"
                  id="email"
                  placeholder="analia.lopez@gmail.com"
                />
                <ProfileForm
                  title="Contraseña"
                  type="password"
                  id="password"
                  placeholder="**************"
                />
                <ProfileForm
                  title="Teléfono"
                  id="telefono"
                  placeholder="+549 011 7953 1654"
                />
                <ProfileForm
                  title="Especialidad"
                  id="especialidad"
                  placeholder="Fonoaudiología"
                />
              </div>
            </div>
          </article>
          <article className="border-l border-gray-400 p-8 col-span-3">
            <header className="flex justify-between items-center mb-8">
              <h2 className="text-2xl">Ayuda</h2>
              <QuestionIcon />
            </header>
            <ProfileList />
            <InformationHelp />
          </article>
        </article>
      </section>
    </DashboardLayout>
  );
}

export default ProfilePage;
