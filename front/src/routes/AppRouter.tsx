import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import Sidebar from "../components/Sidebar";
import Paciente from "../pages/Paciente";
import Urgencia from "../pages/Urgencia";

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Sidebar />}>
          <Route path="urgencia" element={<Urgencia />} />
          <Route path="paciente" element={<Paciente />} />
          <Route index element={<Navigate to="urgencia" replace />} />
        </Route>

        <Route path="*" element={<Navigate to="/urgencia" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
