"use client"

import Cookies from "js-cookie";

const API_BASE_URL = process.env.URL_API ?? "http://localhost:8080";

export async function apiPost(endpoint: string, data: any): Promise<any> {
  try {
    const token = Cookies.get("token");
    const url = `${API_BASE_URL}/api/v1/${endpoint}`;
  
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
      body: JSON.stringify(data),
    });
  
    if (!response.ok) {
      const body = await response.text();
      console.error("API error:", body);
      throw new Error(`HTTP ${response.status}`);
    }
  
    return response.json();
  } catch (error) {
    console.error("API Error:", error);
    console.log((error as Error).message)
   
    return { error: true, message: (error as Error).message || "Unexpected error" };
  }
}

export async function apiGet(endpoint: string): Promise<any> {
  try {
    const token = Cookies.get("token");

    const url = `${API_BASE_URL}/api/v1/${endpoint}`;

    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        "Accept": "application/json",
        ...(token ? { "Authorization": `Bearer ${token}` } : {}),
      },
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      return { error: true, message: errorData.message || "Request failed" };
    }

    return await response.json();
  } catch (error) {
    console.error("API Error:", error);
    return { error: true, message: (error as Error).message || "Unexpected error" };
  }
}