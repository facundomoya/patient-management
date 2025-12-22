# Sistema de Gestión de Pacientes para Clínica

## Descripción del Proyecto
- **Objetivo:** Aplicación web para gestionar el flujo de atención en una clínica: registro de urgencias, lista de espera, reclamo de pacientes por médicos, registro de atenciones, y altas de pacientes, médicos y enfermeras.
- **Ámbito académico:** Trabajo Final Integrador (TFI) de Ingeniería de Software – UTN FRT.
- **Arquitectura:** Backend en Spring Boot (Java 21) + Frontend en React (Vite + TypeScript + Tailwind).

## Características Principales
- **Urgencias:**
  - Registro de ingresos con `nivel de emergencia` y signos vitales.
  - Lista de espera ordenada por prioridad (crítica → emergencia → urgencia → urgencia menor → sin urgencia) y orden de llegada para empates.
  - Endpoints para ver ingresos pendientes y todos los ingresos.
- **Reclamo de pacientes (médicos):**
  - Reclamo del siguiente paciente priorizado.
  - Consulta del paciente actualmente en atención.
- **Registro de atención:**
  - El médico registra el informe de atención sobre un ingreso en proceso.
- **Altas y catálogos:**
  - Alta y listado de `Pacientes`, `Médicos`, `Enfermeras`.
  - Listado de `Obras Sociales`.
- **Autenticación simplificada:**
  - El front envía `X-User-Email` en cada solicitud (inyectado por interceptor).
  - El backend valida perfil (`enfermera` para urgencias, `medico` para reclamos/atenciones).
- **CORS activo:**
  - Configurado para desarrollo abierto desde el frontend.
- **Pruebas BDD:**
  - Cucumber describe escenarios del módulo de urgencias (validaciones y prioridades).

## Tecnologías Utilizadas
- **Backend:** Spring Boot 3.2, Java 21, Maven, Cucumber 7 (JUnit Platform), BCrypt.
- **Frontend:** React 19, Vite 7, TypeScript 5.9, Tailwind CSS v4, Axios, React Router, Lucide Icons, React Hot Toast.

## Estructura del Proyecto
- **Backend:**
  - App principal: [backend/src/main/java/org/example/ClinicaApplication.java](backend/src/main/java/org/example/ClinicaApplication.java)
  - Configuración: [backend/src/main/resources/application.properties](backend/src/main/resources/application.properties), [backend/src/main/java/org/example/web/config/CorsConfig.java](backend/src/main/java/org/example/web/config/CorsConfig.java)
  - Controladores:
    - Pacientes: [backend/src/main/java/org/example/web/controller/PacienteController.java](backend/src/main/java/org/example/web/controller/PacienteController.java)
    - Médicos: [backend/src/main/java/org/example/web/controller/MedicoController.java](backend/src/main/java/org/example/web/controller/MedicoController.java)
    - Enfermeras: [backend/src/main/java/org/example/web/controller/EnfermeraController.java](backend/src/main/java/org/example/web/controller/EnfermeraController.java)
    - Obras sociales: [backend/src/main/java/org/example/web/controller/ObraSocialController.java](backend/src/main/java/org/example/web/controller/ObraSocialController.java)
    - Urgencias: [backend/src/main/java/org/example/web/controller/UrgenciasController.java](backend/src/main/java/org/example/web/controller/UrgenciasController.java)
    - Reclamo: [backend/src/main/java/org/example/web/controller/ReclamoController.java](backend/src/main/java/org/example/web/controller/ReclamoController.java)
    - Atenciones: [backend/src/main/java/org/example/web/controller/AtencionController.java](backend/src/main/java/org/example/web/controller/AtencionController.java), [backend/src/main/java/org/example/web/controller/AtencionesController.java](backend/src/main/java/org/example/web/controller/AtencionesController.java)
  - Pruebas BDD: [backend/src/test/resources/features/moduloUrgencias.feature](backend/src/test/resources/features/moduloUrgencias.feature)
  - Dependencias: [backend/pom.xml](backend/pom.xml)
- **Frontend:**
  - Configuración Vite: [front/vite.config.ts](front/vite.config.ts)
  - Cliente HTTP y APIs: [front/src/api/http.ts](front/src/api/http.ts), [front/src/api/pacientes.ts](front/src/api/pacientes.ts), [front/src/api/medicos.ts](front/src/api/medicos.ts), [front/src/api/enfermeras.ts](front/src/api/enfermeras.ts), [front/src/api/urgencia.ts](front/src/api/urgencia.ts), [front/src/api/reclamo.ts](front/src/api/reclamo.ts), [front/src/api/atenciones.ts](front/src/api/atenciones.ts)
  - Páginas: [front/src/pages](front/src/pages)
  - Componentes principales: [front/src/components](front/src/components), [front/src/modal](front/src/modal)
  - Estilos y utilidades: [front/src/index.css](front/src/index.css), [front/src/utils](front/src/utils)
- **Guía rápida de ejecución:** [EJECUTAR.md](EJECUTAR.md)

## Descripción de Archivos y Carpetas

### Backend
- **backend/pom.xml**: Configuración de dependencias Maven y compilación del proyecto Spring Boot.
- **backend/src/main/java/org/example/ClinicaApplication.java**: Clase principal que inicia la aplicación Spring Boot.
- **backend/src/main/java/org/example/web/controller/**: Controladores REST de los módulos de pacientes, médicos, enfermeras, urgencias, reclamos y atenciones.
- **backend/src/main/java/org/example/web/config/**: Configuración de CORS, inyección de dependencias y de la aplicación.
- **backend/src/main/java/org/example/app/**: Servicios de aplicación (ServicioUrgencias, ServicioReclamo, ServicioRegistroAtencion, etc.).
- **backend/src/main/java/org/example/domain/**: Entidades del dominio (Paciente, Médico, Enfermera, Ingreso, Atencion, etc.).
- **backend/src/main/java/org/example/auth/**: Módulo de autenticación con BCrypt y repositorio de usuarios.
- **backend/src/main/java/org/example/infrastructure/**: Implementaciones de repositorios en memoria y configuración de infraestructura.
- **backend/src/main/resources/application.properties**: Propiedades de configuración (puerto, aplicación, etc.).
- **backend/src/test/java/**: Pruebas unitarias y de integración con JUnit 5 y Mockito.
- **backend/src/test/java/mock/**: Implementaciones en memoria de repositorios para testing.
- **backend/src/test/java/org/example/steps/**: Steps de Cucumber para pruebas BDD.
- **backend/src/test/resources/features/**: Archivos de features en Gherkin para especificaciones ejecutables (moduloUrgencias.feature).

### Frontend
- **front/vite.config.ts**: Configuración de Vite para desarrollo y build.
- **front/package.json**: Dependencias npm y scripts de desarrollo.
- **front/tsconfig.json**: Configuración de TypeScript.
- **front/tailwind.config.js**: Configuración de Tailwind CSS.
- **front/src/main.tsx**: Punto de entrada de la aplicación React.
- **front/src/App.tsx**: Componente raíz de la aplicación.
- **front/src/api/http.ts**: Cliente HTTP Axios configurado con interceptores para autenticación.
- **front/src/api/pacientes.ts, medicos.ts, enfermeras.ts, urgencia.ts, reclamo.ts, atenciones.ts**: Llamadas a APIs del backend.
- **front/src/pages/**: Páginas principales (Paciente, Medico, Enfermera, Urgencia, Reclamo, Atenciones, Login).
- **front/src/components/**: Componentes reutilizables (Tablas, Sidebar, Cronómetro de espera).
- **front/src/modal/**: Modales de alta (PacienteModal, UrgenciaModal).
- **front/src/utils/**: Utilidades para validación (CUIL) y formato de fechas.
- **front/src/hooks/**: Hooks personalizados para lógica reutilizable.
- **front/src/routes/AppRouter.tsx**: Configuración de rutas de React Router.
- **front/src/index.css, App.css**: Estilos globales de la aplicación.
- **front/public/**: Archivos estáticos (favicon, etc.).

### Raíz
- **README.md**: Este archivo con documentación general del proyecto.

## API del Backend
- **Base:** `http://localhost:8080/api` (configurable en frontend vía `VITE_API_URL`).
- **Rutas principales:**
  - `GET /obras-sociales` → Lista obras sociales.
  - `GET /pacientes` · `POST /pacientes` → Listado y alta.
  - `GET /medicos` · `POST /medicos` → Listado y alta.
  - `GET /enfermeras` · `POST /enfermeras` → Listado y alta.
  - `POST /urgencias` → Registrar ingreso (requiere `X-User-Email` de una `enfermera`).
  - `GET /urgencias/lista-espera` → Lista de espera ordenada.
  - `GET /urgencias/ingresos` → Todos los ingresos registrados.
  - `POST /reclamo` → Médico reclama siguiente paciente (requiere `X-User-Email`).
  - `GET /reclamo/actual` → Paciente actualmente en atención del médico.
  - `POST /atencion` → Registrar atención sobre ingreso en proceso (requiere `X-User-Email`).

## Modelos y DTOs (resumen funcional)
- **Urgencia:** `cuilPaciente`, `informe`, `nivelEmergencia`, signos vitales (temperatura, frecuencia cardiaca/respiratoria, tensión sistólica/diastólica).
- **IngresoDTO:** datos del paciente + nivel de emergencia + fecha de ingreso + (enfermeroCuil).
- **AtencionDTO:** paciente + médico + informe + fecha + nivel de emergencia.
- **AltaPacienteRequest:** datos personales, domicilio y obra social (opcional).

## Fundamento Funcional (Urgencias)
- **Prioridad:** Pacientes se ordenan por `nivel de emergencia` (mayor a menor) y, en caso de empate, por `orden de llegada`.
- **Validaciones:** Signos vitales mandatorios y rangos válidos (ej.: no negativos), documentados en [moduloUrgencias.feature](backend/src/test/resources/features/moduloUrgencias.feature).
- **Flujo:** Enfermera admite → paciente entra en lista → médico reclama → médico atiende → atención registrada.

## Cómo Ejecutar el proyecto
- **Backend (Spring Boot):**
  - Desde terminal en Windows:
    ```powershell
    cd backend
    mvn spring-boot:run
    ```
  - Verificar: abrir `http://localhost:8080/api/obras-sociales`.
- **Frontend (React + Vite):**
  - Desde terminal en Windows:
    ```powershell
    cd front
    npm install
    npm run dev
    ```
  - Verificar: abrir `http://localhost:5173`.
- **Configuración de API en el front:**
  - Por defecto usa `http://localhost:8080/api`.
  - Para apuntar a otra URL, defina `VITE_API_URL` (ej.: `.env`):
    ```env
    VITE_API_URL=http://localhost:8080/api
    ```

## Pruebas

### Pruebas Unitarias
- **Framework:** JUnit 5 (Jupiter) integrado en Spring Boot.
- **Mocking:** Mockito para simulación de dependencias.
- **Repositorios Mock:** Implementaciones en memoria (`DBPacienteEnMemoria`, `DBMedicoEnMemoria`, `DBEnfermeraEnMemoria`, etc.) para tests sin base de datos.
- **Casos de prueba:**
  - Validación de creación de entidades (pacientes, médicos, enfermeras).
  - Priorización correcta en lista de espera (por nivel de emergencia y orden de llegada).
  - Validación de signos vitales (rangos, obligatoriedad).
  - Cambios de estado de ingresos (pendiente → en proceso → finalizado).
- **Ubicación:** [backend/src/test/java/org/example](backend/src/test/java/org/example)
- **Ejecutar tests unitarios:**
  ```powershell
  cd backend
  mvn test
  ```

### Pruebas BDD (Behavior-Driven Development)
- **Framework:** Cucumber 7 con JUnit Platform Engine.
- **Especificaciones ejecutables:** [backend/src/test/resources/features/moduloUrgencias.feature](backend/src/test/resources/features/moduloUrgencias.feature)
- **Steps (implementación):** [backend/src/test/java/org/example/steps](backend/src/test/java/org/example/steps)
- **Escenarios cubiertos:**
  - Ingreso de paciente existente con datos válidos.
  - Validación de datos mandatorios (frecuencia cardiaca, respiratoria, etc.).
  - Validación de rangos (frecuencias y tensiones no negativas).
  - Priorización por nivel de emergencia (crítica > emergencia > urgencia > urgencia menor > sin urgencia).
  - Orden de llegada para pacientes con igual nivel de emergencia.
- **Ejecutar tests BDD:**
  ```powershell
  cd backend
  mvn test -Dgroups=urgencias
  ```

### Cobertura de Testing
- **Mock Data:** Cada test utiliza datos pre-cargados en repositorios en memoria para aislar la lógica de la persistencia.
- **CI/CD:** Los tests se ejecutan automáticamente con `mvn test` durante el build.
- **Reportes:** Maven genera reportes en `backend/target/surefire-reports/`.

## Estado del Proyecto

Completado ✔️

## Cómo Clonar el Repositorio
En una terminal, ejecute:
  ```bash
  git clone https://github.com/facundomoya/tfi-ingDeSoftware.git
  ```
  Luego:
  ```powershell
  cd tfi-ingDeSoftware
  ```

## Equipo de desarrollo

Este proyecto fue desarrollado por estudiantes de **Ingeniería en Sistemas de Información** de la **Universidad Tecnológica Nacional - Facultad Regional Tucumán (UTN-FRT)** para la materia **Ingeniería de Software**. Los desarrolladores del proyecto son:

- Facundo Moya - [Github](https://github.com/facundomoya)
- Juan Martín Bossi - [Github](https://github.com/JuanBossi)
- Santiago Joaquín Martín Peñaloza - [Github](https://github.com/santijj99)
- Ignacio David Sanchez Konicek - [Github](https://github.com/Ignacio159)
