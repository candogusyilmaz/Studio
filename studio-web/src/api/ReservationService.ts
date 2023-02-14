import { SortingState } from "@tanstack/react-table";
import api from "./api";
import { Page, ReservationView } from "./types";

export function createReservation(slotId: number, startDate: Date, endDate: Date) {
  return api.post("reservations", {
    slotId,
    startDate,
    endDate,
  });
}

export function fetchReservationHistory(page: number, sort: SortingState) {
  const query = new URL(api.defaults.baseURL + "reservations/history");

  query.searchParams.set("page", page.toString());

  if (sort.length > 0) {
    query.searchParams.set("sort", `${sort[0].id},${sort[0].desc ? "desc" : "asc"}`);
  }

  return api.get<Page<ReservationView>>(query.toString());
}
