import type { Paciente } from "../api/pacientes";

interface Props {
  pacientes: Paciente[];
}

export function PacientesTable({ pacientes }: Props) {
  return (
    <div className="mt-6 rounded-l border bg-white shadow-md overflow-hidden">
      <table className="w-full text-sm">
        
        {/* HEADER */}
        <thead className=" bg-[#C7D3DD]/40 text-[#37393A]">
          <tr>
            <th className="p-3 text-left font-semibold text-[#77B6EA]">Apellido</th>
            <th className="p-3 text-left font-semibold text-[#77B6EA]">Nombre</th>
            <th className="p-3 text-left font-semibold text-[#77B6EA]">CUIL</th>
            <th className="p-3 text-left font-semibold text-[#77B6EA]">Domicilio</th>
            <th className="p-3 text-left font-semibold text-[#77B6EA]">Obra Social</th>
          </tr>
        </thead>

        {/* BODY */}
        <tbody>
          {pacientes.map((p) => (
            <tr
              key={p.cuil}
              className="border-t hover:bg-slate-50 transition-colors"
            >
              {/* NOMBRE + APELLIDO */}
              <td className="p-3 font-medium text-[#2f3133]">
                <span className="block">{p.apellido}</span>
              </td>
              <td className="p-3 font-medium text-[#2f3133]">
                <span className="block">{p.nombre}</span>
              </td>

              {/* CUIL */}
              <td className="p-3 text-[#424547]">{p.cuil}</td>

              {/* DOMICILIO */}
              <td className="p-3 text-[#424547]">
                {p.domicilio.calle} {p.domicilio.numero},{" "}
                {p.domicilio.localidad}
              </td>

              {/* OBRA SOCIAL */}
              <td className="p-3">
                {p.obraSocial ? (
                  <span className="
                    inline-flex items-center gap-1
                    px-2 py-1 rounded-lg
                    bg-[#77B6EA]/20 text-[#2a4f91] 
                    text-xs font-semibold
                  ">
                    {p.obraSocial.nombre}  
                    <span className="opacity-70">
                      #{p.obraSocial.numeroAfiliado}
                    </span>
                  </span>
                ) : (
                  <span className="text-slate-500 text-xs italic">
                    Sin obra social
                  </span>
                )}
              </td>

            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
