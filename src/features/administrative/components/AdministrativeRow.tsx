import React from "react";
import { Admin } from "../../auth/types/auth.types";

interface Props {
  admin: Admin;
  onView?: (id: number) => void;
}

export const AdministrativeRow: React.FC<Props> = ({ admin,  onView }) => {
  return (
    <div className="grid grid-cols-[2fr_1fr_1fr_2fr_1fr] px-4 py-2 items-center border-b hover:bg-gray-50">
      <span onClick={() => onView?.(admin.id)} className="cursor-pointer hover:underline">
        {admin.name}
      </span>
      <span>{admin.dni}</span>
      <span>{admin.phone}</span>
      <span>{admin.email}</span>
      <div className="flex justify-end">

      </div>
    </div>
  );
};