import { cookies } from "next/headers";
import { NextRequest } from "next/server";

const API_BASE_URL = process.env.URL_API ?? "http://localhost:8080";

export async function PATCH(
  req: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id } = await params;
    
  const cookieStore = cookies();
  const token = (await cookieStore).get("token")?.value;

  const body = await req.json();

  const response = await fetch(`${API_BASE_URL}api/v1/demands/${id}/status`, {
    method: "PATCH",
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