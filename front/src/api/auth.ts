import { http } from "./http";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface Usuario {
  email: string;
  rol: string;
  cuilActor: string;
  nombre: string;
  apellido: string;
}

export async function login(payload: LoginRequest): Promise<Usuario> {
  const { data } = await http.post<Usuario>("/auth/login", payload);
  return data;
}

export function getUsuarioLogueado(): Usuario | null {
  const usuarioStr = localStorage.getItem("usuario");
  if (!usuarioStr) return null;
  try {
    return JSON.parse(usuarioStr);
  } catch {
    return null;
  }
}

export function setUsuarioLogueado(usuario: Usuario): void {
  localStorage.setItem("usuario", JSON.stringify(usuario));
}

export function logout(): void {
  localStorage.removeItem("usuario");
}

