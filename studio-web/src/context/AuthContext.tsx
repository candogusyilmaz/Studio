import { showNotification } from "@mantine/notifications";
import { AxiosError } from "axios";
import React, { createContext, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../api/api";
import { removeUserFromLocalStorage, setUserToLocalStorage } from "../helper/helper";

export interface AuthProps {
  email: string;
  username: string;
  displayName: string;
  accessToken: string;
  expiresAt: string;
  permissions: string[];
}

interface AuthProviderProps {
  children: React.ReactNode;
}

interface AuthContextProps {
  isClientError: boolean;
  user?: AuthProps | null;
  login: (username: string, password: string) => void;
  logout: () => void;
  refresh: () => void;
}

export const AuthContext = createContext<AuthContextProps>({
  isClientError: false,
  login: (u, p) => {},
  logout: () => {},
  refresh: () => {},
});

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<AuthProps | null>(null);
  const [isClientError, setIsClientError] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const from: string = location.state?.from?.pathname || "/";

  function login(username: string, password: string) {
    api
      .post("auth/token", {
        username: username,
        password: password,
      })
      .then((s) => {
        setUser(s.data);
        setIsClientError(false);
        setUserToLocalStorage(s.data);
        navigate(from, { replace: true });
      })
      .catch((s: AxiosError) => {
        if (!s.response) {
          showNotification({
            title: "Sunucu Hatası",
            message: "Sunucularımıza bağlanırken bir problem oluştu. Lütfen daha sonra tekrar deneyin.",
            color: "red",
            autoClose: 5000,
          });
          return;
        }

        if (s.response.status >= 500) {
          showNotification({
            title: "Sunucu Hatası",
            message: "Sunucularımızda bir problem yaşıyoruz. Lütfen daha sonra tekrar deneyin.",
            color: "red",
            autoClose: 5000,
          });
          return;
        }

        setIsClientError(true);
      });
  }

  async function refresh() {
    api
      .post("auth/refresh-token")
      .then((s) => {
        setUser(s.data);
        setUserToLocalStorage(s.data);
      })
      .catch(() => {
        logout();
      });
  }

  function logout() {
    setUser(null);
    setIsClientError(false);
    removeUserFromLocalStorage();
    navigate("/login");
  }

  return <AuthContext.Provider value={{ isClientError, user, login, logout, refresh }}>{children}</AuthContext.Provider>;
};
