import { AxiosError } from "axios";
import dayjs from "dayjs";
import React, { createContext, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../api/api";
import { getUserFromLocalStorage, removeUserFromLocalStorage, setUserToLocalStorage } from "../utils/LocalStorageUtils";

export interface AuthProps {
  email: string;
  username: string;
  displayName: string;
  title?: string;
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

export const AuthContext = createContext<AuthContextProps>(null!);

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<AuthProps | null>(null);
  const [isClientError, setIsClientError] = useState(false);
  const [isLoggingIn, setIsLoggingIn] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const from: string = location.state?.from?.pathname || "/";

  useEffect(() => {
    const userInLocalStorage = getUserFromLocalStorage();

    if (!user && !userInLocalStorage) {
      logout();
    }

    if (!user && userInLocalStorage) {
      if (dayjs(userInLocalStorage.expiresAt).isBefore(dayjs(new Date()))) {
        logout();
        return;
      } else {
        navigate(location, { replace: true });
      }

      setUser(userInLocalStorage);
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

  const value = {
    isClientError,
    isLoggingIn,
    getUser,
    login,
    logout,
    refresh,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
