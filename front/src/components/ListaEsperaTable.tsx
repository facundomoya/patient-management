export function ListaEsperaTable({ ingresos }: any) {
  function colorNivel(nivel: string) {
    switch (nivel.toUpperCase()) {
      case "CRITICA":
        return "bg-red-100 text-red-700";
      case "EMERGENCIA":
        return "bg-orange-100 text-orange-700";
      case "URGENCIA":
        return "bg-yellow-100 text-yellow-700";
      case "URGENCIA MENOR":
        return "bg-green-100 text-green-700";
      case "SIN URGENCIA":
        return "bg-blue-100 text-blue-700";
      default:
        return "bg-gray-100 text-gray-700";
    }
  }

  return (
    <div className="mt-6 rounded-l border bg-white shadow-sm overflow-hidden">
      <table className="w-full text-sm">

        {/* HEADER */}
        <thead className="bg-[#C7D3DD]/40 text-[#37393A]">
          <tr className="text-center">
            <th className="p-3 text-[#77B6EA]">Apellido</th>
            <th className="p-3 text-[#77B6EA]">Nombre</th>
            <th className="p-3 text-[#77B6EA]">CUIL</th>
            <th className="p-3 text-[#77B6EA]">Nivel</th>
            <th className="p-3 text-[#77B6EA]">Informe</th>
            <th className="p-3 text-[#77B6EA]">Temp (°C)</th>
            <th className="p-3 text-[#77B6EA]">FC (lpm)</th>
            <th className="p-3 text-[#77B6EA]">FR (rpm)</th>
            <th className="p-3 text-[#77B6EA]">TA Sist.</th>
            <th className="p-3 text-[#77B6EA]">TA Diast.</th>
          </tr>
        </thead>

        {/* BODY */}
        <tbody className="text-center">
          {ingresos.map((ingreso: any, idx: number) => (
            <tr
              key={idx}
              className="border-t hover:bg-slate-50"
            >
              <td className="p-3  font-medium">
                {ingreso.apellidoPaciente}
              </td>
              <td className="p-3  font-medium">
               {ingreso.nombrePaciente}
              </td>
              <td className="p-3 ">{ingreso.cuilPaciente}</td>

              <td className="p-3 ">
                <span
                  className={`px-2 py-1 rounded font-semibold text-xs ${colorNivel(
                    ingreso.nivelEmergencia
                  )}`}
                >
                  {ingreso.nivelEmergencia}
                </span>
              </td>

              <td className="p-3">{ingreso.informe}</td>

              <td className="p-3">{ingreso.temperatura ?? "-"}</td>
              <td className="p-3">{ingreso.frecuenciaCardiaca ?? "-"}</td>
              <td className="p-3">{ingreso.frecuenciaRespiratoria ?? "-"}</td>
              <td className="p-3">{ingreso.tensionSistolica ?? "-"}</td>
              <td className="p-3">{ingreso.tensionDiastolica ?? "-"}</td>
            </tr>
          ))}
        </tbody>

      </table>
    </div>
  );
}
