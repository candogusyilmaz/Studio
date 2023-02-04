export interface SimpleUserView {
  id: number;
  username: string;
  displayName: string;
  email: string;
}

export interface ReservationView {
  id: number;
  startDate: Date;
  endDate: Date;
  user?: SimpleUserView;
  slot?: SlotView;
  lastAction?: ReservationActionView;
}

export interface SlotView {
  id: number;
  name: string;
  room?: RoomView;
  items?: ItemView[];
}

export interface ItemView {
  id: number;
  name: string;
}

export interface ReservationActionView {
  id: number;
  description: string;
  status: ReservationStatus;
  actionDate: Date;
  actionBy?: SimpleUserView;
}

export interface RoomView {
  id: number;
  name: string;
  capacity: number;
  location?: LocationView;
  slots?: SlotView[];
}

export interface LocationView {
  id: number;
  name: string;
  parent?: LocationParentView;
  rooms?: RoomView[];
}

export interface LocationParentView {
  id: number;
  name: string;
}

export const ReservationStatusList = {
  ACTIVE: "ACTIVE",
  CANCELLED: "CANCELLED",
  CONFIRMED: "CONFIRMED",
  PENDING: "PENDING",
  REJECTED: "REJECTED",
  UPDATED: "UPDATED",
};

export type ReservationStatus = keyof typeof ReservationStatusList;

export function getReservationStatus(status?: ReservationStatus) {
  switch (status) {
    case "ACTIVE":
      return "Aktif";
    case "CANCELLED":
      return "İptal";
    case "CONFIRMED":
      return "Onaylandı";
    case "PENDING":
      return "Bekliyor";
    case "REJECTED":
      return "Reddedildi";
    case "UPDATED":
      return "Güncellendi";
    default:
      return "Bilinmiyor";
  }
}

export function getReservationStatusColor(status?: ReservationStatus) {
  switch (status) {
    case "ACTIVE":
      return "green";
    case "CANCELLED":
      return "red";
    case "CONFIRMED":
      return "green";
    case "PENDING":
      return "orange";
    case "REJECTED":
      return "red";
    case "UPDATED":
      return "blue";
    default:
      return "gray";
  }
}
