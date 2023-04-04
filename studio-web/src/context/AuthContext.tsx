import { AxiosError } from "axios";
import React, { createContext, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../api/api";
import usePersistedState from "../hooks/usePersistedState";

export interface AuthProps {
  displayName: string;
  title?: string;
  token: string;
  permissions: string[];
}

interface AuthProviderProps {
  children: React.ReactNode;
}

interface AuthContextProps {
  user: AuthProps | undefined;
  isClientError: boolean;
  isLoggingIn: boolean;
  setUser: (user: AuthProps | undefined) => void;
  login: (username: string, password: string) => void;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextProps>(null!);

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = usePersistedState<AuthProps | undefined>("juser");
  const [isClientError, setIsClientError] = useState(false);
  const [isLoggingIn, setIsLoggingIn] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const from: string = location.state?.from?.pathname || "/";

  useEffect(() => {
    if (!user) {
      logout();
    }

    navigate(location, { replace: true });
  }, []);

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

  function logout() {
    setUser(undefined);
    setIsClientError(false);
    navigate("/login");
  }

  const value = {
    isClientError,
    isLoggingIn,
    user,
    setUser,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
