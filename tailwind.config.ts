import { Config } from 'tailwindcss';

const config: Config = {
  content: [
    './index.html',
    './src/**/*.{ts,tsx,js,jsx}',
  ],
  theme: {
    extend: {
      colors: {
        aura: {
          green: '#198038',
          blue:  '#1B84FF',
          gray:  {
            100: '#F5F7FA',
            300: '#D9DBE1',
            400: '#B0B7C3',
            600: '#6B7280',
          },
        },
      },
      borderRadius: {
        card: '12px',
        sm:   '4px',
      },
      spacing: {
        'modal-x': '24px',
        'modal-y': '32px',
        'step-gap': '80px',
      },
      fontSize: {
        'base': ['16px', '24px'],
        'sm':   ['12px', '16px'],
      },
    },
  },
  plugins: [],
};

export default config;