import { useState, useEffect } from "react";

interface CronometroEsperaProps {
  fechaInicio: string; 
}

export function CronometroEspera({ fechaInicio }: CronometroEsperaProps) {
  const [tiempoTranscurrido, setTiempoTranscurrido] = useState({ minutos: 0, segundos: 0 });

  useEffect(() => {
    if (!fechaInicio) return;

    const calcularTiempo = () => {
      const inicio = new Date(fechaInicio);
      const ahora = new Date();
      const diferencia = Math.floor((ahora.getTime() - inicio.getTime()) / 1000);

      if (diferencia < 0) {
        setTiempoTranscurrido({ minutos: 0, segundos: 0 });
        return;
      }

      const minutos = Math.floor(diferencia / 60);
      const segundos = diferencia % 60;

      setTiempoTranscurrido({ minutos, segundos });
    };

  
    calcularTiempo();

    const interval = setInterval(calcularTiempo, 1000);

    return () => clearInterval(interval);
  }, [fechaInicio]);

  return (
    <span className="font-mono text-sm">
      {String(tiempoTranscurrido.minutos).padStart(2, "0")}:
      {String(tiempoTranscurrido.segundos).padStart(2, "0")}
    </span>
  );
}
