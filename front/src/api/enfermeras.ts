import { http } from "./http";

// ---- Tipos de dominio ----
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

// ---- API ----

// Alta enfermera
export async function createEnfermera(payload: AltaEnfermeraDTO): Promise<Enfermera> {
  const { data } = await http.post<Enfermera>("/enfermeras", payload);
  return data;
}

// Listar todas las enfermeras
export async function listEnfermeras(): Promise<Enfermera[]> {
  const { data } = await http.get<Enfermera[]>("/enfermeras");
  return data;
}

