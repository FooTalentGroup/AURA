import { FC } from "react";
import { ClipBoardIcon } from "../../../components/shared/ui/Icons";
import { MedicalHistoryProps } from "../types/dashboard.types";
import { Link } from "react-router-dom";

const MedicalHistory: FC<MedicalHistoryProps> = ({ children }) => {
  return (
    <article>
      <header className="flex justify-between items-center mb-6">
        <h2 className="flex items-center gap-2 text-xl font-semibold">
          <ClipBoardIcon /> Historial clínico
        </h2>
        <Link
          to="/patient"
          className="text-blue-600 py-2 px-5 border border-black rounded-4xl hover:bg-gray-200 cursor-pointer"
        >
          Ver historial
        </Link>
      </header>
      {children}
    </article>
  );
};

export default MedicalHistory;
