import api from "./api";

export function fetchSlots() {
  return api.get("/slots").then((s) => s.data);
}

export function fetchAvailableSlots(startDate: Date, endDate: Date) {
  const query = new URL(api.defaults.baseURL + "slots/available");
  query.searchParams.set("startDate", startDate.toJSON().slice(0, -1));
  query.searchParams.set("endDate", endDate.toJSON().slice(0, -1));

  return api.get(query.toString()).then((s) => s.data);
}

export function fetchAvailableSlotsWithRoomId(startDate: Date, endDate: Date, roomId: number) {
  const query = new URL(api.defaults.baseURL + "slots/available");
  query.searchParams.set("startDate", startDate.toJSON().slice(0, -1));
  query.searchParams.set("endDate", endDate.toJSON().slice(0, -1));
  query.searchParams.set("roomId", roomId.toString());

  return api.get(query.toString()).then((s) => s.data);
}