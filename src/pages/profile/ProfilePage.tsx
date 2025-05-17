import DashboardLayout from "../../layouts/DashboardLayout";
import { CloseIcon, QuestionIcon } from "../../components/shared/ui/Icons";
import { useEffect, useState } from "react";
import { api } from "../../core/services/api";
import { EditableForm } from "../../components/shared/ui/EditableForm";
import ProfileList from "../../features/profile/components/ProfileList";
import InformationHelp from "../../features/profile/components/InformationHelp";
import {
  CurrentUserProps,
  UserUpdateData,
} from "../../features/profile/types/profile.type";

function ProfilePage() {
  const [userData, setUserData] = useState<CurrentUserProps | undefined>(
    undefined
  );

  const personalFields = [
    { key: "name", label: "Nombre" },
    { key: "lastName", label: "Apellido" },
    { key: "birthDate", label: "Fecha de nacimiento", type: "date" },
    { key: "dni", label: "DNI" },
  ];

  const contactFields = [
    { key: "email", label: "Correo electrónico", type: "email" },
    { key: "phoneNumber", label: "Teléfono" },
    { key: "address", label: "Dirección" },
  ];


  const handleSaveContactInfo = (updatedData: Partial<CurrentUserProps>) => {
    const updateData: UserUpdateData = {
      name: updatedData.name || userData?.name,
      email: updatedData.email || updatedData?.email,
      lastName: updatedData.lastName || userData?.lastName,
      birthDate: updatedData.birthDate || userData?.birthDate || "",
      dni: updatedData.dni || userData?.dni || "",

      // TODO: Adrián hay que descomentar estás opciones si backend las agrega
      // phoneNumber: updatedData.phoneNumber || userData?.phoneNumber || "",
      // address: updatedData.address || userData?.address || "",
    };

    if (!userData) return;

    api
      .updateCurrentUser(userData.id, updateData)
      .then((res) => {
        setUserData({ ...userData, ...updateData });
      })
      .catch((error) => {
        console.error("Error al actualizar datos de contacto:", error);
      });
  };

useEffect(() => {
  const fetchUser = async () => {
    try {
      const user = await api.getCurrentUser() as CurrentUserProps;
      setUserData(user);
    } catch (err) {
      console.error("Error al cargar el usuario:", err);
    }
  };
  fetchUser();
}, []);

const handleSavePersonalInfo = (updatedData: UserUpdateData) => {
  if (!userData) return;

  const updateData: UserUpdateData = {
    name: updatedData.name || userData.name,
    email: updatedData.email || userData.email,
    lastName: updatedData.lastName || userData.lastName,
    birthDate: updatedData.birthDate || userData.birthDate || "",
    dni: updatedData.dni || userData.dni || "",
  };

  api
    .updateCurrentUser(userData.id, updateData)
    .then(() => {
      setUserData({ ...userData, ...updateData });
    })
    .catch((error) => {
      if (error.response) {
        console.error("Error al actualizar datos:", error.response.data);
      } else {
        console.error("Error al actualizar datos:", error.message);
      }
    });
};

  return (
    <DashboardLayout>
      <section className="bg-white pt-5 pb-12 rounded-2xl">
        <header className="flex justify-between items-center px-6 gap-4 mb-5">
          <h1 className="text-3xl">Perfil</h1>
          <button>
            <CloseIcon />
          </button>
        </header>
        <hr className="text-gray-400" />
        <article className="grid grid-cols-8 rounded-lg px-6">
          <article className="p-8 col-span-5">
            <header className="flex flex-col items-center mb-12">
              <span className="mb-4 flex justify-center items-center text-3xl font-bold bg-sky-200 text-blue-400 w-20 h-20 rounded-full">
                {userData?.name.charAt(0).toUpperCase()}
                {userData?.lastName.charAt(0).toUpperCase()}
              </span>
              <h2 className="text-2xl">Bienvenido, {userData?.name}</h2>
            </header>
            {userData ? (
              <article className="flex flex-col gap-8">
                <EditableForm
                  title="Información básica"
                  fields={personalFields}
                  data={userData}
                  onSave={handleSavePersonalInfo}
                />

                <EditableForm
                  title="Información de contacto"
                  fields={contactFields}
                  data={userData}
                  onSave={handleSaveContactInfo}
                />
              </article>
            ) : (
              <p>Cargando datos del usuario...</p>
            )}
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
