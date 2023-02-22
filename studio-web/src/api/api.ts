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
    config.headers["Authorization"] = "Bearer " + getAccessTokenFromLocalStorage();
  }

  return config;
});

export const AxiosInterceptor = ({ children }: any) => {
  const { logout } = useContext(AuthContext);

  useEffect(() => {
    const respInterceptor = api.interceptors.response.use(responseInterceptorSuccess, (error: AxiosError<unknown, any>) => {
      if (
        error.response?.status === 401 &&
        error.response?.headers["www-authenticate"]?.startsWith(
          'Bearer error="invalid_token", error_description="An error occurred while attempting to decode the Jwt: Jwt expired at',
        )
      ) {
        return api
          .post("auth/refresh-token")
          .then((s) => {
            setUserToLocalStorage(s.data);
            return api(error.config ?? {});
          })
          .catch((s) => {
            logout();
            return s;
          });
      } else {
        if (!error.response) {
          showNotification({
            id: "server-error-no-response",
            title: "Sunucu Hatası",
            message: "Sunucularımıza bağlanırken bir problem oluştu. Lütfen daha sonra tekrar deneyin.",
            color: "red",
            autoClose: 5000,
          });
        } else if (error.response.status >= 500) {
          showNotification({
            id: "server-error-500",
            title: "Sunucu Hatası",
            message: "Sunucularımızda bir problem yaşıyoruz. Lütfen daha sonra tekrar deneyin.",
            color: "red",
            autoClose: 5000,
          });
        }

        return Promise.reject(error);
      }
    });

    return () => {
      api.interceptors.response.eject(respInterceptor);
    };
  }, []);

  return children;
};

export function getErrorMessage(axiosError: AxiosError) {
  let message;

  Object.values(axiosError?.response?.data ?? {}).forEach((val) => {
    message = val;
  });

  return message ?? "Bilinmeyen bir hata oluştu!";
}

export default api;
