import { AxiosError } from "axios";
import dayjs from "dayjs";
import React, { createContext, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../api/api";
import { getUserFromLocalStorage, removeUserFromLocalStorage, setUserToLocalStorage } from "../helper/helper";

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
  isLoggingIn: boolean;
  getUser: () => AuthProps | null;
  login: (username: string, password: string) => void;
  logout: () => void;
  refresh: () => void;
}

export const AuthContext = createContext<AuthContextProps>({
  isClientError: false,
  isLoggingIn: false,
  getUser: () => null,
  login: (u, p) => {},
  logout: () => {},
  refresh: () => {},
});

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<AuthProps | null>(null);
  const [isClientError, setIsClientError] = useState(false);
  const [isLoggingIn, setIsLoggingIn] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const from: string = location.state?.from?.pathname || "/";

  useEffect(() => {
    const userInLocalStorage = getUserFromLocalStorage();

    if (!user && userInLocalStorage) {
      setUser(userInLocalStorage);
    }

    if (!user && !userInLocalStorage) {
      logout();
    }
  }, []);

  function getUser() {
    if (user) {
      return user;
    }

    return getUserFromLocalStorage();
  }

  function login(username: string, password: string) {
    setIsLoggingIn(true);
    api
      .post("auth/token", {
        username: username,
        password: password,
      })
      .then((s) => {
        setIsLoggingIn(false);
        setUser(s.data);
        setIsClientError(false);
        setUserToLocalStorage(s.data);
        navigate(from, { replace: true });
      })
      .catch((s: AxiosError) => {
        if (!s.response || s.response.status >= 500) {
          return;
        }

        setIsClientError(true);
      })
      .finally(() => {
        setTimeout(() => {
          setIsLoggingIn(false);
        }, 500);
      });
  }

  function refresh() {
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

  return <AuthContext.Provider value={{ isClientError, isLoggingIn, getUser, login, logout, refresh }}>{children}</AuthContext.Provider>;
};
