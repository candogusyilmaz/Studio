import { SortingState } from "@tanstack/react-table";
import api from "./api";
import { LocationView, Page } from "./types";

const locationsURL = api.defaults.baseURL + "locations";

export function fetchLocations(page: number, sort: SortingState, signal?: AbortSignal) {
  const query = new URL(locationsURL);

  query.searchParams.set("page", page.toString());

  if (sort.length > 0) {
    query.searchParams.set("sort", `${sort[0].id},${sort[0].desc ? "desc" : "asc"}`);
  }

  return api.get<Page<LocationView>>(query.toString(), { signal });
}

export function fetchLocationsByName(name: string, signal?: AbortSignal) {
  const query = new URL(locationsURL);

  query.searchParams.set("name", name);

  return api.get<Page<LocationView>>(query.toString(), { signal });
}

export function fetchLocationsAll(signal?: AbortSignal) {
  const query = new URL(locationsURL + "/all");
  return api.get<LocationView[]>(query.toString(), { signal });
}

export function createLocation(name: string, parentId?: number) {
  const query = new URL(locationsURL);

  const body: any = {
    name: name,
  };

  if (parentId) {
    body.parentId = parentId;
  }

  return api.post(query.toString(), body);
}
