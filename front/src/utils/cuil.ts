export function validarCuil(cuil: string): boolean {
  if (!cuil) return false;

  // dejar solo dígitos
  const digits = cuil.replace(/\D/g, "");
  if (digits.length !== 11) return false;

  const w = [5,4,3,2,7,6,5,4,3,2];
  let sum = 0;

  for (let i = 0; i < 10; i++) {
    sum += Number(digits[i]) * w[i];
  }

  const mod = 11 - (sum % 11);
  const dv = mod === 11 ? 0 : mod === 10 ? 9 : mod;
  const dvReal = Number(digits[10]);

  return dv === dvReal;
}

export function formatearCuil(cuil: string): string {
  const d = cuil.replace(/\D/g, "");
  if (d.length !== 11) return cuil;
  return `${d.slice(0, 2)}-${d.slice(2, 10)}-${d.slice(10)}`;
}
