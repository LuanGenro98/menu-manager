import { Item } from "@/types/next-props";
import { cookies } from "next/headers";

const API_BASE_URL = process.env.URL_API ?? "http://localhost:8080";

export async function GET(req: Request) {
  const cookieStore = cookies();
  const { searchParams } = new URL(req.url);
  const category = searchParams.get("category");
  const token = (await cookieStore).get("token")?.value;

  const url = category ? `${API_BASE_URL}api/v1/items?categoryId=${category}` : `${API_BASE_URL}api/v1/items`;

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

  const incoming = await req.formData();
  const formData = new FormData();

  // pegar o request (que chega como Blob)
  const request = incoming.get("request");
  if (request) formData.append("request", request);

  // pegar image
  const image = incoming.get("image");
  if (image instanceof File) {
    formData.append("image", image, image.name);
  }

  const response = await fetch(`${API_BASE_URL}api/v1/items`, {
    method: "POST",
    headers: {
      Accept: "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: formData,
  });

  const data = await response.json();
  return Response.json(data);
}
