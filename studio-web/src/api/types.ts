export interface ApiErrorResponse {
  message?: string;
}

export interface Page<T> {
  content: T[];
  pageable: {
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    pageSize: number;
    pageNumber: number;
    paged: boolean;
    unpaged: boolean;
  };
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

export interface UserBasicView {
  id: number;
  username: string;
  displayName: string;
  email: string;
}

export interface ReservationView {
  id: number;
  startDate: Date;
  endDate: Date;
  user?: UserBasicView;
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
  actionBy?: UserBasicView;
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
  parent?: LocationParentlessView;
  rooms?: RoomView[];
}

export interface LocationParentlessView {
  id: number;
  name: string;
}

export interface QuoteView {
  id: number;
  user?: UserBasicView;
  content: string;
  status: QuoteStatus;
  lastShownDate: Date;
  statusResetDate: Date;
  shownTimes: number;
  enabled: boolean;
}

export const ReservationStatusList = {
  ACTIVE: "ACTIVE",
  CANCELLED: "CANCELLED",
  CONFIRMED: "CONFIRMED",
  PENDING: "PENDING",
  REJECTED: "REJECTED",
  UPDATED: "UPDATED",
};

export const QuoteStatusList = {
  ACTIVE: "ACTIVE",
  PENDING: "PENDING",
  SHOWN: "SHOWN",
};

export type ReservationStatus = keyof typeof ReservationStatusList;
export type QuoteStatus = keyof typeof QuoteStatusList;

export function isStatusCancellable(status: ReservationStatus) {
  const cancellableStatuses = [
    ReservationStatusList.PENDING,
    ReservationStatusList.UPDATED,
    ReservationStatusList.ACTIVE,
    ReservationStatusList.CONFIRMED,
    ReservationStatusList.PENDING,
  ];

  return cancellableStatuses.includes(status);
}

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
