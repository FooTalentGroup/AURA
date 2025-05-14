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

export const getSpecialtyBackgroundColor = (specialty: string) => {
  const lowerCaseSpecialty = specialty.toLowerCase();
  if (lowerCaseSpecialty === "medicina") {
    return "bg-orange-100";
  } else if (lowerCaseSpecialty === "psicóloga") {
    return "bg-purple-100";
  }
  return "bg-gray-100";
};
