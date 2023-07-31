import { AuthProps } from "../context/AuthContext";

const userKey = "juser";

export function getUserFromLocalStorage(): AuthProps | null {
  const user = localStorage.getItem(userKey);

  if (user === null) return null;

  return JSON.parse(user);
}

export function getAccessTokenFromLocalStorage(): string | null {
  const user = getUserFromLocalStorage();

  if (user === null) return null;

  return user.token;
}

export function getPreferredLanguage() {
  return "tr";
}
