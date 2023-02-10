import api from "./api";
import { Page, ReservationView } from "./types";

export function createReservation(slotId: number, startDate: Date, endDate: Date) {
  return api.post("reservations", {
    slotId,
    startDate,
    endDate,
  });
}

export function fetchReservationHistory(page: number) {
  const query = new URL(api.defaults.baseURL + "reservations/history");

  query.searchParams.set("page", page.toString());

  return api.get<Page<ReservationView>>(query.toString());
}
