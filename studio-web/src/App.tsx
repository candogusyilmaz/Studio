import { ColorScheme, ColorSchemeProvider, MantineProvider } from "@mantine/core";
import { useLocalStorage } from "@mantine/hooks";
import { ModalsProvider } from "@mantine/modals";
import "./App.css";
import "./i18n";
import { AxiosInterceptor } from "./api/api";
import darkTheme from "./themes/darkTheme";
import lightTheme from "./themes/lightTheme";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Notifications } from "@mantine/notifications";
import { StudioRoutes } from "./router";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      cacheTime: 5000,
      staleTime: 5000,
      refetchOnWindowFocus: false,
    },
  },
});

function AxiosProvider() {
  return (
    <AxiosInterceptor>
      <QueryClientProvider client={queryClient}>
        <StudioRoutes />
      </QueryClientProvider>
    </AxiosInterceptor>
  );
}

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
        <Notifications position="top-center" />
        <ModalsProvider>
          <AxiosProvider />
        </ModalsProvider>
      </MantineProvider>
    </ColorSchemeProvider>
  );
}

export default App;
