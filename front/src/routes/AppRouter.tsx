import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import Sidebar from "../components/Sidebar";
import Paciente from "../pages/Paciente";
import Urgencia from "../pages/Urgencia";
import Enfermera from "../pages/Enfermera";
import Reclamo from "../pages/Reclamo";
import Atenciones from "../pages/Atenciones";
import Login from "../pages/Login";
import { getUsuarioLogueado } from "../api/auth";
import Medico from "../pages/Medico";
import type { JSX } from "react";

function RequireAuth({ children }: { children: JSX.Element }) {
  const usuario = getUsuarioLogueado();
  if (!usuario) return <Navigate to="/login" replace />;
  return children;
}

function RequireEnfermera({ children }: { children: JSX.Element }) {
  const usuario = getUsuarioLogueado();
  if (!usuario) return <Navigate to="/login" replace />;
  if (usuario.rol !== "ENFERMERA") {
    return <Navigate to="/reclamo" replace />;
  }
  return children;
}

function RequireMedico({ children }: { children: JSX.Element }) {
  const usuario = getUsuarioLogueado();
  if (!usuario) return <Navigate to="/login" replace />;
  if (usuario.rol !== "MEDICO") {
    return <Navigate to="/urgencia" replace />;
  }
  return children;
}

function RedirectIfAuth({ children }: { children: JSX.Element }) {
  const usuario = getUsuarioLogueado();
  if (usuario) {
    // Redirigir según el rol
    if (usuario.rol === "ENFERMERA") {
      return <Navigate to="/urgencia" replace />;
    } else {
      return <Navigate to="/reclamo" replace />;
    }
  }
  return children;
}

function IndexRedirect() {
  const usuario = getUsuarioLogueado();
  if (usuario) {
    if (usuario.rol === "ENFERMERA") {
      return <Navigate to="/urgencia" replace />;
    } else {
      return <Navigate to="/reclamo" replace />;
    }
  }
  return <Navigate to="/login" replace />;
}

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<RedirectIfAuth><Login /></RedirectIfAuth>} />
        <Route path="/" element={<RequireAuth><Sidebar /></RequireAuth>}>
          {/* Rutas solo para ENFERMERA */}
          <Route path="urgencia" element={<RequireEnfermera><Urgencia /></RequireEnfermera>} />
          <Route path="paciente" element={<RequireEnfermera><Paciente /></RequireEnfermera>} />
          <Route path="enfermera" element={<RequireEnfermera><Enfermera /></RequireEnfermera>} />
          
          {/* Rutas solo para MÉDICO */}
          <Route path="reclamo" element={<RequireMedico><Reclamo /></RequireMedico>} />
          <Route path="atenciones" element={<RequireMedico><Atenciones /></RequireMedico>} />
          <Route path="medico" element={<RequireMedico><Medico /></RequireMedico>} />
          
          <Route index element={<IndexRedirect />} />
        </Route>

        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
