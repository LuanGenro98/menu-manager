export const status = [
    { id: 1, value: "ORDERED", label: "Pedido realizado" },
    { id: 2, value: "PREPARING", label: "Preparando" },
    { id: 3, value: "READY", label: "Pronto" },
    { id: 4, value: "SERVED", label: "Servido" },
    { id: 5, value: "PAID", label: "Pago" },
    { id: 6, value: "CANCELED", label: "Cancelado" },
];

export function translateStatus(value: string): string {
    const item = status.find(s => s.value === value);
    return item ? item.label : value;
}
  