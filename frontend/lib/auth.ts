"use client";

import Cookies from "js-cookie";

export function setToken(token: string) {
  Cookies.set("token", token, {
    expires: 7,
    secure: process.env.NODE_ENV === "production",
    sameSite: "strict",
    path: "/",
  });
}

export function getToken() {
  return Cookies.get("token");
}

export function logout() {
  Cookies.remove("token");
  window.location.href = "/login";
}
  