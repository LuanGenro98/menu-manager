import { cookies } from "next/headers";

const API_BASE_URL = process.env.URL_API ?? "http://localhost:8080";

export async function PUT(
  req: Request,
  { params }: { params: { id: string } }
) {
  const { id } = await params;
  const cookieStore = cookies();
  const token = (await cookieStore).get("token")?.value;

  const body = await req.json();

  const response = await fetch(`${API_BASE_URL}api/v1/categories/${id}`, {
    method: "PUT",
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

export async function DELETE(
  req: Request,
  { params }: { params: { id: string } }
) {
  const { id } = await params;
  const cookieStore = cookies();
  const token = (await cookieStore).get("token")?.value;

  const response = await fetch(`${API_BASE_URL}api/v1/categories/${id}`, {
    method: "DELETE",
    headers: {
      Accept: "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
  });

  if (response.status === 204) {
    return Response.json({ success: true });
  }  

  const data = await response.json();
  return Response.json(data);
}
