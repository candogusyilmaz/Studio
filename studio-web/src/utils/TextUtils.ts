export function getInitials(text: string | undefined | null) {
  if (!text) {
    return "S";
  }

  const textSplit = text.toLocaleUpperCase().split(" ");

  if (textSplit.length === 1) {
    return textSplit[0][0];
  }

  return textSplit[0][0] + textSplit[textSplit.length - 1][0];
}
