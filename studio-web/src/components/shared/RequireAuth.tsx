import dayjs from "dayjs";
import { useContext } from "react";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import { AuthContext } from "../../context/AuthContext";
import { getUserFromLocalStorage } from "../../helper/helper";

export default function RequireAuth({ allowedRoles = [] }: { allowedRoles?: string[] }) {
  const { getUser, logout } = useContext(AuthContext);
  const location = useLocation();

  /* if (user) {
    if (dayjs(user.expiresAt).isBefore(new Date())){
      refresh();
    }
    
    if (allowedRoles.length > 0 && user.permissions.some((s) => allowedRoles?.includes(s)) === false) {
      return <Navigate to="/unauthorized" state={{ from: location }} replace />;
    }
    return <Outlet />;
    
  } else {
    return <Navigate to="/login" state={{ from: location }} replace />;
  } */

  let juser = getUser();

  if (juser) {
    if (dayjs(juser.expiresAt).isBefore(new Date())) {
      logout();
    }

    if (allowedRoles.length > 0 && juser?.permissions.some((s) => allowedRoles?.includes(s)) === false) {
      return <Navigate to="/unauthorized" state={{ from: location }} replace />;
    }
    return <Outlet />;
  } else {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }
}
