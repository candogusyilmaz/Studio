import api from "./api";
import { SlotView } from "./types";

export async function fetchAvailableSlots(startDate: Date, endDate: Date, roomId?: number, signal?: AbortSignal) {
  const query = new URL(api.defaults.baseURL + "slots/available");
  query.searchParams.set("startDate", startDate.toJSON().slice(0, -1));
  query.searchParams.set("endDate", endDate.toJSON().slice(0, -1));

  if (roomId) {
    query.searchParams.set("roomId", roomId.toString());
  }

  return api.get<SlotView[]>(query.toString(), { signal });
}
