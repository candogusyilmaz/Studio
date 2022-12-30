import { ColorScheme, ColorSchemeProvider, MantineProvider } from "@mantine/core";
import { useLocalStorage } from "@mantine/hooks";
import { ModalsProvider } from "@mantine/modals";
import { NotificationsProvider } from "@mantine/notifications";
import { Route, Routes } from "react-router-dom";
import "./App.css";
import DashboardLayout from "./layouts/DashboardLayout";
import { Login } from "./pages/Login";
import "./i18n";
import { AxiosInterceptor } from "./api/api";
import RequireAuth from "./components/shared/RequireAuth";
import { Unauthorized } from "./pages/Unauthorized";
import { NotFound } from "./pages/NotFound";
import darkTheme from "./themes/darkTheme";
import lightTheme from "./themes/lightTheme";

function AxiosProvider() {
  return (
    <AxiosInterceptor>
      <Routes>
        <Route element={<DashboardLayout />}>
          <Route element={<RequireAuth />}>
            <Route path="/" element={<div>This is dashboard</div>} />
          </Route>
          <Route element={<RequireAuth allowedRoles={["dd"]} />}>
            <Route path="/protected" element={<div>hello</div>} />
          </Route>
        </Route>

        <Route path="/login" element={<Login />} />
        <Route path="/unauthorized" element={<Unauthorized />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </AxiosInterceptor>
  );
}

function App() {
  const [colorScheme, setColorScheme] = useLocalStorage<ColorScheme>({
    key: "preferred-color-scheme",
    defaultValue: "light",
    getInitialValueInEffect: true,
  });

  const toggleColorScheme = (value?: ColorScheme) => setColorScheme(value || (colorScheme === "dark" ? "light" : "dark"));
  
  return (
    <ColorSchemeProvider colorScheme={colorScheme} toggleColorScheme={toggleColorScheme}>
      <MantineProvider withGlobalStyles withNormalizeCSS theme={colorScheme === "dark" ? darkTheme : lightTheme}>
        <ModalsProvider>
          <NotificationsProvider position="top-right">
            <AxiosProvider />
          </NotificationsProvider>
        </ModalsProvider>
      </MantineProvider>
    </ColorSchemeProvider>
  );
}

export default App;
