/**
 * Convert value to money from cents
 * @param {string} amount 
 * @returns {number} 
 */
export function convertRealToCents(amount: string){
    const numericPrice = parseFloat(amount.replace('/\./g', '').replace(',', '.'));
    const priceInCents = Math.round(numericPrice * 100);

    return priceInCents;
}

export function convertRealToAmerican(amount: string) {
    if (!amount) return "";
  
    return amount
      .replace(/\./g, "")
      .replace(",", ".");  
}

export function convertAmericanToReal(value: number | string) {
    if (value == null || value === "") return "";
  
    const num = typeof value === "string" ? parseFloat(value) : value;
  
    return num
      .toFixed(2)
      .replace(".", ",")
      .replace(/\B(?=(\d{3})+(?!\d))/g, ".");
  }
  