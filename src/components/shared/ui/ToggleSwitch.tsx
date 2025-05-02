import { useState } from "react";

interface ToggleSwitchProps {
  isOn?: boolean;
  onChange?: (isOn: boolean) => void;
  disabled?: boolean;
  onColor?: string;
  offColor?: string;
  size?: "sm" | "md" | "lg";
}

export default function ToggleSwitch({
  isOn = false,
  onChange = () => {},
  disabled = false,
  onColor = "bg-blue-500",
  offColor = "bg-gray-400",
  size = "sm",
}: ToggleSwitchProps) {
  const [isToggled, setIsToggled] = useState(isOn);

  const handleToggle = () => {
    if (disabled) return;

    const newValue = !isToggled;
    setIsToggled(newValue);
    onChange(newValue);
  };

  const sizeClasses = {
    sm: {
      switch: "w-10 h-5",
      knob: "w-3 h-3",
      knobOn: "translate-x-5",
    },
    md: {
      switch: "w-14 h-7",
      knob: "w-5 h-5",
      knobOn: "translate-x-7",
    },
    lg: {
      switch: "w-16 h-8",
      knob: "w-6 h-6",
      knobOn: "translate-x-8",
    },
  };

  const currentSize = sizeClasses[size];

  return (
    <div
      onClick={handleToggle}
      className={`
        relative rounded-full transition-colors duration-300 cursor-pointer
        ${isToggled ? onColor : offColor}
        ${currentSize.switch}
        ${disabled ? "opacity-50 cursor-not-allowed" : ""}
      `}
    >
      <div
        className={`
          absolute top-1 left-1 bg-white rounded-full transform transition-transform duration-300
          ${currentSize.knob}
          ${isToggled ? currentSize.knobOn : ""}
        `}
      />
    </div>
  );
}
