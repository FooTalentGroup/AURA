export type CurrentUserProps = {
  id: number;
  email: string;
  name: string;
  lastName: string;
  birthDate: string | null;
  dni: string | null;
  roles: string[];
  phoneNumber?: string;
  address?: string;
};

export type UserUpdateData = Partial<Omit<CurrentUserProps, "id" | "roles">>;

export type EditableField = {
  key: string;
  label: string;
  type?: string;
};
