const Loader = () => {
  return (
    <div className="flex justify-center items-center">
      <div
        className={`text-blue-500 w-12 h-12 normal border-3 animate-spin rounded-full`}
        style={{
          borderTopColor: "#f3f4f6",
          borderRightColor: "#f3f4f6",
          borderLeftColor: "#f3f4f6",
          borderBottomColor: `#3b82f6`,
        }}
      ></div>
    </div>
  );
};

export default Loader;
