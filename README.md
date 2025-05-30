
# 🩺 Backend - Sistema de Gestión Clínica AURA
Este proyecto representa el backend del sistema de gestión de citas, pacientes y servicios de una clínica de terapias.

---

## 🚀 Tecnologías 

| Componente        | Tecnología    | Justificación                          |
|-------------------|--------------|----------------------------------------|
| Lenguaje          | Java 17      | Familiaridad del equipo, robustez      |
| Framework         | Spring Boot  | Desarrollo rápido de APIs REST         |
| Base de Datos     | MySQL 8      | Relacional, conocimiento del equipo    |
| Autenticación     | JWT          | Seguridad para APIs                    |
| Control de Versiones | Git/GitHub | Colaboración eficiente                |
| Documentar API    | Swagger      | permite documentar y probar de forma interactiva la API|

---

## 📁 Estructura del Proyecto

```bash
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── config/         # Configuraciones
│   │   │   ├── controllers/    # Controladores REST
│   │   │   ├── models/         # Entidades
│   │   │   ├── repositories/   # Acceso a datos
│   │   │   ├── services/       # Lógica de negocio
│   │   │   └── exceptions/     # Manejo de errores
│   │   └── resources/
│   │       ├── application.properties # Configuración
├── docs/
└── pom.xml             # Dependencias Maven
```

---

## 🔷 Gitflow y Commits



🌿 Estrategia de Ramas (Gitflow)

| Rama          | Propósito                                 |
|---------------|-------------------------------------------|
| `main`        | Código en producción (estable)            |
| `develop`     | Integración de features                   |
| `feature/*`   | Desarrollo de nuevas funcionalidades      |
| `hotfix/*`    | Correcciones urgentes en producción       |
| `release/*`   | Preparación para nuevas versiones         |


## 💡 Convención de Commits


| Emoji | Tipo       | Descripción                           |
|-------|------------|---------------------------------------|
| ✨    | `feat`     | Nueva funcionalidad                   |
| 🐛    | `fix`      | Corrección de errores                 |
| 📚    | `docs`     | Cambios en documentación              |
| ♻️    | `refactor` | Reestructuración de código            |
| 🧪    | `test`     | Pruebas unitarias/integración         |
| 🎨    | `style`    | Formato de código                     |
| 🔧    | `chore`    | Tareas de mantenimiento               |


---

## 🌐 Enlace de Producción

- Backend: [https://clinica-shsg.onrender.com/swagger-ui.html](https://clinica-shsg.onrender.com/swagger-ui.html/m)


---

## ⚙️ Variables de Entorno en Producción

- `SPRING_DATASOURCE_URL`: URL de conexión a la base de datos en producción.
- `SPRING_DATASOURCE_USERNAME`: Usuario de la base de datos.
- `SPRING_DATASOURCE_PASSWORD`: Contraseña de la base de datos.
- `JWT_SECRET`: Clave secreta para generar tokens JWT.

---

## 🔧 Detalles Técnicos de Despliegue

- **Plataforma**: Render
- **Puerto abierto**: 8080
- **Comando de build**: `mvn clean package`
- **Comando de ejecución**: `java -jar target/backend-0.0.1-SNAPSHOT.jar`
- **HTTPS**: Configuración automática proporcionada por Render
- **CI/CD**: El despliegue se realiza automáticamente mediante integración continua (CI/CD) conectando el repositorio de GitHub a Render.
- **DockerHub**: La imagen del backend se construye y publica en DockerHub como parte del proceso de CI/CD.

---

## 📡 Verificación de Endpoints

- Endpoint de prueba: `GET https://clinica-shsg.onrender.com/swagger-ui/index.html`
- Estado: ✅ Funciona correctamente.

---
## 🔷 Requisitos e Instalación


🛠️ Requisitos

- Java JDK 17+
- MySQL 8+
- Maven 3.6+

---

## 🔧 Instalación

1. **Clonar repositorio**:
```bash
git clone https://github.com/AuraFTG/backend.git
```
2. 🔧 **Configuración de la Base de Datos**

Configura el archivo `src/main/resources/application.properties` con:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/aura_db
spring.datasource.username=usuario
spring.datasource.password=contraseña
```

3. **Ejecutar aplicación**:
```bash
mvn spring-boot:run
```
---

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/AuraFTG/backend)


## 📄 Licencia

Este proyecto está bajo la licencia [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0).  
Ver [LICENSE](LICENSE) para el texto completo de la licencia.

