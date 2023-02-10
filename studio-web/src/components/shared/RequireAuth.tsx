import dayjs from "dayjs";
import { useContext } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { AuthContext } from "../../context/AuthContext";

export default function RequireAuth({ children, permissions = [] }: { children: JSX.Element; permissions?: string[] }) {
  const { getUser, refresh } = useContext(AuthContext);
  const location = useLocation();
  const user = getUser();

  if (user) {
    if (dayjs(user.expiresAt).isBefore(new Date())) {
      refresh();
    }

    if (permissions.length > 0 && user?.permissions.some((s) => permissions.includes(s)) === false) {
      return <Navigate to="/unauthorized" state={{ from: location }} replace />;
    }
    return children;
  } else {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }
}