import { SortingState } from "@tanstack/react-table";
import api from "./api";
import { Page, PermissionView } from "./types";

const locationsURL = api.defaults.baseURL + "permissions";

export function fetchPermissions(page: number, sort: SortingState, signal?: AbortSignal) {
  const query = new URL(locationsURL);

  query.searchParams.set("page", page.toString());

  if (sort.length > 0) {
    query.searchParams.set("sort", `${sort[0].id},${sort[0].desc ? "desc" : "asc"}`);
  }

  return api.get<Page<PermissionView>>(query.toString(), { signal });
}

export function fetchPermissionById(id: number, signal?: AbortSignal) {
  const query = new URL(locationsURL + "/" + id);

  return api.get<PermissionView>(query.toString(), { signal });
}

export function updateDisplayName(id: number, displayName: string) {
  return api.patch(`${locationsURL}/${id}`, { displayName });
}
