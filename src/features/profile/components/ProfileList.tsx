import { ArrowRightIcon } from "../../../components/shared/ui/Icons";

const ProfileList = () => {
  return (
    <ul className="[&>li>a]:rounded-md [&>li>a]:bg-gray-200/60 [&>li>a]:p-5 [&>li>a]:mb-4 [&>li>a]:flex [&>li>a]:justify-between [&>li>a]:hover:px-7 [&>li>a]:transition-all [&>li>a]:duration-300">
      <li>
        <a href="#">
          ¿Cómo registrar un nuevo paciente?
          <ArrowRightIcon />
        </a>
      </li>
      <li>
        <a href="#">
          ¿Dónde veo mi agenda de turnos?
          <ArrowRightIcon />
        </a>
      </li>
      <li>
        <a href="#">
          ¿Dónde veo los historiales clínicos?
          <ArrowRightIcon />
        </a>
      </li>
      <li>
        <a href="#">
          ¿Cómo registro una observación?
          <ArrowRightIcon />
        </a>
      </li>
      <li>
        <a href="#">
          Se me cerró la sesión ¿Qué hago?
          <ArrowRightIcon />
        </a>
      </li>
    </ul>
  );
};

export default ProfileList;
