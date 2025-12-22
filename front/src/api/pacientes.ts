import { http } from "./http";

export type Cuil = string;

export interface Domicilio {
  calle: string;
  numero: number;
  localidad: string;
}

export interface ObraSocial {
  codigo: string;    
  nombre: string;
}

export interface AltaPacienteDTO {
  cuil: Cuil;
  apellido: string;
  nombre: string;
  domicilio: Domicilio;

  
  obraSocialCodigo?: string;
  numeroAfiliado?: string;
}

export interface Paciente {
  cuil: Cuil;
  apellido: string;
  nombre: string;
  domicilio: Domicilio;
  obraSocial?: {
    codigo: string;
    nombre: string;
    numeroAfiliado: string;
  } | null;
}


export async function createPaciente(payload: AltaPacienteDTO): Promise<Paciente> {
  const { data } = await http.post<Paciente>("/pacientes", payload);
  return data;
}

export async function listObrasSociales(): Promise<ObraSocial[]> {
  const { data } = await http.get<ObraSocial[]>("/obras-sociales");
  return data;
}

export async function listPacientes(): Promise<Paciente[]> {
  const { data } = await http.get<Paciente[]>("/pacientes");
  return data;
}