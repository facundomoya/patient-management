import { useEffect, useMemo, useState } from "react";
import { listarIngresosPendientes, registrarUrgencia } from "../api/urgencia";
import type { IngresoUrgencia, NivelEmergencia, RegistrarUrgenciaDTO } from "../api/urgencia";
import { extractErrorMessage } from "../api/http";

type FormState = {
  cuilPaciente: string;
  informe: string;
  nivelEmergencia: NivelEmergencia | "";
  enfermeraNombre: string;
  enfermeraApellido: string;
  enfermeraCuil: string;
  temperatura: string;
  frecuenciaCardiaca: string;
  frecuenciaRespiratoria: string;
  tensionSistolica: string;
  tensionDiastolica: string;
};

const initialForm: FormState = {
  cuilPaciente: "",
  informe: "",
  nivelEmergencia: "",
  enfermeraNombre: "",
  enfermeraApellido: "",
  enfermeraCuil: "",
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

export default function Urgencia() {
  const [form, setForm] = useState<FormState>(initialForm);
  const [okMsg, setOkMsg] = useState<string | null>(null);
  const [errMsg, setErrMsg] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const [ingresos, setIngresos] = useState<IngresoUrgencia[]>([]);
  const [loadingIngresos, setLoadingIngresos] = useState(false);

  const hayDatosEnFormulario = useMemo(
    () => JSON.stringify(form) !== JSON.stringify(initialForm),
    [form]
  );

  useEffect(() => {
    cargarIngresos();
  }, []);

  function onChange<K extends keyof FormState>(key: K, value: FormState[K]) {
    setForm(prev => ({ ...prev, [key]: value }));
  }

  async function cargarIngresos() {
    setLoadingIngresos(true);
    try {
      const lista = await listarIngresosPendientes();
      setIngresos(lista);
    } catch (err) {
      setErrMsg(extractErrorMessage(err));
    } finally {
      setLoadingIngresos(false);
    }
  }

  function validar(): string | null {
    if (!form.cuilPaciente.trim()) return "El CUIL del paciente es obligatorio";
    if (!form.informe.trim()) return "El informe clínico es obligatorio";
    if (!form.nivelEmergencia) return "Seleccione un nivel de emergencia";
    if (!form.enfermeraNombre.trim()) return "El nombre de la enfermera es obligatorio";
    if (!form.enfermeraApellido.trim()) return "El apellido de la enfermera es obligatorio";
    if (!form.enfermeraCuil.trim()) return "El CUIL de la enfermera es obligatorio";
    return null;
  }

  function parseOptionalNumber(value: string, label: string): number | null {
    if (!value.trim()) return null;
    const num = Number(value);
    if (Number.isNaN(num)) {
      throw new Error(`${label} debe ser numérica`);
    }
    return num;
  }

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    setErrMsg(null);
    setOkMsg(null);

    const validation = validar();
    if (validation) {
      setErrMsg(validation);
      return;
    }

    let temperatura: number | null;
    let fc: number | null;
    let fr: number | null;
    let ts: number | null;
    let td: number | null;

    try {
      temperatura = parseOptionalNumber(form.temperatura, "Temperatura");
      fc = parseOptionalNumber(form.frecuenciaCardiaca, "Frecuencia cardíaca");
      fr = parseOptionalNumber(form.frecuenciaRespiratoria, "Frecuencia respiratoria");
      ts = parseOptionalNumber(form.tensionSistolica, "Tensión sistólica");
      td = parseOptionalNumber(form.tensionDiastolica, "Tensión diastólica");
    } catch (err) {
      setErrMsg((err as Error).message);
      return;
    }

    const payload: RegistrarUrgenciaDTO = {
      cuilPaciente: form.cuilPaciente.trim(),
      informe: form.informe.trim(),
      nivelEmergencia: form.nivelEmergencia as NivelEmergencia,
      enfermeraNombre: form.enfermeraNombre.trim(),
      enfermeraApellido: form.enfermeraApellido.trim(),
      enfermeraCuil: form.enfermeraCuil.trim(),
      temperatura: temperatura ?? undefined,
      frecuenciaCardiaca: fc ?? undefined,
      frecuenciaRespiratoria: fr ?? undefined,
      tensionSistolica: ts ?? undefined,
      tensionDiastolica: td ?? undefined,
    };

    setLoading(true);
    try {
      await registrarUrgencia(payload);
      setOkMsg("Urgencia registrada correctamente");
      setForm(initialForm);
      await cargarIngresos();
    } catch (err) {
      setErrMsg(extractErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="mx-auto max-w-5xl p-6">
      <div className="flex items-center justify-between flex-wrap gap-4">
        <h1 className="text-2xl font-semibold tracking-tight">Registro de Urgencias</h1>
        <button
          onClick={cargarIngresos}
          disabled={loadingIngresos}
          className="rounded-xl border px-4 py-2 text-sm hover:bg-slate-50 disabled:opacity-60"
        >
          {loadingIngresos ? "Actualizando..." : "Actualizar lista"}
        </button>
      </div>

      <form onSubmit={onSubmit} className="mt-6 space-y-4">
        {okMsg && (
          <div className="rounded-lg border border-green-600 bg-green-50 px-4 py-3 text-green-800">
            {okMsg}
          </div>
        )}
        {errMsg && (
          <div className="rounded-lg border border-red-600 bg-red-50 px-4 py-3 text-red-800">
            {errMsg}
          </div>
        )}

        <div className="grid gap-4 md:grid-cols-3">
          <div>
            <label className="block text-sm text-slate-600">CUIL paciente *</label>
            <input
              className="mt-1 w-full rounded-xl border px-3 py-2 outline-none focus:ring"
              placeholder="20-12345678-9"
              value={form.cuilPaciente}
              onChange={(e) => onChange("cuilPaciente", e.target.value)}
            />
          </div>
          <div className="md:col-span-2">
            <label className="block text-sm text-slate-600">Informe *</label>
            <textarea
              className="mt-1 w-full rounded-xl border px-3 py-2 outline-none focus:ring"
              rows={1}
              value={form.informe}
              onChange={(e) => onChange("informe", e.target.value)}
            />
          </div>
        </div>

        <div className="grid gap-4 md:grid-cols-3">
          <div>
            <label className="block text-sm text-slate-600">Nivel de emergencia *</label>
            <select
              className="mt-1 w-full rounded-xl border px-3 py-2 outline-none focus:ring bg-white"
              value={form.nivelEmergencia}
              onChange={(e) => onChange("nivelEmergencia", e.target.value as NivelEmergencia | "")}
            >
              <option value="">Seleccione...</option>
              {niveles.map(n => (
                <option key={n} value={n}>{n}</option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm text-slate-600">Enfermera nombre *</label>
            <input
              className="mt-1 w-full rounded-xl border px-3 py-2 outline-none focus:ring"
              value={form.enfermeraNombre}
              onChange={(e) => onChange("enfermeraNombre", e.target.value)}
            />
          </div>
          <div>
            <label className="block text-sm text-slate-600">Enfermera apellido *</label>
            <input
              className="mt-1 w-full rounded-xl border px-3 py-2 outline-none focus:ring"
              value={form.enfermeraApellido}
              onChange={(e) => onChange("enfermeraApellido", e.target.value)}
            />
          </div>
        </div>

        <div className="grid gap-4 md:grid-cols-3">
          <div>
            <label className="block text-sm text-slate-600">Enfermera CUIL *</label>
            <input
              className="mt-1 w-full rounded-xl border px-3 py-2 outline-none focus:ring"
              placeholder="27-00000000-0"
              value={form.enfermeraCuil}
              onChange={(e) => onChange("enfermeraCuil", e.target.value)}
            />
          </div>
        </div>

        <div className="rounded-2xl border p-4">
          <p className="mb-2 text-sm font-medium text-slate-700">Signos vitales (opcional)</p>
          <div className="grid gap-4 md:grid-cols-3">
            <div>
              <label className="block text-sm text-slate-600">Temperatura (°C)</label>
              <input
                className="mt-1 w-full rounded-xl border px-3 py-2 outline-none focus:ring"
                inputMode="decimal"
                value={form.temperatura}
                onChange={(e) => onChange("temperatura", e.target.value)}
              />
            </div>
            <div>
              <label className="block text-sm text-slate-600">Frecuencia cardíaca (lpm)</label>
              <input
                className="mt-1 w-full rounded-xl border px-3 py-2 outline-none focus:ring"
                inputMode="decimal"
                value={form.frecuenciaCardiaca}
                onChange={(e) => onChange("frecuenciaCardiaca", e.target.value)}
              />
            </div>
            <div>
              <label className="block text-sm text-slate-600">Frecuencia respiratoria (rpm)</label>
              <input
                className="mt-1 w-full rounded-xl border px-3 py-2 outline-none focus:ring"
                inputMode="decimal"
                value={form.frecuenciaRespiratoria}
                onChange={(e) => onChange("frecuenciaRespiratoria", e.target.value)}
              />
            </div>
            <div>
              <label className="block text-sm text-slate-600">Tensión sistólica (mmHg)</label>
              <input
                className="mt-1 w-full rounded-xl border px-3 py-2 outline-none focus:ring"
                inputMode="decimal"
                value={form.tensionSistolica}
                onChange={(e) => onChange("tensionSistolica", e.target.value)}
              />
            </div>
            <div>
              <label className="block text-sm text-slate-600">Tensión diastólica (mmHg)</label>
              <input
                className="mt-1 w-full rounded-xl border px-3 py-2 outline-none focus:ring"
                inputMode="decimal"
                value={form.tensionDiastolica}
                onChange={(e) => onChange("tensionDiastolica", e.target.value)}
              />
            </div>
          </div>
        </div>

        <div className="flex items-center gap-3">
          <button
            type="submit"
            disabled={loading}
            className="rounded-2xl bg-slate-900 px-5 py-2.5 text-white shadow hover:opacity-90 disabled:opacity-60"
          >
            {loading ? "Registrando..." : "Registrar urgencia"}
          </button>
          <button
            type="button"
            disabled={loading || !hayDatosEnFormulario}
            onClick={() => { setForm(initialForm); setOkMsg(null); setErrMsg(null); }}
            className="rounded-2xl border px-4 py-2 hover:bg-slate-50 disabled:opacity-60"
          >
            Limpiar
          </button>
        </div>
      </form>

      <section className="mt-12">
        <h2 className="text-xl font-semibold tracking-tight">Lista de espera</h2>
        {loadingIngresos && ingresos.length === 0 ? (
          <div className="mt-4 rounded-xl border p-8 text-center text-slate-500">
            Cargando ingresos...
          </div>
        ) : ingresos.length === 0 ? (
          <div className="mt-4 rounded-xl border p-8 text-center text-slate-500">
            No hay ingresos pendientes
          </div>
        ) : (
          <div className="mt-4 grid gap-4 md:grid-cols-2">
            {ingresos.map((ingreso, idx) => (
              <article
                key={`${ingreso.cuilPaciente}-${idx}`}
                className="rounded-xl border bg-white p-4 shadow-sm"
              >
                <header className="mb-2 flex items-center justify-between gap-2">
                  <div>
                    <p className="text-sm text-slate-500">Paciente</p>
                    <h3 className="text-lg font-semibold text-slate-900">
                      {ingreso.apellidoPaciente}, {ingreso.nombrePaciente}
                    </h3>
                    <p className="text-xs text-slate-500">CUIL: {ingreso.cuilPaciente}</p>
                  </div>
                  <span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-700">
                    {ingreso.nivelEmergencia}
                  </span>
                </header>
                <p className="text-sm text-slate-700 whitespace-pre-line">{ingreso.informe}</p>

                <dl className="mt-4 grid grid-cols-2 gap-2 text-xs text-slate-600">
                  {ingreso.temperatura != null && (
                    <div>
                      <dt className="font-semibold text-slate-500">Temp.</dt>
                      <dd>{ingreso.temperatura} °C</dd>
                    </div>
                  )}
                  {ingreso.frecuenciaCardiaca && (
                    <div>
                      <dt className="font-semibold text-slate-500">FC</dt>
                      <dd>{ingreso.frecuenciaCardiaca} lpm</dd>
                    </div>
                  )}
                  {ingreso.frecuenciaRespiratoria && (
                    <div>
                      <dt className="font-semibold text-slate-500">FR</dt>
                      <dd>{ingreso.frecuenciaRespiratoria} rpm</dd>
                    </div>
                  )}
                  {ingreso.tensionSistolica && (
                    <div>
                      <dt className="font-semibold text-slate-500">TA Sist.</dt>
                      <dd>{ingreso.tensionSistolica} mmHg</dd>
                    </div>
                  )}
                  {ingreso.tensionDiastolica && (
                    <div>
                      <dt className="font-semibold text-slate-500">TA Diast.</dt>
                      <dd>{ingreso.tensionDiastolica} mmHg</dd>
                    </div>
                  )}
                </dl>
              </article>
            ))}
          </div>
        )}
      </section>
    </div>
  );
}
