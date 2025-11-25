import { X } from "lucide-react";
import {type ReactNode } from "react";

interface PacienteModalProps {
  open: boolean;
  onClose: () => void;
  children: ReactNode;
}

export default function PacienteModal({ open, onClose, children }: PacienteModalProps) {
  if (!open) return null;

  return (
    <div className="fixed inset-0 bg-black/30 flex items-center justify-center z-50">
      <div className="bg-white w-full max-w-3xl rounded-2xl shadow-xl p-6 relative">

        {/* BOTÓN CERRAR */}
        <button
          onClick={onClose}
          className="absolute right-4 top-4 text-gray-500 hover:text-gray-900 transition"
        >
          <X size={22} />
        </button>

        {children}
      </div>
    </div>
  );
}
