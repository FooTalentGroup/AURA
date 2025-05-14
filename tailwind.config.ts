import { Config } from "tailwindcss";

const config: Config = {
  content: ["./index.html", "./src/**/*.{ts,tsx,js,jsx}"],
  theme: {
    extend: {
      colors: {
        aura: {
          green: "#198038",
          blue: "#1B84FF",
          gray: {
            100: "#F5F7FA",
            300: "#D9DBE1",
            400: "#B0B7C3",
            600: "#6B7280",
          },
        },
      },
      borderRadius: {
        card: "12px",
        sm: "4px",
      },
      spacing: {
        "modal-x": "24px",
        "modal-y": "32px",
        "step-gap": "80px",
      },
      fontSize: {
        base: ["16px", "24px"],
        sm: ["12px", "16px"],
      },
      animation: {
        "spin-slow": "spin 1.5s linear infinite",
        spin: "spin 1s linear infinite",
        "spin-fast": "spin 0.5s linear infinite",
      },
    },
  },
  plugins: [],
};

export default config;
