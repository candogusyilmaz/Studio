package dev.canverse.studio.api.features.quote;

import com.google.common.base.Preconditions;
import dev.canverse.studio.api.exceptions.BadRequestException;
import dev.canverse.studio.api.features.authentication.AuthenticationProvider;
import dev.canverse.studio.api.features.quote.dtos.CreateQuote;
import dev.canverse.studio.api.features.quote.dtos.QuoteInfo;
import dev.canverse.studio.api.features.quote.entities.Quote;
import dev.canverse.studio.api.features.quote.entities.QuoteStatus;
import dev.canverse.studio.api.features.quote.repositories.QuoteRepository;
import dev.canverse.studio.api.features.quote.repositories.QuoteSpecifications;
import dev.canverse.studio.api.features.user.entities.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class QuoteService {
    private static final int MAX_NUMBER_OF_SIMULTANEOUS_PENDING_QUOTES = 3;
    private final QuoteRepository quoteRepository;
    private final AuthenticationProvider authenticationProvider;

    public void create(CreateQuote.Request dto) {
        validateSimultaneousPendingQuotes(authenticationProvider.getAuthentication().getId());

        var quote = new Quote();
        quote.setUser(authenticationProvider.getAuthentication());
        quote.setContent(dto.getContent());
        quote.setEnabled(true);

        quoteRepository.save(quote);
    }

    @Cacheable(value = "quoteOfTheDay")
    public QuoteInfo getQuoteOfTheDay() {
        return quoteRepository.findBy(QuoteSpecifications.getQuoteOfTheDay(), r -> r.project("user").first().map(QuoteInfo::new)).orElse(null);
    }

    public Page<QuoteInfo> getQuotes(Pageable pageable, User user) {
        return quoteRepository.findBy(QuoteSpecifications.getQuotesByUserId(user.getId()),
                r -> r.sortBy(pageable.getSort()).page(pageable).map(QuoteInfo::new));
    }

    public void toggleMyQuote(int quoteId) {
        var quote = quoteRepository
                .findBy(QuoteSpecifications.getQuotesByUserId(authenticationProvider.getAuthentication().getId(), quoteId), FluentQuery.FetchableFluentQuery::first)
                .orElseThrow(() -> new BadRequestException("Alıntı bulunamadı."));

        Preconditions.checkArgument(quote.getStatus() != QuoteStatus.ACTIVE, "Bugünün alıntısı değiştirilemez.");

        if (quote.getStatus() == QuoteStatus.PENDING && !quote.isEnabled()) {
            validateSimultaneousPendingQuotes(authenticationProvider.getAuthentication().getId());
        }

        quote.setEnabled(!quote.isEnabled());
        quoteRepository.save(quote);
    }

    @Async
    @Scheduled(cron = "0 0 21 * * MON-FRI")
    @Transactional
    @CacheEvict(value = "quoteOfTheDay", allEntries = true)
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

    private void validateSimultaneousPendingQuotes(int userId) {
        long pendingQuotesCount = quoteRepository
                .findBy(QuoteSpecifications.getQuotesByUserId(userId, QuoteStatus.PENDING, true),
                        FluentQuery.FetchableFluentQuery::count);

        Preconditions.checkArgument(pendingQuotesCount <= MAX_NUMBER_OF_SIMULTANEOUS_PENDING_QUOTES,
                "Aynı anda en fazla %s tane gösterilmek üzere olan alıntınız olabilir.", MAX_NUMBER_OF_SIMULTANEOUS_PENDING_QUOTES);
    }
}
