import { IconCalendarEvent, IconHistory, IconHome, IconHomeCog, IconPlus, IconServerCog, IconShieldCog } from "@tabler/icons-react";
import React, { ReactElement } from "react";
import { Route, Routes } from "react-router-dom";
import RequireAuth from "./components/shared/RequireAuth";
import StudioShell from "./layouts/StudioShell";
import { Login } from "./pages/Login";
import { NotFound } from "./pages/NotFound";
import { Unauthorized } from "./pages/Unauthorized";
import { PERMISSIONS } from "./authorities";

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
      {
        href: "/management/permissions",
        label: "Yetki Tanımlamaları",
        icon: <IconShieldCog size={16} />,
        permission: PERMISSIONS.LIST_PERMISSIONS,
      },
    ],
  },
];

const Dashboard = React.lazy(() => import("./pages/dashboard/Dashboard"));
const MyQuotes = React.lazy(() => import("./pages/profile/MyQuotes"));
const NewReservation = React.lazy(() => import("./pages/reservations/NewReservation"));
const ReservationHistory = React.lazy(() => import("./pages/reservations/ReservationHistory"));
const ReservationManagement = React.lazy(() => import("./pages/management/reservations/ReservationManagement"));
const PermissionManagement = React.lazy(() => import("./pages/management/permissions/PermissionManagement"));

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
      <Route
        path="/management/permissions"
        element={
          <RequireAuth permissions={[PERMISSIONS.LIST_PERMISSIONS]}>
            <PermissionManagement />
          </RequireAuth>
        }
      />
    </Route>

    <Route path="/login" element={<Login />} />
    <Route path="/unauthorized" element={<Unauthorized />} />
    <Route path="*" element={<NotFound />} />
  </Routes>
);
