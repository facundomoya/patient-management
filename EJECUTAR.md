# Guía para Ejecutar el Proyecto

## 🚀 Backend (Spring Boot - Java)

### Opción 1: Desde IntelliJ IDEA
1. Abre el archivo `backend/src/main/java/org/example/ClinicaApplication.java`
2. Haz clic derecho → `Run 'ClinicaApplication.main()'`
3. O presiona `Shift + F10` con el cursor en el archivo

### Opción 2: Desde la Terminal
```bash
cd backend
mvn spring-boot:run
```

**Verificación:**
- El servidor debería iniciar en `http://localhost:8080`
- Deberías ver en la consola: `Started ClinicaApplication`
- Prueba en el navegador: `http://localhost:8080/api/obras-sociales`
- Deberías ver un JSON con las obras sociales

---

## 🎨 Frontend (React + TypeScript + Vite)

### Desde la Terminal
```bash
cd front
npm install  # Solo la primera vez o si agregaste dependencias
npm run dev
```

**Verificación:**
- El servidor debería iniciar en `http://localhost:5173` (o el puerto que Vite asigne)
- Abre tu navegador en esa URL
- Deberías ver la página de pacientes

---

## ✅ Verificar que Todo Funciona

### 1. Verificar Backend
Abre en tu navegador o usa curl:
- **Obras Sociales:** `http://localhost:8080/api/obras-sociales`
- **Pacientes:** `http://localhost:8080/api/pacientes`

Deberías ver JSON con datos mock.

### 2. Verificar Frontend
1. Abre `http://localhost:5173` (o el puerto que Vite muestre)
2. Deberías ver:
   - El formulario de alta de pacientes
   - La lista de pacientes mock (4 pacientes)
   - El select de obras sociales con 5 opciones

### 3. Probar Crear un Paciente
1. Completa el formulario:
   - CUIL: `20-99999999-9`
   - Apellido: `Test`
   - Nombre: `Usuario`
   - Calle: `Av. Test`
   - Número: `123`
   - Localidad: `San Miguel de Tucumán`
   - (Obra social opcional)
2. Haz clic en "Crear paciente"
3. Deberías ver un mensaje de éxito
4. El nuevo paciente debería aparecer en la lista

### 4. Verificar en la Consola del Navegador
- Abre las DevTools (F12)
- Ve a la pestaña "Network" (Red)
- Intenta crear un paciente
- Deberías ver la petición POST a `http://localhost:8080/api/pacientes`

---

## 🐛 Solución de Problemas

### Backend no inicia
- Verifica que el puerto 8080 no esté en uso
- Revisa la consola de IntelliJ para ver errores
- Asegúrate de que Maven haya descargado todas las dependencias

### Frontend no se conecta al backend
- Verifica que el backend esté corriendo en `http://localhost:8080`
- Revisa la consola del navegador (F12) para ver errores CORS
- Verifica que la URL en `front/src/api/http.ts` sea correcta

### Error CORS
- El backend ya tiene CORS configurado
- Si persiste, verifica que `CorsConfig.java` esté siendo cargado

### No se ven los pacientes mock
- Verifica que el backend esté corriendo
- Abre `http://localhost:8080/api/pacientes` directamente
- Revisa la consola del navegador para errores


