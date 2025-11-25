import { NavLink, Outlet } from "react-router-dom";
import { Activity, User, LogOut } from "lucide-react";

export default function Sidebar() {
  return (
    <div className="bg-[#E8EEF2] min-h-screen">

      {/* SIDEBAR FIJO */}
      <aside className="fixed left-0 top-0 h-screen w-60 bg-[#37393A] text-white shadow-xl p-5 flex flex-col">
        <h2 className="font-bold text-xl tracking-wide">Panel Admin</h2>

        <nav className="mt-8 flex flex-col gap-2 text-sm">
          <NavLink
            to="/urgencia"
            className={({ isActive }) =>
              `flex items-center gap-3 rounded-lg px-3 py-2 transition
              ${isActive 
                ? "bg-[#77B6EA]/20 border-l-4 border-[#77B6EA] text-white font-semibold" 
                : "text-gray-300 hover:bg-white/10"}`
            }
          >
            <Activity size={18} />
            Urgencias
          </NavLink>

          <NavLink
            to="/paciente"
            className={({ isActive }) =>
              `flex items-center gap-3 rounded-lg px-3 py-2 transition
              ${isActive 
                ? "bg-[#77B6EA]/20 border-l-4 border-[#77B6EA] text-white font-semibold" 
                : "text-gray-300 hover:bg-white/10"}`
            }
          >
            <User size={18} />
            Pacientes
          </NavLink>
        </nav>

        {/* BOTÓN LOGOUT Arriba del borde inferior */}
        <button className="mt-auto flex items-center gap-2 text-red-400 hover:text-red-300 transition">
          <LogOut size={18} />
          Cerrar sesión
        </button>
      </aside>

      {/* CONTENIDO PRINCIPAL DESPLAZABLE */}
      <main className="ml-60 min-h-screen p-6 overflow-y-auto">
        <Outlet />
      </main>
    </div>
  );
}
