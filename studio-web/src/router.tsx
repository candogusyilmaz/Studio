import { ReactElement } from "react";
import { IconCalendarEvent, IconHome, IconHomeCog, IconList, IconPlus, IconServerCog } from "@tabler/icons";
import { Route, Routes } from "react-router-dom";
import { Login } from "./pages/Login";
import { Unauthorized } from "./pages/Unauthorized";
import { NotFound } from "./pages/NotFound";
import StudioShell from "./layouts/StudioShell";
import RequireAuth from "./components/shared/RequireAuth";
import { NewReservation } from "./pages/reservations/NewReservation";
import { ReservationHistory } from "./pages/reservations/ReservationHistory";
import ReservationManagement from "./pages/management/reservations/ReservationManagement";

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
        icon: <IconList size={16} />,
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
            <div>This is dashboard</div>
          </RequireAuth>
        }
      />
      <Route
        path="/reservations/new"
        element={
          <RequireAuth permissions={["per 1"]}>
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
