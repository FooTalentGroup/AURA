import { ArrowRightIcon } from "../../../components/shared/ui/Icons";
import { TabButtonProps } from "../types/patientTabs.types";

const TabButton = ({ label, isActive, onClick }: TabButtonProps) => {
  return (
    <button
      className={`cursor-pointer rounded-lg flex justify-between items-center w-full px-5 py-4 text-left transition-colors duration-150 ${
        isActive
          ? "bg-blue-100 text-sky-800 hover:bg-blue-100"
          : "text-gray-800 hover:bg-gray-100"
      }`}
      onClick={onClick}
    >
      <span>{label}</span>
      <span>
        <ArrowRightIcon />
      </span>
    </button>
  );
};

export default TabButton;
