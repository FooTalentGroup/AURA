import { ClipBoardIcon } from "../../../components/shared/ui/Icons";

const PatientNotes = () => {
  return (
    <article>
      <header className="flex justify-between mb-4">
        <h2 className="flex gap-2 items-center text-xl font-semibold">
          <ClipBoardIcon /> Notas
        </h2>
        <button className="text-blue-600 py-2 px-5 border border-black rounded-4xl hover:bg-gray-200 cursor-pointer">
          Agregar Nota
        </button>
      </header>
      <p>
        Paciente de 13 años con déficit leve en lenguaje, evaluado en febrero.
        Seguimiento neurológico recomendado en marzo por posibles dificultades
        de aprendizaje. Audiometría normal en abril, sin alteraciones auditivas.
        Se sugiere terapia de lenguaje y control neurológico periódico para
        monitorear evolución. Familia comprometida con el proceso.
      </p>
    </article>
  );
};

export default PatientNotes;
