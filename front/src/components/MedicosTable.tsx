import type { Medico } from "../api/medicos";

interface Props {
  medicos: Medico[];
}

export function MedicosTable({ medicos }: Props) {
  return (
    <div className="mt-6 rounded-l border bg-white shadow-md overflow-hidden">
      <table className="w-full text-sm">
        
        {/* HEADER */}
        <thead className=" bg-[#C7D3DD]/40 text-[#37393A]">
          <tr>
            <th className="p-3 text-left font-semibold text-[#77B6EA]">Apellido</th>
            <th className="p-3 text-left font-semibold text-[#77B6EA]">Nombre</th>
            <th className="p-3 text-left font-semibold text-[#77B6EA]">CUIL</th>
          </tr>
        </thead>

        {/* BODY */}
        <tbody>
          {medicos.map((m) => (
            <tr
              key={m.cuil}
              className="border-t hover:bg-slate-50 transition-colors"
            >
              {/* APELLIDO */}
              <td className="p-3 font-medium text-[#2f3133]">
                <span className="block">{m.apellido}</span>
              </td>
              {/* NOMBRE */}
              <td className="p-3 font-medium text-[#2f3133]">
                <span className="block">{m.nombre}</span>
              </td>

              {/* CUIL */}
              <td className="p-3 text-[#424547]">{m.cuil}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

