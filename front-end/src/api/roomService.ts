import { SortingState } from "@tanstack/react-table";
import api from "./api";
import { Page, RoomView } from "./types";

const roomsURL = api.defaults.baseURL + "rooms";

export function fetchRooms(page: number, sort: SortingState, signal?: AbortSignal) {
    const query = new URL(roomsURL);
  
    query.searchParams.set("page", page.toString());
  
    if (sort.length > 0) {
      query.searchParams.set("sort", `${sort[0].id},${sort[0].desc ? "desc" : "asc"}`);
    }
  
    return api.get<Page<RoomView>>(query.toString(), { signal });
  }

export function createRoom(name: string, capacity: number, locationId: number) {
  const query = new URL(roomsURL);

  const body = {
    name,
    capacity,
    locationId,
  };

  return api.post(query.toString(), body);
}
