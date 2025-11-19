import { Item } from "@/types/next-props";
import { cookies } from "next/headers";

const API_BASE_URL = process.env.URL_API ?? "http://localhost:8080";

export async function GET(req: Request) {
  const cookieStore = cookies();
  const token = (await cookieStore).get("token")?.value;
  const { searchParams } = new URL(req.url);
  const status = searchParams.get("status");

  const url = status ? `${API_BASE_URL}api/v1/demands?status=${status}` : `${API_BASE_URL}api/v1/demands`;


  const response = await fetch(url, {
    headers: {
      "Content-Type": "application/json",
      "Accept": "application/json",
      "Authorization": `Bearer ${token}`,
    },
  });
  
  const data = await response.json();
  
  return Response.json(data);
}
export async function POST(req: Request) {
  const cookieStore = cookies();
  const token = (await cookieStore).get("token")?.value;

  const body = await req.json();

  const response = await fetch(`${API_BASE_URL}api/v1/demands`, {
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