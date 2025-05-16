import { SquareArrowUpRightIcon } from "../../../components/shared/ui/Icons";

const InformationHelp = () => {
  return (
    <a
      href="#"
      className="flex items-center justify-between bg-blue-200/50 p-4 rounded-xl mt-6"
    >
      <div className="w-9/12 text-xs">
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

export default InformationHelp;
