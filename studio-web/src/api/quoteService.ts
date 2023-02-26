import { SortingState } from "@tanstack/react-table";
import api from "./api";
import { Page, QuoteView } from "./types";

const quotesURL = api.defaults.baseURL + "quotes";

export function createQuote(quote: string) {
  const query = new URL(quotesURL);
  return api.post(query.toString(), { content: quote });
}

export function fetchQuoteOfTheDay(signal?: AbortSignal) {
  const query = new URL(quotesURL + "/today");
  return api.get<QuoteView>(query.toString(), { signal });
}

export function fetchMyQuotes(page: number, sort: SortingState, signal?: AbortSignal) {
  const query = new URL(quotesURL);
  query.searchParams.set("page", page.toString());

  if (sort.length > 0) {
    query.searchParams.set("sort", `${sort[0].id},${sort[0].desc ? "desc" : "asc"}`);
  }
  return api.get<Page<QuoteView>>(query.toString(), { signal });
}

export function toggleQuoteStatus(quoteId: number) {
  const query = new URL(quotesURL + "/toggle/" + quoteId);
  return api.patch(query.toString());
}
