import api from "./api";
import { QuoteView } from "./types";

const quotesURL = api.defaults.baseURL + "quotes";

export function createQuote(quote: string) {
  const query = new URL(quotesURL);
  return api.post(query.toString(), { content: quote });
}

export function fetchQuoteOfTheDay(signal?: AbortSignal) {
  const query = new URL(quotesURL + "/today");
  return api.get<QuoteView>(query.toString(), { signal });
}
