import { ReactElement } from "react";
import { IconCalendarEvent, IconHome, IconList, IconPlus } from "@tabler/icons";
import { Route, Routes } from "react-router-dom";
import { Login } from "./pages/Login";
import { Unauthorized } from "./pages/Unauthorized";
import { NotFound } from "./pages/NotFound";
import StudioShell from "./layouts/StudioShell";
import RequireAuth from "./components/shared/RequireAuth";
import { NewReservation } from "./pages/reservations/NewReservation";
import { ReservationHistory } from "./pages/reservations/ReservationHistory";

interface RouteLink {
  href?: string;
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
    href: "/reservations",
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
    </Route>

    <Route path="/login" element={<Login />} />
    <Route path="/unauthorized" element={<Unauthorized />} />
    <Route path="*" element={<NotFound />} />
  </Routes>
);
