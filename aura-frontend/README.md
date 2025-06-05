# 🌟 AURA Frontend

Frontend SPA para el sistema **AURA** (Administración Unificada de Registros y Atenciones), construido con **React**, **TypeScript**, **Vite** y **Tailwind CSS**.

---

## 🚀 Instalación Rápida

1. **Pre-requisitos:**  
   - Node.js >= 18  
   - npm >= 9 o yarn >= 1.22

2. **Clona el repositorio:**
   ```bash
   git clone https://github.com/AuraFTG/aura-frontend.git
   cd aura-frontend
   ```

3. **Instala dependencias:**
   ```bash
   npm install
   # o
   yarn install
   ```

4. **Configura el entorno:**  
   Crea un archivo `.env` en la raíz:
   ```env
   VITE_API_BASE_URL=https://api.tu-dominio.com
   ```

5. **Inicia el servidor de desarrollo:**
   ```bash
   npm run dev
   # o
   yarn dev
   ```

---

## 📁 Estructura Esencial

```
src/
  components/   # Componentes reutilizables
  pages/        # Vistas principales
  services/     # Lógica de API
  hooks/        # Custom hooks
  styles/       # CSS/Tailwind
  main.tsx      # Entrada principal
  App.tsx       # Componente raíz
```

---

## 🛠️ Scripts Útiles

- `npm run dev` / `yarn dev` — Modo desarrollo (hot reload)
- `npm run build` / `yarn build` — Build de producción
- `npm run preview` / `yarn preview` — Previsualizar build
- `npm run lint` / `yarn lint` — Linting y corrección
- `npm run type-check` / `yarn type-check` — Chequeo de tipos

---

## 🧩 Stack Tecnológico

- **React** + **TypeScript**
- **Vite**
- **Tailwind CSS**
- **ESLint**

---

## 🤝 Contribuir

1. Crea una rama:  
   `git checkout -b feature/nueva-funcionalidad`
2. Haz tus cambios y commitea.
3. Abre un Pull Request.

---

¿Dudas? Consulta la [Wiki del proyecto](https://deepwiki.com/AuraFTG/aura-frontend).
