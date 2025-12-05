import { useState, useEffect } from "react";
import UrgenciaModal from "../modal/UrgenciaModal";
import { ListaEsperaTable } from "../components/ListaEsperaTable";
import toast from "react-hot-toast";
import { Plus } from "lucide-react";
import {listarIngresosPendientes, registrarUrgencia, type RegistrarUrgenciaDTO, type IngresoUrgencia,type NivelEmergencia,} from "../api/urgencia";
import { extractErrorMessage } from "../api/http";
import { validarCuil, formatearCuil } from "../utils/cuil";
import { useCuilInput } from "../hooks/useCuilInput";
import { getUsuarioLogueado } from "../api/auth";

type FormState = {
  informe: string;
  nivelEmergencia: NivelEmergencia | "";

  // SIGNOS VITALES
  temperatura: string;
  frecuenciaCardiaca: string;
  frecuenciaRespiratoria: string;
  tensionSistolica: string;
  tensionDiastolica: string;
};

const initialForm: FormState = {
  informe: "",
  nivelEmergencia: "",

  temperatura: "",
  frecuenciaCardiaca: "",
  frecuenciaRespiratoria: "",
  tensionSistolica: "",
  tensionDiastolica: "",
};

const niveles: NivelEmergencia[] = [
  "Critica",
  "Emergencia",
  "Urgencia",
  "Urgencia Menor",
  "Sin Urgencia",
];

/* ----------------------------------------
   COMPONENTE PRINCIPAL
---------------------------------------- */

export default function Urgencia() {
  const [openModal, setOpenModal] = useState(false);
  const [form, setForm] = useState<FormState>(initialForm);
  const [ingresos, setIngresos] = useState<IngresoUrgencia[]>([]);
  const [loading, setLoading] = useState(false);

  // HOOKS DE CUIL
  const cuilPaciente = useCuilInput("");

  function onChange<K extends keyof FormState>(key: K, value: FormState[K]) {
    setForm((prev) => ({ ...prev, [key]: value }));
  }

  /* ----------------------------------------
     Cargar lista de espera al iniciar
  ---------------------------------------- */
  useEffect(() => {
    cargarIngresos();
  }, []);

  async function cargarIngresos() {
    try {
      const data = await listarIngresosPendientes();
      setIngresos(data);
    } catch (e) {
      toast.error("No se pudo cargar la lista de espera");
      console.error(e);
    }
  }

  /* ----------------------------------------
     Parse numérico opcional
  ---------------------------------------- */

  function parseOptionalNumber(value: string): number | undefined {
    if (!value.trim()) return undefined;
    const num = Number(value);
    return Number.isNaN(num) ? undefined : num;
  }

  /* ----------------------------------------
     SUBMIT DEL FORMULARIO
  ---------------------------------------- */

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);

    // Validar que el usuario esté logueado
    const usuario = getUsuarioLogueado();
    if (!usuario) {
      toast.error("Debes iniciar sesión para registrar urgencias");
      setLoading(false);
      return;
    }

    if (usuario.rol !== "ENFERMERA") {
      toast.error("Solo las enfermeras pueden registrar urgencias");
      setLoading(false);
      return;
    }

    // VALIDACIÓN CUIL PACIENTE
    if (!validarCuil(cuilPaciente.value)) {
      toast.error("El CUIL del paciente es inválido");
      setLoading(false);
      return;
    }

    const payload: RegistrarUrgenciaDTO = {
      cuilPaciente: formatearCuil(cuilPaciente.value),
      informe: form.informe.trim(),
      nivelEmergencia: form.nivelEmergencia as NivelEmergencia,

      temperatura: parseOptionalNumber(form.temperatura),
      frecuenciaCardiaca: parseOptionalNumber(form.frecuenciaCardiaca),
      frecuenciaRespiratoria: parseOptionalNumber(form.frecuenciaRespiratoria),
      tensionSistolica: parseOptionalNumber(form.tensionSistolica),
      tensionDiastolica: parseOptionalNumber(form.tensionDiastolica),
    };

    try {
      await registrarUrgencia(payload);

      toast.success("Urgencia registrada correctamente", {
        style: { borderRadius: "12px", background: "#333", color: "#fff" },
      });

      await cargarIngresos();
      setForm(initialForm);
      cuilPaciente.handleChange("");
      setOpenModal(false);
    } catch (err) {
      const msg = extractErrorMessage(err);
      toast.error(msg);
    } finally {
      setLoading(false);
    }
  }

  /* ----------------------------------------
     RENDER
  ---------------------------------------- */

  return (
    <div className="mx-auto max-w-6xl p-6">

      {/* HEADER */}
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-[#37393A]">Registro de Urgencias</h1>
        <button onClick={() => setOpenModal(true)} className="relative inline-flex items-center gap-2    justify-center p-0.5 overflow-hidden text-sm font-semibold rounded-xl bg-linear-to-br from-[#4ea5f5] to-[#4fcd89] group shadow-md transition-all duration-300 hover:shadow-xl hover:-translate-y-0.5 active:scale-95">
          <span className=" relative px-5 py-2.5 bg-white text-[#2e3440] rounded-[10px] transition-all duration-300 group-hover:bg-transparent group-hover:text-white flex items-center gap-2">
            <Plus size={16} />Registrar urgencia</span>
        </button>
      </div>

      <UrgenciaModal open={openModal} onClose={() => setOpenModal(false)}>
        <form
          onSubmit={onSubmit}
          className="space-y-8 p-1"
        >

          <div className="grid grid-cols-2 gap-6">

            {/* CUIL PACIENTE */}
            <div className="flex flex-col gap-1">
              <label className="text-sm font-semibold text-gray-700">CUIL paciente *</label>
              <input
                className={`
            rounded-2xl px-4 py-3 bg-white/70
            border shadow-sm
            focus:ring-2 focus:ring-blue-300
            outline-none transition-all backdrop-blur-sm
            hover:shadow-md
            ${cuilPaciente.valido === false ? "border-red-500" : "border-gray-300"}
            ${cuilPaciente.valido === true ? "border-green-600" : ""}
          `}
                value={cuilPaciente.value}
                onChange={(e) => cuilPaciente.handleChange(e.target.value)}
                placeholder="XX-XXXXXXXX-X"
              />
            </div>

            {/* INFORME */}
            <div className="flex flex-col gap-1">
              <label className="text-sm font-semibold text-gray-700">Informe *</label>
              <input
                className="
            rounded-2xl px-4 py-3 bg-white/70 
            border border-gray-300 shadow-sm 
            hover:shadow-md
            focus:ring-2 focus:ring-blue-300
            outline-none transition-all backdrop-blur-sm
          "
                value={form.informe}
                onChange={(e) => onChange('informe', e.target.value)}
              />
            </div>

            {/* NIVEL */}
            <div className="flex flex-col gap-1">
              <label className="text-sm font-semibold text-gray-700">Nivel  *</label>
              <select className="rounded-2xl px-4 py-3 bg-white/70 border border-gray-300 shadow-sm hover:shadow-md focus:ring-2 focus:ring-blue-300 outline-none transition-all backdrop-blur-sm"
                value={form.nivelEmergencia}
                onChange={(e) => onChange('nivelEmergencia', e.target.value as NivelEmergencia | '')}
              >
                <option value="">Seleccione…</option>
                {niveles.map((n) => (
                  <option key={n} value={n}>{n}</option>
                ))}
              </select>
            </div>

          </div>

          {/* SIGNOS VITALES */}
          <div className="rounded-3xl bg-white/50 backdrop-blur-lg">
            <p className="text-sm font-bold text-gray-800 mb-4">Signos vitales</p>
            <div className="grid grid-cols-2 gap-4">
              {[
                ["temperatura", "Temperatura (°C)"],
                ["frecuenciaCardiaca", "Frecuencia cardíaca (lpm)"],
                ["frecuenciaRespiratoria", "Frecuencia respiratoria (rpm)"],
                ["tensionSistolica", "Tensión sistólica (mmHg)"],
                ["tensionDiastolica", "Tensión diastólica (mmHg)"],
              ].map(([field, label]) => (
                <div key={field} className="flex flex-col gap-1">
                  <label className="text-sm font-medium text-gray-600">{label}</label>
                  <input className="rounded-xl px-4 py-2.5 bg-white/70 border border-gray-300 shadow-sm hover:shadow-md focus:ring-2 focus:ring-blue-300 outline-none transition-all backdrop-blur-sm"
                    value={(form as any)[field]}
                    onChange={(e) => onChange(field as any, e.target.value)}
                  />
                </div>
              ))}
            </div>
          </div>

          {/* BOTONES */}
          <div className="flex justify-end gap-4 pt-2">

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
      </UrgenciaModal>


      {/* TABLA DE ESPERA */}
      <h2 className="text-xl font-semibold mt-12 text-[#37393A]">Lista de espera</h2>

      {ingresos.length === 0 ? (
        <p className="mt-4 text-center text-gray-500">No hay ingresos pendientes</p>
      ) : (
        <ListaEsperaTable ingresos={ingresos} />
      )}

    </div>
  );
}
