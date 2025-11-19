
const API_BASE_URL = process.env.URL_API ?? "http://localhost:8080";

export async function POST(req: Request) {

  const body = await req.json();

  const response = await fetch(`${API_BASE_URL}api/v1/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json"
    },
    body: JSON.stringify(body),
  });
  
  const data = await response.json();
  
  return Response.json(data);
}