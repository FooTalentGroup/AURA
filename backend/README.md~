
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

```
---


### 🔄 Endpoint de paginación

**GET** `/professionals/page?page=0&size=10`

**Parámetros:**
- `page`: número de página (empieza en 0)
- `size`: cantidad de resultados por página

**Ejemplo:** `/professionals/page?page=1&size=5` devuelve la segunda página con 5 profesionales.

### 🔍 Buscar profesionales

**Endpoint**: `GET /professionals/search`

Permite buscar profesionales por apellido o especialidad. 
La búsqueda no distingue entre mayúsculas y minúsculas.

**Parámetros:**

| Parámetro | Tipo   | Descripción                              |
|-----------|--------|------------------------------------------|
| keyword   | String | Texto a buscar (apellido o especialidad) |

**Ejemplo de uso:**

GET /professionals/search?keyword=pediatria

**Respuesta exitosa:**
```json
[
  {
    "id": 3,
    "dni": "12345678",
    "name": "Ana",
    "lastName": "Gómez",
    "specialty": "Pediatria",
    ...
  }
]


## 📄 Licencia

Este proyecto está bajo la licencia [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0).  
Ver [LICENSE](LICENSE) para el texto completo de la licencia.

