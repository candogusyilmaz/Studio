package cdy.studioapi.services;

import cdy.studioapi.enums.QuoteStatus;
import cdy.studioapi.exceptions.BadRequestException;
import cdy.studioapi.infrastructure.QuoteRepository;
import cdy.studioapi.infrastructure.specs.QuoteSpecifications;
import cdy.studioapi.models.Quote;
import cdy.studioapi.requests.QuoteCreateRequest;
import cdy.studioapi.views.QuoteView;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class QuoteService {
    private static final int MAX_NUMBER_OF_SIMULTANEOUS_PENDING_QUOTES = 3;
    private final QuoteRepository quoteRepository;
    private final AuthenticationProvider authenticationProvider;

    public void create(QuoteCreateRequest dto) {
        var pendingQuotesCount = quoteRepository.findBy(QuoteSpecifications.getQuotesByUserId(authenticationProvider.getAuthentication().getId(), QuoteStatus.PENDING, true), FluentQuery.FetchableFluentQuery::count);

        if (pendingQuotesCount >= MAX_NUMBER_OF_SIMULTANEOUS_PENDING_QUOTES) {
            throw new BadRequestException("Aynı anda en fazla " + MAX_NUMBER_OF_SIMULTANEOUS_PENDING_QUOTES + " tane gösterilmek üzere olan alıntınız olabilir.");
        }

        var quote = new Quote();
        quote.setUser(authenticationProvider.getAuthentication());
        quote.setContent(dto.getContent());
        quote.setEnabled(true);

        quoteRepository.save(quote);
    }

    public QuoteView getQuoteOfTheDay() {
        return quoteRepository.findBy(QuoteSpecifications.getQuoteOfTheDay(), r -> r.project("user").first().map(QuoteView::new)).orElse(null);
    }

    public Page<QuoteView> getMyQuotes(Pageable pageable) {
        return quoteRepository.findBy(QuoteSpecifications.getQuotesByUserId(authenticationProvider.getAuthentication().getId()),
                r -> r.sortBy(pageable.getSort()).page(pageable).map(QuoteView::new));
    }

    public void enableOrDisableMyQuote(int quoteId) {
        var quote = quoteRepository
                .findBy(QuoteSpecifications.getQuotesByUserId(authenticationProvider.getAuthentication().getId(), quoteId), FluentQuery.FetchableFluentQuery::first)
                .orElseThrow(() -> new BadRequestException("Alıntı bulunamadı."));

        if (quote.getStatus() == QuoteStatus.ACTIVE) {
            throw new BadRequestException("Bugünün alıntısı değiştirilemez.");
        }

        if (quote.getStatus() == QuoteStatus.PENDING && !quote.isEnabled()) {
            var pendingQuotesCount = quoteRepository
                    .findBy(QuoteSpecifications.getQuotesByUserId(authenticationProvider.getAuthentication().getId(), QuoteStatus.PENDING, true),
                            FluentQuery.FetchableFluentQuery::count);

            if (pendingQuotesCount >= MAX_NUMBER_OF_SIMULTANEOUS_PENDING_QUOTES) {
                throw new BadRequestException("Aynı anda en fazla " + MAX_NUMBER_OF_SIMULTANEOUS_PENDING_QUOTES + " tane gösterilmek üzere olan alıntınız olabilir.");
            }
        }

        quote.setEnabled(!quote.isEnabled());
        quoteRepository.save(quote);
    }

    @Async
    @Scheduled(cron = "0 0 21 * * MON-FRI")
    @Transactional
    public void changeQuoteOfTheDay() {
        // get the active quote and set it to shown then save it to the database
        quoteRepository
                .findBy(QuoteSpecifications.getQuotes(QuoteStatus.ACTIVE, true), FluentQuery.FetchableFluentQuery::first)
                .ifPresent(quote -> {
                    quote.setShown();
                    quoteRepository.save(quote);
                });

        // get all pending quotes
        var quotes = quoteRepository.findBy(QuoteSpecifications.getQuotes(QuoteStatus.PENDING, true), FluentQuery.FetchableFluentQuery::all);

        if (quotes.isEmpty()) return;

        var randomQuote = quotes.get(ThreadLocalRandom.current().nextInt(0, quotes.size()));
        randomQuote.setActive();

        quoteRepository.save(randomQuote);
    }

    @Async
    @Scheduled(cron = "0 0 20 * * *")
    @Transactional
    public void resetShownQuotesStatus() {
        quoteRepository.setShownQuoteStatusToPending(LocalDate.now());
    }
}
