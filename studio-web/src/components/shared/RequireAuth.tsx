import dayjs from "dayjs";
import { useContext } from "react";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import { AuthContext } from "../../context/AuthContext";
import { getUserFromLocalStorage } from "../../helper/helper";

export default function RequireAuth({ allowedRoles = [] }: { allowedRoles?: string[] }) {
  const { user, refresh } = useContext(AuthContext);
  const location = useLocation();

  let juser = user === null ? getUserFromLocalStorage() : user;
  
  if (juser !== null) {
    if (dayjs(juser?.expiresAt).isBefore(new Date())) {
      refresh();
      juser = getUserFromLocalStorage();
    }

    if (allowedRoles.length > 0 && juser?.permissions.some((s) => allowedRoles?.includes(s)) === false) {
      return <Navigate to="/unauthorized" state={{ from: location }} replace />;
    }
    return <Outlet />;
  } else {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }
}
