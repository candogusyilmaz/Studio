import { AuthProps } from "../context/AuthContext";

const userKey = "juser";

export function getUserFromLocalStorage(): AuthProps | null {
  const user = localStorage.getItem(userKey);

  if (user === null) return null;

  return JSON.parse(user);
}

export function setUserToLocalStorage(user: AuthProps) {
  localStorage.setItem(userKey, JSON.stringify(user));
}

export function removeUserFromLocalStorage() {
  localStorage.removeItem(userKey);
}

export function getAccessTokenFromLocalStorage(): string | null {
  const user = getUserFromLocalStorage();

  if (user === null) return null;

  return user.accessToken;
}

export function getPreferredLanguage() {
  return "tr";
}
