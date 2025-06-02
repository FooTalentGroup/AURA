const MedicalHistoryTable = () => {
  return (
    <div className="bg-white rounded-lg overflow-hidden">
      <table className="min-w-full divide-y divide-gray-200">
        <thead className="bg-gray-50 ">
          <tr>
            <th className="px-2 py-3 text-left text-xs font-medium uppercase tracking-wider">
              Fecha
            </th>
            <th className="px-2 py-3 text-left text-xs font-medium uppercase tracking-wider">
              Título
            </th>
            <th className="px-2 py-3 text-left text-xs font-medium uppercase tracking-wider">
              Observación
            </th>
            <th className="px-2 py-3 text-left text-xs font-medium uppercase tracking-wider">
              Especialidad
            </th>
            <th className="px-2 py-3 text-left text-xs font-medium uppercase tracking-wider">
              Profesional
            </th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          <tr>
            <td className="px-2 py-4 whitespace-nowrap">05 Abr 2025</td>
            <td className="px-2 py-4 whitespace-nowrap">
              Audiometría infantil
            </td>
            <td className="px-2 py-4 whitespace-nowrap">Normal</td>
            <td className="px-2 py-4 whitespace-nowrap">Fonoaudiología</td>
            <td className="px-2 py-4 whitespace-nowrap">Dr. José Hernández</td>
          </tr>
          <tr>
            <td className="px-2 py-4 whitespace-nowrap">20 Mar 2025</td>
            <td className="px-2 py-4 whitespace-nowrap">
              Evaluación neurológica
            </td>
            <td className="px-2 py-4 whitespace-nowrap">
              Seguimiento recomendado
            </td>
            <td className="px-2 py-4 whitespace-nowrap">Psicología</td>
            <td className="px-2 py-4 whitespace-nowrap">Dr. Carina Ledesma</td>
          </tr>
          <tr>
            <td className="px-2 py-4 whitespace-nowrap">12 Feb 2025</td>
            <td className="px-2 py-4 whitespace-nowrap">Test de lenguaje</td>
            <td className="px-2 py-4 whitespace-nowrap">Déficit leve</td>
            <td className="px-2 py-4 whitespace-nowrap">Psicopedagogía</td>
            <td className="px-2 py-4 whitespace-nowrap">Dr. Laura Torres</td>
          </tr>
        </tbody>
      </table>
    </div>
  );
};

export default MedicalHistoryTable;
