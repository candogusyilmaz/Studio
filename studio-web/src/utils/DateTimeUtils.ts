export function convertNumberToShortTimeString(value: number) {
  if (value < 0 || value > 48) {
    throw new Error("value 0-48 arasında olmalıdır");
  }

  if (value === 48) {
    return "24:00";
  }

  return new Date(new Date().setHours(0, 30 * value, 0, 0)).toLocaleTimeString(navigator.language, {
    timeStyle: "short",
  });
}

export function convertNumberToDate(value: number, date: Date, seconds: number) {
  if (value < 0 || value > 48) {
    throw new Error("value 0-48 arasında olmalıdır");
  }

  const result = date.setHours(0, value * 30, seconds, 0);
  return new Date(result);
}

/**
 * Converts a date to a string by using the current or specified locale.
 * dd-
 */
export function convertDatesToString(startDate: Date | string, endDate: Date | string) {
  const dateString = new Date(startDate).toLocaleDateString(navigator.language, {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });

  const startDateTime = new Date(startDate).toLocaleTimeString(navigator.language, {
    hour: "2-digit",
    minute: "2-digit",
    hour12: false
  });

  const endDateTime = new Date(endDate).toLocaleTimeString(navigator.language, {
    hour: "2-digit",
    minute: "2-digit",
    hour12: false
  });

  return `${dateString} ${startDateTime} - ${endDateTime}`;
}
