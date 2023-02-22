import { SortingState } from "@tanstack/react-table";
import api from "./api";
import { Page, ReservationView } from "./types";

const reservationsURL = api.defaults.baseURL + "reservations";

export function createReservation(slotId: number, startDate: Date, endDate: Date) {
  const query = new URL(reservationsURL);
  return api.post(query.toString(), {
    slotId,
    startDate,
    endDate,
  });
}

export function fetchReservationHistory(page: number, sort: SortingState) {
  const query = new URL(reservationsURL + "/history");

  query.searchParams.set("page", page.toString());

  if (sort.length > 0) {
    query.searchParams.set("sort", `${sort[0].id},${sort[0].desc ? "desc" : "asc"}`);
  }

  return api.get<Page<ReservationView>>(query.toString());
}

export function cancelReservation(reservationId: number) {
  const query = new URL(reservationsURL + "/cancel/" + reservationId);
  return api.patch(query.toString());
}
