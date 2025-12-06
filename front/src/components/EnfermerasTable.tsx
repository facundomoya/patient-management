import type { Enfermera } from "../api/enfermeras";

interface Props {
  enfermeras: Enfermera[];
}

export function EnfermerasTable({ enfermeras }: Props) {
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
          {enfermeras.map((e) => (
            <tr
              key={e.cuil}
              className="border-t hover:bg-slate-50 transition-colors"
            >
              {/* APELLIDO */}
              <td className="p-3 font-medium text-[#2f3133]">
                <span className="block">{e.apellido}</span>
              </td>
              {/* NOMBRE */}
              <td className="p-3 font-medium text-[#2f3133]">
                <span className="block">{e.nombre}</span>
              </td>

              {/* CUIL */}
              <td className="p-3 text-[#424547]">{e.cuil}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

