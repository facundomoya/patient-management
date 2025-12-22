// src/hooks/useCuilInput.ts
import { useState } from "react";
import { validarCuil, formatearCuil } from "../utils/cuil";

export function useCuilInput(initial: string = "") {
  const [value, setValue] = useState(initial);
  const [valido, setValido] = useState<boolean | null>(null);

  function handleChange(raw: string) {
    let digits = raw.replace(/\D/g, "");

    if (digits.length > 11) digits = digits.slice(0, 11);

    let formatted = digits;
    if (digits.length > 2 && digits.length <= 10) {
      formatted = `${digits.slice(0, 2)}-${digits.slice(2)}`;
    } else if (digits.length === 11) {
      formatted = formatearCuil(digits);
    }

    setValue(formatted);

    if (digits.length === 11) {
      setValido(validarCuil(formatted));
    } else {
      setValido(null);
    }
  }

  return { value, valido, handleChange };
}
