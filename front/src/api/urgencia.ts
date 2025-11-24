import { http } from "./http";

export type NivelEmergencia =
  | "Critica"
  | "Emergencia"
  | "Urgencia"
  | "Urgencia Menor"
  | "Sin Urgencia";

export interface RegistrarUrgenciaDTO {
  cuilPaciente: string;
  informe: string;
  nivelEmergencia: NivelEmergencia;
  enfermeraCuil: string;
  enfermeraNombre: string;
  enfermeraApellido: string;
  temperatura?: number | null;
  frecuenciaCardiaca?: number | null;
  frecuenciaRespiratoria?: number | null;
  tensionSistolica?: number | null;
  tensionDiastolica?: number | null;
}

export interface IngresoUrgencia {
  cuilPaciente: string;
  nombrePaciente: string;
  apellidoPaciente: string;
  nivelEmergencia: NivelEmergencia | string;
  informe: string;
  temperatura: number | null;
  frecuenciaCardiaca: string | number | null;
  frecuenciaRespiratoria: string | number | null;
  tensionSistolica: string | number | null;
  tensionDiastolica: string | number | null;
}

export async function registrarUrgencia(payload: RegistrarUrgenciaDTO): Promise<void> {
  await http.post("/urgencias", payload);
}

export async function listarIngresosPendientes(): Promise<IngresoUrgencia[]> {
  const { data } = await http.get<IngresoUrgencia[]>("/urgencias/lista-espera");
  return data;
}
