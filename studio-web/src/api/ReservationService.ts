import api from "./api";

export async function createReservation(slotId: number, startDate: Date, endDate: Date) {
    return await api.post("reservations", {
        slotId,
        startDate,
        endDate
    });
}