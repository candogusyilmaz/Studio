import api from "./api";
import { ReservationView } from "./types";

export async function createReservation(slotId: number, startDate: Date, endDate: Date) {
  return await api.post("reservations", {
    slotId,
    startDate,
    endDate,
  });
}

export async function fetchReservationHistory() {
  return await api.get<ReservationView[]>("reservations/history");
}
