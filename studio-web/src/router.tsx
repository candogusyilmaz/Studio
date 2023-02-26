import { IconCalendarEvent, IconHistory, IconHome, IconHomeCog, IconPlus, IconServerCog } from "@tabler/icons";
import { ReactElement } from "react";
import { Route, Routes } from "react-router-dom";
import RequireAuth from "./components/shared/RequireAuth";
import StudioShell from "./layouts/StudioShell";
import Dashboard from "./pages/dashboard/Dashboard";
import { Login } from "./pages/Login";
import ReservationManagement from "./pages/management/reservations/ReservationManagement";
import { NotFound } from "./pages/NotFound";
import MyQuotes from "./pages/profile/MyQuotes";
import { NewReservation } from "./pages/reservations/NewReservation";
import { ReservationHistory } from "./pages/reservations/ReservationHistory";
import { Unauthorized } from "./pages/Unauthorized";

interface RouteLink {
  href: string;
  label: string;
  permission?: string;
  icon?: ReactElement;
  links?: RouteLink[];
}

export const headerRoutes: RouteLink[] = [
  {
    href: "/",
    label: "Anasayfa",
    icon: <IconHome size={16} />,
  },
  {
    href: "/reservations/", // Necessary in order to use it as a key prop
    label: "Rezervasyon",
    icon: <IconCalendarEvent size={16} />,
    links: [
      {
        href: "/reservations/new",
        label: "Yeni",
        icon: <IconPlus size={16} />,
      },
      {
        href: "/reservations/history",
        label: "Geçmiş",
        icon: <IconHistory size={16} />,
      },
    ],
  },
  {
    href: "/management/",
    label: "Yönetim",
    icon: <IconServerCog size={16} />,
    links: [
      {
        href: "/management/reservations",
        label: "Rezervasyon Tanımlamaları",
        icon: <IconHomeCog size={16} />,
      },
    ],
  },
];

export const StudioRoutes = () => (
  <Routes>
    <Route element={<StudioShell />}>
      <Route
        path="/"
        element={
          <RequireAuth>
            <Dashboard />
          </RequireAuth>
        }
      />
      <Route
        path="/profile/quotes"
        element={
          <RequireAuth>
            <MyQuotes />
          </RequireAuth>
        }
      />
      <Route
        path="/reservations/new"
        element={
          <RequireAuth>
            <NewReservation />
          </RequireAuth>
        }
      />
      <Route
        path="/reservations/history"
        element={
          <RequireAuth>
            <ReservationHistory />
          </RequireAuth>
        }
      />
      <Route
        path="/management/reservations"
        element={
          <RequireAuth>
            <ReservationManagement />
          </RequireAuth>
        }
      />
    </Route>

    <Route path="/login" element={<Login />} />
    <Route path="/unauthorized" element={<Unauthorized />} />
    <Route path="*" element={<NotFound />} />
  </Routes>
);
