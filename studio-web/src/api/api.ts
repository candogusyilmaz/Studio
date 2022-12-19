import axios, { AxiosError, AxiosRequestConfig, AxiosResponse } from "axios";
import { useContext, useEffect } from "react";
import { AuthContext } from "../context/AuthContext";
import { getAccessTokenFromLocalStorage, setUserToLocalStorage } from "../helper/helper";

const api = axios.create({
  baseURL: "http://localhost:8080/api/",
  withCredentials: true,
  xsrfHeaderName: "X-XSRF-TOKEN",
  xsrfCookieName: "XSRF-TOKEN",
});

function requestInterceptor(config: AxiosRequestConfig<any>) {
  if (!config?.headers) {
    throw new Error("Expected 'config' and 'config.headers' not to be undefined");
  }

  if (config.url === "auth/token" || config.url === "auth/refresh-token") {
    delete config.headers["Authorization"];
  } else {
    config.headers["Authorization"] = "Bearer " + getAccessTokenFromLocalStorage();
  }

  return config;
}

function responseInterceptorSuccess(response: AxiosResponse<any, any>) {
  return response;
}

export default api;

export const AxiosInterceptor = ({ children }: any) => {
  const { logout } = useContext(AuthContext);

  useEffect(() => {
    const reqInterceptor = api.interceptors.request.use(requestInterceptor);
    const respInterceptor = api.interceptors.response.use(responseInterceptorSuccess, (error: AxiosError<unknown, any>) => {
      if (
        error.response?.status === 401 &&
        error.response?.headers["www-authenticate"]?.startsWith(
          "Bearer error=\"invalid_token\", error_description=\"An error occurred while attempting to decode the Jwt: Jwt expired at",
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
        return Promise.reject(error);
      }
    });

    return () => {
      api.interceptors.request.eject(reqInterceptor);
      api.interceptors.response.eject(respInterceptor);
    };
  }, []);

  return children;
};
