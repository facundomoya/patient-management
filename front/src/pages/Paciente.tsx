import { useState, useEffect } from "react";
import { createPaciente, listObrasSociales, listPacientes } from "../api/pacientes";
import PacienteModal from "../modal/PacienteModal";
import { PacientesTable } from "../components/PacientesTable";
import { Plus } from "lucide-react";
import type { AltaPacienteDTO, Domicilio, ObraSocial, Paciente as PacienteType } from "../api/pacientes";
import { useCuilInput } from "../hooks/useCuilInput";
import { validarCuil, formatearCuil } from "../utils/cuil";
import toast from "react-hot-toast";

type FormState = {
  apellido: string;
  nombre: string;
  calle: string;
  numero: string;
  localidad: string;
  obraSocialCodigo: string;
  numeroAfiliado: string;
};

const initialForm: FormState = {
  apellido: "",
  nombre: "",
  calle: "",
  numero: "",
  localidad: "San Miguel de Tucumán",
  obraSocialCodigo: "",
  numeroAfiliado: "",
};

export default function Paciente() {
  const [openModal, setOpenModal] = useState(false);
  const [form, setForm] = useState<FormState>(initialForm);
  const [obras, setObras] = useState<ObraSocial[]>([]);
  const [pacientes, setPacientes] = useState<PacienteType[]>([]);
  const [loading, setLoading] = useState(false);

  // 🧠 Hook de CUIL con formateo automático
  const cuilPaciente = useCuilInput("");

  function onChange<K extends keyof FormState>(key: K, value: FormState[K]) {
    setForm((p) => ({ ...p, [key]: value }));
  }

  useEffect(() => {
    (async () => {
      try {
        const os = await listObrasSociales();
        setObras(os);
      } catch {
        setObras([]);
      }
    })();

    cargarPacientes();
  }, []);

  async function cargarPacientes() {
    try {
      const data = await listPacientes();
      setPacientes(data);
    } catch { }
  }

  /* ----------------------------------------
       SUBMIT
  ---------------------------------------- */
  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    if (!validarCuil(cuilPaciente.value)) {
      toast.error("El CUIL ingresado es inválido");
      setLoading(false);
      return;
    }
    const domicilio: Domicilio = {
      calle: form.calle,
      numero: Number(form.numero),
      localidad: form.localidad,
    };

    const payload: AltaPacienteDTO = {
      cuil: formatearCuil(cuilPaciente.value),
      apellido: form.apellido,
      nombre: form.nombre,
      domicilio,
    };
    if (form.obraSocialCodigo) {
      payload.obraSocialCodigo = form.obraSocialCodigo;
      payload.numeroAfiliado = form.numeroAfiliado;
    }
    try {
      await createPaciente(payload);
      await cargarPacientes();
      setForm(initialForm);
      cuilPaciente.handleChange(""); // reset
      setOpenModal(false);
    } finally {
      setLoading(false);
    }
  }
  return (
    <div className="mx-auto max-w-6xl p-6">
      {/* HEADER */}
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-[#37393A]">Pacientes</h1>
        <button onClick={() => setOpenModal(true)} className="relative inline-flex items-center gap-2    justify-center p-0.5 overflow-hidden text-sm font-semibold rounded-xl bg-linear-to-br from-[#4ea5f5] to-[#4fcd89] group shadow-md transition-all duration-300 hover:shadow-xl hover:-translate-y-0.5 active:scale-95">
          <span className=" relative px-5 py-2.5 bg-white text-[#2e3440] rounded-[10px] transition-all duration-300 group-hover:bg-transparent group-hover:text-white flex items-center gap-2">
            <Plus size={16} />Crear Paciente</span>
        </button>
      </div>
      {/* MODAL */}
      <PacienteModal open={openModal} onClose={() => setOpenModal(false)}>
        <h2 className="text-xl font-semibold mb-4 text-[#37393A]">Nuevo Paciente</h2>
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
                  ${cuilPaciente.valido === false ? "border-red-500" : ""}
                  ${cuilPaciente.valido === true ? "border-green-600" : ""}
                `} value={cuilPaciente.value} onChange={(e) => cuilPaciente.handleChange(e.target.value)} placeholder="20-XXXXXXXX-X" />
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
          {/* DOMICILIO */}
          <div>
            <p className="font-medium text-sm text-slate-700 mb-1">Domicilio</p>
            <div className="grid grid-cols-3 gap-4">
              <input
                className="rounded-xl border px-4 py-2.5 shadow-sm bg-white"
                placeholder="Calle"
                value={form.calle}
                onChange={(e) => onChange("calle", e.target.value)}
              />
              <input
                className="rounded-xl border px-4 py-2.5 shadow-sm bg-white"
                placeholder="Número"
                value={form.numero}
                onChange={(e) => onChange("numero", e.target.value)}
              />
              <input
                className="rounded-xl border px-4 py-2.5 shadow-sm bg-white"
                placeholder="Localidad"
                value={form.localidad}
                onChange={(e) => onChange("localidad", e.target.value)}
              />
            </div>
          </div>
          {/* OBRA SOCIAL */}
          <div className="grid grid-cols-2 gap-4">
            <div className="flex flex-col">
              <label className="text-sm font-medium text-gray-700">Obra Social</label>
              <select
                className="rounded-xl border px-4 py-2.5 mt-1 shadow-sm bg-white 
                focus:ring-2 focus:ring-[#77B6EA]/40 focus:border-[#77B6EA]"
                value={form.obraSocialCodigo}
                onChange={(e) => onChange("obraSocialCodigo", e.target.value)}
              >
                <option value="">— Sin obra social —</option>
                {obras.map((o) => (
                  <option key={o.codigo} value={o.codigo}>
                    {o.nombre}
                  </option>
                ))}
              </select>
            </div>
            <div className="flex flex-col">
              <label className="text-sm font-medium text-gray-700">
                Número Afiliado {form.obraSocialCodigo ? "*" : ""}
              </label>
              <input
                disabled={!form.obraSocialCodigo}
                className="rounded-xl border px-4 py-2.5 mt-1 shadow-sm bg-white disabled:bg-gray-100"
                value={form.numeroAfiliado}
                onChange={(e) => onChange("numeroAfiliado", e.target.value)}
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
        Lista de Pacientes
      </h2>

      <PacientesTable pacientes={pacientes} />
    </div>
  );
}
