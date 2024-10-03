import { jwtDecode } from "jwt-decode";
import { tokenKey } from "../const/constants";

export function getToken() {
  return sessionStorage.getItem(tokenKey);
}

export function getAuthHeader() {
  return `Bearer ${getToken()}`;
}

export function getRoleFromToken() {
  const token = getToken();
  if (!token) {
    console.error("Token not found in sessionStorage");
    return null;
  }

  try {
    const decodedToken = jwtDecode(token);
    return decodedToken?.role || null;
  } catch (error) {
    console.error("Failed to decode token:", error);
    return null;
  }
}
