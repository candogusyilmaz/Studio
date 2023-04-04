import { Suspense, useContext } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { AuthContext } from "../../context/AuthContext";

export default function RequireAuth({ children, permissions = [] }: { children: JSX.Element; permissions?: string[] }) {
  const { user } = useContext(AuthContext);
  const location = useLocation();

  if (!user) return <Navigate to="/login" state={{ from: location }} replace />;

  if (permissions.length > 0 && user?.permissions.some((s) => permissions.includes(s)) === false) {
    return <Navigate to="/unauthorized" state={{ from: location }} replace />;
  }

  return <Suspense>{children}</Suspense>;
}
