import { cookies } from "next/headers";
import { NextRequest } from "next/server";

const API_BASE_URL = process.env.URL_API ?? "http://localhost:8080";

export async function PUT(
  req: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id } = await params;
  const cookieStore = cookies();
  const token = (await cookieStore).get("token")?.value;

  const incoming = await req.formData();
  const formData = new FormData();

  const request = incoming.get("request");
  if (request) formData.append("request", request);

  const image = incoming.get("image");
  if (image instanceof File) {
    formData.append("image", image, image.name);
  }

  const response = await fetch(`${API_BASE_URL}api/v1/items/${id}`, {
    method: "PUT",
    headers: {
      Accept: "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: formData,
  });

  const data = await response.json();
  return Response.json(data);
}


export async function DELETE(
  req: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  const { id } = await params;
  const cookieStore = cookies();
  const token = (await cookieStore).get("token")?.value;

  const response = await fetch(`${API_BASE_URL}api/v1/items/${id}`, {
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
