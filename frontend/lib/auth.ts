export function getSessionToken(): string | null {
    if (typeof window === "undefined") return null;
    return localStorage.getItem("token");
}
  
export function isAuthenticated(): boolean {
    return !!getSessionToken();
}
  
export function logout() {
    localStorage.removeItem("token");
    window.location.href = "/login";
}
  