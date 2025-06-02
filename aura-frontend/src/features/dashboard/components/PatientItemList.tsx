const PatientItemList = () => {
  return (
    <div className="text-neutral-600">
      <ul className="flex justify-between items-center gap-8">
        <li className="flex gap-2">
          <span className="flex items-center font-semibold text-sm bg-gray-200/60 px-3 py-[1px] rounded-xl">
            12:00
          </span>

          <div className="flex items-center gap-2">
            <img
              className="w-8 h-8 rounded-full object-cover"
              src="https://images.unsplash.com/photo-1605783313291-1b996e9e7376?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
              alt=""
            />
            <ul className="leading-3 font-semibold">
              <li className="text-gray-700">Lucas Fernández</li>
              <li className="text-gray-500 text-sm">(13)</li>
            </ul>
          </div>
        </li>
        <li className="">44.617.293</li>
        <li className="">OSPLAD</li>
      </ul>
    </div>
  );
};

export default PatientItemList;
