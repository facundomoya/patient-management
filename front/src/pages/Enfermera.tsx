import { useState, useEffect } from "react";
import { createEnfermera, listEnfermeras } from "../api/enfermeras";
import PacienteModal from "../modal/PacienteModal";
import { EnfermerasTable } from "../components/EnfermerasTable";
import { Plus } from "lucide-react";
import type { AltaEnfermeraDTO, Enfermera as EnfermeraType } from "../api/enfermeras";
import { useCuilInput } from "../hooks/useCuilInput";
import { validarCuil, formatearCuil } from "../utils/cuil";
import toast from "react-hot-toast";

type FormState = {
  apellido: string;
  nombre: string;
};

const initialForm: FormState = {
  apellido: "",
  nombre: "",
};

export default function Enfermera() {
  const [openModal, setOpenModal] = useState(false);
  const [form, setForm] = useState<FormState>(initialForm);
  const [enfermeras, setEnfermeras] = useState<EnfermeraType[]>([]);
  const [loading, setLoading] = useState(false);

  // 🧠 Hook de CUIL con formateo automático
  const cuilEnfermera = useCuilInput("");

  function onChange<K extends keyof FormState>(key: K, value: FormState[K]) {
    setForm((p) => ({ ...p, [key]: value }));
  }

  useEffect(() => {
    cargarEnfermeras();
  }, []);

  async function cargarEnfermeras() {
    try {
      const data = await listEnfermeras();
      setEnfermeras(data);
    } catch { }
  }

  /* ----------------------------------------
       SUBMIT
  ---------------------------------------- */
  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    if (!validarCuil(cuilEnfermera.value)) {
      toast.error("El CUIL ingresado es inválido");
      setLoading(false);
      return;
    }
    
    const payload: AltaEnfermeraDTO = {
      cuil: formatearCuil(cuilEnfermera.value),
      apellido: form.apellido,
      nombre: form.nombre,
    };
    
    try {
      await createEnfermera(payload);
      await cargarEnfermeras();
      setForm(initialForm);
      cuilEnfermera.handleChange(""); // reset
      setOpenModal(false);
      toast.success("Enfermera creada exitosamente");
    } catch (error: any) {
      toast.error(error?.response?.data || "Error al crear enfermera");
    } finally {
      setLoading(false);
    }
  }
  
  return (
    <div className="mx-auto max-w-6xl p-6">
      {/* HEADER */}
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-[#37393A]">Enfermeras</h1>
        <button onClick={() => setOpenModal(true)} className="relative inline-flex items-center gap-2    justify-center p-0.5 overflow-hidden text-sm font-semibold rounded-xl bg-linear-to-br from-[#4ea5f5] to-[#4fcd89] group shadow-md transition-all duration-300 hover:shadow-xl hover:-translate-y-0.5 active:scale-95">
          <span className=" relative px-5 py-2.5 bg-white text-[#2e3440] rounded-[10px] transition-all duration-300 group-hover:bg-transparent group-hover:text-white flex items-center gap-2">
            <Plus size={16} />Crear Enfermera</span>
        </button>
      </div>
      {/* MODAL */}
      <PacienteModal open={openModal} onClose={() => setOpenModal(false)}>
        <h2 className="text-xl font-semibold mb-4 text-[#37393A]">Nueva Enfermera</h2>
        <form onSubmit={onSubmit} className="space-y-6">
          {/* DATOS PRINCIPALES */}
          <div className="grid grid-cols-2 gap-4">
            {/* CUIL */}
            <div className="flex flex-col">
              <label className="text-sm font-medium text-gray-700">CUIL *</label>
              <input
                className={`
                  rounded-xl border px-4 py-2.5 mt-1 shadow-sm bg-white
                  focus:ring-2 focus:ring-[#77B6EA]/40 focus:border-[#77B6EA]
                  outline-none transition-all
                  ${cuilEnfermera.valido === false ? "border-red-500" : ""}
                  ${cuilEnfermera.valido === true ? "border-green-600" : ""}
                `} value={cuilEnfermera.value} onChange={(e) => cuilEnfermera.handleChange(e.target.value)} placeholder="20-XXXXXXXX-X" />
            </div>
            {/* Apellido */}
            <div className="flex flex-col">
              <label className="text-sm font-medium text-gray-700">Apellido *</label>
              <input className="rounded-xl border px-4 py-2.5 mt-1 shadow-sm bg-white focus:ring-2 focus:ring-[#77B6EA]/40 focus:border-[#77B6EA]" value={form.apellido} onChange={(e) => onChange("apellido", e.target.value)} />
            </div>
            {/* Nombre */}
            <div className="flex flex-col">
              <label className="text-sm font-medium text-gray-700">Nombre *</label>
              <input
                className="rounded-xl border px-4 py-2.5 mt-1 shadow-sm bg-white 
                focus:ring-2 focus:ring-[#77B6EA]/40 focus:border-[#77B6EA]"
                value={form.nombre}
                onChange={(e) => onChange("nombre", e.target.value)}
              />
            </div>
          </div>

          {/* BOTONES */}
          <div className="flex justify-end gap-4">

            {/* CANCELAR */}
            <button type="button" onClick={() => setOpenModal(false)} className="relative inline-flex items-center justify-center p-0.5 overflow-hidden text-sm font-medium rounded-xl group bg-linear-to-br from-pink-500 to-orange-400 group-hover:from-pink-500 group-hover:to-orange-400 hover:text-white focus:ring-4 focus:outline-none focus:ring-pink-200">
              <span className=" relative px-4 py-2.5 bg-white text-gray-700 rounded-xl transition-all ease-in duration-150 group-hover:bg-transparent group-hover:text-white">Cancelar</span>
            </button>
            {/* GUARDAR */}
            <button type="submit" disabled={loading} className="relative inline-flex items-center gap-2    justify-center p-0.5 overflow-hidden text-sm font-semibold rounded-xl bg-linear-to-br from-[#4ea5f5] to-[#4fcd89] group shadow-md transition-all duration-300 hover:shadow-xl hover:-translate-y-0.5 active:scale-95">
              <span className=" relative px-5 py-2.5 bg-white text-[#2e3440] rounded-[10px] transition-all duration-300 group-hover:bg-transparent group-hover:text-white flex items-center gap-2">
                {loading ? "Guardando..." : "Guardar"}
              </span>
            </button>

          </div>

        </form>
      </PacienteModal>

      {/* TABLA */}
      <h2 className="text-xl font-semibold mt-12 text-[#37393A]">
        Lista de Enfermeras
      </h2>

      <EnfermerasTable enfermeras={enfermeras} />
    </div>
  );
}

