# 🌀 AURA - Sistema de Gestión Clínica

AURA es una plataforma clínica integral que permite gestionar citas, pacientes, servicios y procesos de una clínica de terapias. Este repositorio monolítico contiene tanto el frontend como el backend del sistema.

---

## 🗂️ Estructura del Proyecto

```bash
aura/
├── aura-frontend/    # Interfaz web desarrollada con React + Vite
├── backend/     # API REST construida con Spring Boot
└── README.md    # Este archivo, con visión general del sistema
```
🚀 **Tecnologías Utilizadas**

| Componente         | Tecnología           | Descripción                                      |
|--------------------|----------------------|--------------------------------------------------|
| 🖥️ Frontend        | React + Vite         | SPA moderna y rápida con HMR                     |
| ⚙️ Backend         | Java 17 + Spring Boot| API REST robusta y segura                        |
| 🗃️ Base de Datos   | MySQL 8              | Almacenamiento relacional                        |
| 🔐 Autenticación   | JWT                  | Seguridad basada en tokens                       |
| 📚 Documentación   | Swagger              | Interfaz interactiva para explorar la API        |
| 🔄 Control de vers.| Git + GitHub        | Flujo de trabajo colaborativo con Gitflow        |
| 🏗️ CI/CD           | Render               | Despliegue continuo del backend                  |

---

# 🔧 Instalación Rápida (Modo Local)

## 📋 Requisitos
- Node.js 18+
- Java JDK 17+
- Maven 3.6+
- MySQL 8+

## 📥 Clonación del Repositorio
```bash
git clone https://github.com/AuraFTG/Aura_team-11_tarde.git
cd Aura_team-11_tarde/
```

## 📦 Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

## 💻 Frontend
```bash
cd aura-frontend
npm install    # Instalar dependencias
npm run dev    # Iniciar servidor de desarrollo
```

## 📚 Documentación Adicional

### ⚙️ Backend (`backend/README.md`)
- Estructura del proyecto y convenciones
- Endpoints API y documentación Swagger
- Configuración de despliegue en Render
- Variables de entorno requeridas
- Pipeline de CI/CD

### 🖥 Frontend (`aura-frontend/README.md`)
- Configuración inicial con Vite
- Hot Module Replacement (HMR)
- Configuración de ESLint/Prettier
- Optimizaciones para producción
- Variables de entorno

🔍 *Recomendamos leer ambos archivos antes de comenzar el desarrollo*

---
## 🌐 Demo en Producción

**Backend (Swagger UI)**:  
🔗 [https://clinica-shsg.onrender.com/swagger-ui/index.html](https://clinica-shsg.onrender.com/swagger-ui/index.html)

**Frontend**:

🔗 [https://aura-web.netlify.app](https://aura-web.netlify.app)

---

## 🧠 Créditos y Recursos
**Desarrollado por**:  
👥 Equipo de AuraFTG  

---
## 📄 Licencia

Este proyecto está bajo la licencia **[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)**.
