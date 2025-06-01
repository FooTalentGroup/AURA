import { useState, FC } from "react";
import { AppointmentTableProps } from "../types/patientTabs.types";
import { formatDate, getSpecialtyColors } from "../utils/utils";

const AppointmentTable: FC<AppointmentTableProps> = ({
  appointments,
  onSetAppointmentId,
}) => {
  const [filters, setFilters] = useState({
    date: "",
    specialty: "",
    professional: "",
  });
  const [selectedOption, setSelectedOption] = useState(0);

  const handleClick = (index: number, id: number) => {
    onSetAppointmentId(id);
    setSelectedOption(index);
  };

  return (
    <div className="bg-white mx-auto py-4 rounded-xl">
      <div className="flex flex-nowrap gap-2 mb-6 px-4">
        <div className="relative">
          <select
            className="block appearance-none w-40 bg-white border border-gray-300 hover:border-gray-400 px-4 py-2 pr-8 rounded shadow leading-tight focus:outline-none focus:ring focus:border-blue-300"
            value={filters.date}
            onChange={(e) => setFilters({ ...filters, date: e.target.value })}
          >
            <option value="">Fecha</option>
            {[...new Set(appointments?.map((app) => app.createdAt))].map(
              (date) => (
                <option key={date} value={date}>
                  {formatDate(date)}
                </option>
              )
            )}
          </select>
          <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
            <svg
              className="fill-current h-4 w-4"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 20 20"
            >
              <path d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" />
            </svg>
          </div>
        </div>

        <div className="relative">
          <select
            className="block appearance-none w-40 bg-white border border-gray-300 hover:border-gray-400 px-4 py-2 pr-8 rounded shadow leading-tight focus:outline-none focus:ring focus:border-blue-300"
            value={filters.specialty}
            onChange={(e) =>
              setFilters({ ...filters, specialty: e.target.value })
            }
          >
            <option value="">Especialidad</option>
            {[...new Set(appointments?.map((app) => app.specialty))].map(
              (specialty) => (
                <option key={specialty} value={specialty}>
                  {specialty}
                </option>
              )
            )}
          </select>
          <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
            <svg
              className="fill-current h-4 w-4"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 20 20"
            >
              <path d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" />
            </svg>
          </div>
        </div>

        <div className="relative">
          <select
            className="block appearance-none w-40 bg-white border border-gray-300 hover:border-gray-400 px-4 py-2 pr-8 rounded shadow leading-tight focus:outline-none focus:ring focus:border-blue-300"
            value={filters.professional}
            onChange={(e) =>
              setFilters({ ...filters, professional: e.target.value })
            }
          >
            <option value="">Profesional</option>
            {[...new Set(appointments?.map((app) => app.professionalName))].map(
              (professional) => (
                <option key={professional} value={professional}>
                  {professional}
                </option>
              )
            )}
          </select>
          <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
            <svg
              className="fill-current h-4 w-4"
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 20 20"
            >
              <path d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" />
            </svg>
          </div>
        </div>
      </div>

      <hr className="w-full mb-6 text-gray-400" />

      <div className="text-sm rounded-lg overflow-hidden px-4">
        {appointments
          ?.filter((app) => {
            return (
              (filters.date === "" || app.createdAt === filters.date) &&
              (filters.specialty === "" ||
                app.specialty === filters.specialty) &&
              (filters.professional === "" ||
                app.professionalName === filters.professional)
            );
          })
          .map((appointment, index) => (
            <div
              key={index}
              onClick={() => handleClick(index, appointment.id)}
              className={`flex items-center p-4 cursor-pointer border-2  hover:bg-gray-50 rounded-xl my-2 ${
                index === selectedOption
                  ? "border-2 border-[#0072c3] hover:bg-white"
                  : "border-transparent"
              }`}
            >
              <div className="flex-shrink-0 w-6 h-6 flex items-center justify-center">
                {index === selectedOption ? (
                  <div className="w-3 h-3 bg-blue-500 rounded-full"></div>
                ) : (
                  <div className="w-3 h-3 bg-gray-300 rounded-full"></div>
                )}
              </div>
              <div className="w-32 text-gray-700 font-medium">
                {formatDate(appointment.createdAt)}
              </div>
              <div
                className={`font-semibold w-44 px-4 py-2 rounded-sm ${getSpecialtyColors(
                  appointment.specialty
                )}`}
              >
                {appointment.specialty}
              </div>
              <div className="flex-grow px-4">
                {appointment.professionalName}
              </div>
            </div>
          ))}
      </div>
    </div>
  );
};

export default AppointmentTable;
