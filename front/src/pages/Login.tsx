import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login, setUsuarioLogueado } from "../api/auth";
import toast from "react-hot-toast";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);

    try {
      const usuario = await login({ email, password });
      setUsuarioLogueado(usuario);
      toast.success(`Bienvenido ${usuario.nombre} ${usuario.apellido}`);
      
      // Redirigir según el rol
      if (usuario.rol === "ENFERMERA") {
        navigate("/urgencia");
      } else {
        navigate("/paciente");
      }
    } catch (error: any) {
      toast.error(error?.response?.data || "Error al iniciar sesión");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#E8EEF2] to-[#C7D3DD] flex items-center justify-center p-6">
      <div className="bg-white rounded-2xl shadow-xl p-8 w-full max-w-md">
        <h1 className="text-3xl font-bold text-[#37393A] mb-2 text-center">
          Sistema de Clínica
        </h1>
        <p className="text-gray-600 text-center mb-8">Inicia sesión para continuar</p>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Email
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full rounded-xl border border-gray-300 px-4 py-3 shadow-sm focus:ring-2 focus:ring-[#77B6EA]/40 focus:border-[#77B6EA] outline-none transition-all"
              placeholder="usuario@clinica.com"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Contraseña
            </label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full rounded-xl border border-gray-300 px-4 py-3 shadow-sm focus:ring-2 focus:ring-[#77B6EA]/40 focus:border-[#77B6EA] outline-none transition-all"
              placeholder="••••••••"
              required
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full relative inline-flex items-center gap-2 justify-center p-0.5 overflow-hidden text-sm font-semibold rounded-xl bg-linear-to-br from-[#4ea5f5] to-[#4fcd89] group shadow-md transition-all duration-300 hover:shadow-xl hover:-translate-y-0.5 active:scale-95 disabled:opacity-50"
          >
            <span className="relative w-full px-5 py-2.5 bg-white text-[#2e3440] rounded-[10px] transition-all duration-300 group-hover:bg-transparent group-hover:text-white flex items-center justify-center gap-2">
              {loading ? "Iniciando sesión..." : "Iniciar sesión"}
            </span>
          </button>
        </form>

        <div className="mt-6 p-4 bg-blue-50 rounded-xl">
          <p className="text-sm font-semibold text-blue-900 mb-2">Usuarios de prueba:</p>
          <ul className="text-xs text-blue-800 space-y-1">
            <li>• Enfermera: enfermera20433365772@clinica.com / password123</li>
            <li>• Médico: medico20433365772@clinica.com / password123</li>
          </ul>
        </div>
      </div>
    </div>
  );
}

