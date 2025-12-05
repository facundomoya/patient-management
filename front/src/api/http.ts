import axios from "axios";
import { getUsuarioLogueado } from "./auth";

const baseURL =
  import.meta.env.VITE_API_URL?.toString() ||
  "http://localhost:8080/api"; // Spring Boot default port

export const http = axios.create({
  baseURL,
  headers: {
    "Content-Type": "application/json",
  },
  // withCredentials: true, // si necesitás cookies/sesiones
});

// Interceptor para agregar el header de autenticación
http.interceptors.request.use((config) => {
  const usuario = getUsuarioLogueado();
  if (usuario) {
    config.headers["X-User-Email"] = usuario.email;
  }
  return config;
});

// Un helper para extraer mensajes del backend (DomainException)
export function extractErrorMessage(err: unknown): string {
  if (axios.isAxiosError(err)) {
    const data = err.response?.data as any;
    if (typeof data === "string") return data;
    if (data?.message) return String(data.message);
    if (data?.error) return String(data.error);
    if (data?.msg) return String(data.msg);
    return `HTTP ${err.response?.status ?? ""}: ${err.message}`;
  }
  return (err as any)?.message ?? "Error desconocido";
}
