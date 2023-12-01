import { ColorScheme, ColorSchemeProvider, MantineProvider } from "@mantine/core";
import { useLocalStorage } from "@mantine/hooks";
import { ModalsProvider } from "@mantine/modals";
import "./App.css";
import "./i18n";
import { AxiosInterceptor } from "./api/api";
import darkTheme from "./themes/darkTheme";
import lightTheme from "./themes/lightTheme";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Notifications, showNotification } from "@mantine/notifications";
import { StudioRoutes } from "./router";
import { DatesProvider } from "@mantine/dates";
import "dayjs/locale/tr";
import "dayjs/locale/en";
import { AxiosError } from "axios";
import { getPreferredLanguage } from "./utils/LocalStorageUtils";

function retry(failureCount: number, error: unknown) {
  const maximumNumberOfFailures = 0;

  if (error instanceof AxiosError) {
    // if there is no response dont retry
    if (!error.response) {
      return false;
    }

    // if there is a response retry if the status code is 5xx
    return failureCount < maximumNumberOfFailures && error.response?.status >= 500;
  }

  return failureCount < maximumNumberOfFailures;
}

function onQueryError(error: unknown) {
  if (error instanceof AxiosError) {
    if (!error.response) {
      showNotification({
        id: "server-no-response",
        title: "Sunucu ile bağlantı kurulamadı",
        message: "Lütfen internet bağlantınızı kontrol edin",
        color: "red",
        autoClose: 5000,
      });
    } else if (error.response?.status >= 500) {
      showNotification({
        id: "server-error",
        title: "Sunucu hatası",
        message: "Lütfen daha sonra tekrar deneyin",
        color: "red",
        autoClose: 5000,
      });
    }
  }
}

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      cacheTime: 5000,
      staleTime: 5000,
      refetchOnWindowFocus: false,
      retry,
      onError: onQueryError,
    },
    mutations: {
      retry: 0,
    },
  },
});

function App() {
  const [colorScheme, setColorScheme] = useLocalStorage<ColorScheme>({
    key: "theme",
    defaultValue: "light",
    getInitialValueInEffect: true,
  });

  const toggleColorScheme = (value?: ColorScheme) => setColorScheme(value || (colorScheme === "dark" ? "light" : "dark"));

  return (
    <ColorSchemeProvider colorScheme={colorScheme} toggleColorScheme={toggleColorScheme}>
      <MantineProvider withGlobalStyles withNormalizeCSS theme={colorScheme === "dark" ? darkTheme : lightTheme}>
        <Notifications position="top-center" autoClose={5000} />
        <AxiosInterceptor />
        <ModalsProvider>
          <QueryClientProvider client={queryClient}>
            <DatesProvider settings={{ locale: getPreferredLanguage() }}>
              <StudioRoutes />
            </DatesProvider>
          </QueryClientProvider>
        </ModalsProvider>
      </MantineProvider>
    </ColorSchemeProvider>
  );
}

export default App;
