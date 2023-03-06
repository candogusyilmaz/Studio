import { NotificationProps } from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import axios, { AxiosError, AxiosResponse } from "axios";
import { useContext, useEffect } from "react";
import { AuthContext } from "../context/AuthContext";
import { getAccessTokenFromLocalStorage, setUserToLocalStorage } from "../utils/LocalStorageUtils";

const api = axios.create({
  baseURL: "http://localhost:8080/api/",
  withCredentials: true,
  xsrfHeaderName: "X-XSRF-TOKEN",
  xsrfCookieName: "XSRF-TOKEN",
});

function responseInterceptorSuccess(response: AxiosResponse<any, any>) {
  return response;
}

api.interceptors.request.use((config) => {
  if (!config?.headers) {
    throw new Error("Expected 'config' and 'config.headers' not to be undefined");
  }

  if (config.url === "auth/token" || config.url === "auth/refresh-token") {
    delete config.headers["Authorization"];
  } else {
    const token = getAccessTokenFromLocalStorage();

    if (!token) {
      throw new Error("TOKEN_NOT_FOUND");
    }

    config.headers["Authorization"] = "Bearer " + token;
  }

  return config;
});

function isUnauthorizedResponse(error: AxiosError<unknown, any>) {
  return error.response?.status === 401 && error.response?.headers["www-authenticate"]?.startsWith('Bearer error="invalid_token"');
}

export const AxiosInterceptor = ({ children }: any) => {
  const { logout } = useContext(AuthContext);

  useEffect(() => {
    const respInterceptor = api.interceptors.response.use(responseInterceptorSuccess, (error: AxiosError<unknown, any>) => {
      if (error instanceof Error && error.message === "TOKEN_NOT_FOUND") {
        logout();
        return Promise.reject("Görünüşe göre oturumunuz sona ermiş. Lütfen tekrar giriş yapın.");
      }

      if (isUnauthorizedResponse(error)) {
        return api
          .post("auth/refresh-token")
          .then((s) => {
            setUserToLocalStorage(s.data);
            return api(error.config ?? {});
          })
          .catch((s) => {
            logout();
            return Promise.reject(s);
          });
      }

      return Promise.reject(error);
    });

    return () => {
      api.interceptors.response.eject(respInterceptor);
    };
  }, []);

  return children;
};

function getErrorMessage(error: any) {
  let message;

  if (error instanceof AxiosError) {
    if (error.response?.data) {
      Object.values(error.response.data).forEach((val) => {
        message = val;
      });
    }

    if (error.code === "ERR_NETWORK") {
      message = "Sunucu ile bağlantı kurulamadı!";
    }
  }

  if (typeof error === "string") {
    message = error;
  }

  return message ?? "Bilinmeyen bir hata oluştu!";
}

export function showErrorNotification(error: any, props?: NotificationProps) {
  showNotification({
    message: getErrorMessage(error),
    color: "red",
    autoClose: 5000,
    styles: (theme) => ({
      root: {
        border: `1px solid ${theme.colorScheme === "dark" ? theme.colors.red[6] : theme.colors.red[4]}`,
      },
    }),
    ...props,
  });
}

export default api;
