export const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  const day = date.getDate().toString().padStart(2, "0");
  const monthNames = [
    "Ene",
    "Feb",
    "Mar",
    "Abr",
    "May",
    "Jun",
    "Jul",
    "Ago",
    "Sep",
    "Oct",
    "Nov",
    "Dic",
  ];
  const month = monthNames[date.getMonth()];
  const year = date.getFullYear();
  return `${day} ${month} ${year}`;
};

export const getSpecialtyColors = (specialty: string) => {
  const lowerCaseSpecialty = specialty.toLowerCase();

  switch (lowerCaseSpecialty) {
    case "fonoaudiología":
      return "bg-[#fff2e8] text-[#8a3800]";
    case "psicología":
      return "bg-[#edf5ff] text-[#0043ce]";
    case "psicopedagogía":
      return "bg-[#f6f2ff] text-[#6929c4]";
    case "terapia ocupacional":
      return "bg-[#d9fbfb] text-[#005d5d]";
    default:
      return "bg-gray-100";
  }
};
