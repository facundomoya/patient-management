import { X } from "lucide-react";

export default function UrgenciaModal({ open, onClose, children }: any) {
  if (!open) return null;

  return (
    <div className="fixed inset-0 bg-black/10 flex items-center justify-center z-50 p-4">

      <div className="bg-white w-full max-w-2xl rounded-2xl shadow-xl p-6 relative max-h-[90vh] overflow-y-auto">
        {/* BOTÓN CERRAR */}
        <button
          onClick={onClose}
          className="absolute right-4 top-4 text-gray-500 hover:text-gray-700"
        >
          <X size={20} />
        </button>

        {children}
      </div>

    </div>
  );
}
