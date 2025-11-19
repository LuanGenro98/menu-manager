import { cookies } from "next/headers";
import { NextRequest } from "next/server";

const API_BASE_URL = process.env.URL_API ?? "http://localhost:8080";

export async function GET() {
  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value;

  const response = await fetch(`${API_BASE_URL}api/v1/categories`, {
    headers: {
      "Content-Type": "application/json",
      "Accept": "application/json",
      "Authorization": `Bearer ${token}`,
    },
  });
  
  const data = await response.json();
  
  return Response.json(data);
}

export async function POST(req: NextRequest) {
  const cookieStore = await cookies();
  const token = cookieStore.get("token")?.value;

  const body = await req.json();

  const response = await fetch(`${API_BASE_URL}api/v1/categories`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: JSON.stringify(body),
  });
  
  const data = await response.json();
  
  return Response.json(data);
}