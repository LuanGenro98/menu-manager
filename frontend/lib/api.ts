const API_BASE_URL = process.env.URL_API ?? "http://localhost:8080";

export async function apiPost<T>(endpoint: string, data: any): Promise<T> {
    try {

      let url = API_BASE_URL + "/api/v1/" + endpoint

      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      });
  
      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || "Request failed");
      }
  
      return (await response.json()) as T;
    } catch (error) {
      console.error("API Error:", error);
      throw error;
    }
  }
  