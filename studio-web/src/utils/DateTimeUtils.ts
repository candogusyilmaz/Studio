import dayjs from "dayjs";
import { getPreferredLanguage } from "./LocalStorageUtils";

export function convertNumberToShortTimeString(value: number) {
  if (value < 0 || value > 48) {
    throw new Error("value 0-48 arasında olmalıdır");
  }

  if (value === 48) {
    return "24:00";
  }

  return dayjs(new Date().setHours(0, 30 * value, 0, 0))
    .locale(getPreferredLanguage())
    .format("HH:mm");
}

export function convertNumberToDate(value: number, date: Date, seconds: number) {
  if (value < 0 || value > 48) {
    throw new Error("value 0-48 arasında olmalıdır");
  }

  const result = date.setHours(0, value * 30, seconds, 0);
  return new Date(result);
}

export function convertDatesToString(startDate: Date | string, endDate: Date | string) {
  const start = dayjs(startDate).locale(getPreferredLanguage()).format("MMM D, YYYY HH:mm");
  const end = dayjs(endDate).locale(getPreferredLanguage()).format("HH:mm");

  return `${start} - ${end}`;
}

export function convertDateToLocaleString(date: Date | string) {
  return dayjs(date).locale(getPreferredLanguage()).format("MMM D, YYYY HH:mm");
}
