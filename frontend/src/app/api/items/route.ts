import { cookies } from "next/headers";

const API_BASE_URL = process.env.URL_API ?? "http://localhost:8080";

export async function GET() {
  const cookieStore = cookies();
  const token = (await cookieStore).get("token")?.value;

  const response = await fetch(`${API_BASE_URL}api/v1/items`, {
    headers: {
      "Content-Type": "application/json",
      "Accept": "application/json",
      "Authorization": `Bearer ${token}`,
    },
  });
  
  const data = await response.json();
  
  return Response.json(data);
}
  