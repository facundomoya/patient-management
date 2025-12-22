import { http } from "./http";

export type Cuil = string;

export interface AltaMedicoDTO {
  cuil: Cuil;
  apellido: string;
  nombre: string;
}

export interface Medico {
  cuil: Cuil;
  apellido: string;
  nombre: string;
}

export async function createMedico(payload: AltaMedicoDTO): Promise<Medico> {
  const { data } = await http.post<Medico>("/medicos", payload);
  return data;
}

export async function listMedicos(): Promise<Medico[]> {
  const { data } = await http.get<Medico[]>("/medicos");
  return data;
}
