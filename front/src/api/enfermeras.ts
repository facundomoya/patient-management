import { http } from "./http";

export type Cuil = string;

export interface AltaEnfermeraDTO {
  cuil: Cuil;
  apellido: string;
  nombre: string;
}

export interface Enfermera {
  cuil: Cuil;
  apellido: string;
  nombre: string;
}

export async function createEnfermera(payload: AltaEnfermeraDTO): Promise<Enfermera> {
  const { data } = await http.post<Enfermera>("/enfermeras", payload);
  return data;
}

export async function listEnfermeras(): Promise<Enfermera[]> {
  const { data } = await http.get<Enfermera[]>("/enfermeras");
  return data;
}
