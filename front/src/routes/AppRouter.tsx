import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import Sidebar from "../components/Sidebar";
import Paciente from "../pages/Paciente";
import Urgencia from "../pages/Urgencia";
import Enfermera from "../pages/Enfermera";
import Medico from "../pages/Medico";
import Login from "../pages/Login";

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Sidebar />}>
          <Route path="urgencia" element={<Urgencia />} />
          <Route path="paciente" element={<Paciente />} />
          <Route path="enfermera" element={<Enfermera />} />
          <Route path="medico" element={<Medico />} />
          <Route index element={<Navigate to="urgencia" replace />} />
        </Route>

        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
