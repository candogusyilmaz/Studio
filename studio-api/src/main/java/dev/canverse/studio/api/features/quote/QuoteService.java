package dev.canverse.studio.api.features.quote;

import com.google.common.base.Preconditions;
import dev.canverse.expectation.Expect;
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
    private static final long MAX_NUMBER_OF_SIMULTANEOUS_PENDING_QUOTES = 3;
    private final QuoteRepository quoteRepository;

    /**
     * Creates a new quote based on the provided request.
     *
     * @param dto The request containing the details of the quote to be created.
     * @throws IllegalStateException Thrown if there are simultaneous pending quotes for the authenticated user.
     */
    public void create(CreateQuote.Request dto) {
        validateSimultaneousPendingQuotes(AuthenticationProvider.getAuthentication().getId());

        var quote = new Quote();
        quote.setUser(AuthenticationProvider.getAuthentication());
        quote.setContent(dto.getContent());
        quote.setEnabled(true);

        quoteRepository.save(quote);
    }

    /**
     * Retrieves the quote of the day, cached for performance.
     *
     * @return A QuoteInfo object representing the quote of the day, or null if not found.
     */
    @Cacheable(value = "quoteOfTheDay")
    public QuoteInfo getQuoteOfTheDay() {
        return quoteRepository.findBy(QuoteSpecifications.getQuoteOfTheDay(), r -> r.project("user").first().map(QuoteInfo::new)).orElse(null);
    }

    /**
     * Retrieves a paginated list of quotes for the specified user.
     *
     * @param pageable The pageable information specifying the page size, page number, and sorting.
     * @param user     The user for whom to retrieve the quotes.
     * @return A Page containing QuoteInfo objects representing the paginated quotes.
     */
    public Page<QuoteInfo> getQuotes(Pageable pageable, User user) {
        return quoteRepository.findBy(QuoteSpecifications.getQuotesByUserId(user.getId()),
                r -> r.sortBy(pageable.getSort()).page(pageable).map(QuoteInfo::new));
    }

    /**
     * Toggles the enabled status of the authenticated user's quote based on the provided quote ID.
     *
     * @param quoteId The ID of the quote to be toggled.
     * @throws IllegalArgumentException Thrown if the quote is not found or if attempting to disable today's active quote.
     * @throws IllegalStateException    Thrown if there are simultaneous pending quotes for the authenticated user when enabling a pending quote.
     */
    public void toggleMyQuote(int quoteId) {
        var quote = quoteRepository
                .findBy(QuoteSpecifications.getQuotesByUserId(AuthenticationProvider.getAuthentication().getId(), quoteId), FluentQuery.FetchableFluentQuery::first)
                .orElseThrow(() -> new IllegalArgumentException("Quote not found."));

        Preconditions.checkArgument(quote.getStatus() != QuoteStatus.ACTIVE, "Today's quote cannot be disabled.");

        if (quote.getStatus() == QuoteStatus.PENDING && !quote.isEnabled()) {
            validateSimultaneousPendingQuotes(AuthenticationProvider.getAuthentication().getId());
        }

        quote.setEnabled(!quote.isEnabled());
        quoteRepository.save(quote);
    }

    /**
     * Changes the quote of the day based on a scheduled task.
     * This method runs daily at 21:00 from Monday to Friday.
     * It sets the active quote to "shown" and randomly selects a pending quote to become the new active quote.
     */
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

    /**
     * Resets the "shown" status of quotes based on a scheduled task.
     * This method runs daily at 20:00.
     */
    @Async
    @Scheduled(cron = "0 0 20 * * *")
    @Transactional
    public void resetShownQuotesStatus() {
        quoteRepository.setShownQuoteStatusToPending(LocalDate.now());
    }

    /**
     * Validates the number of simultaneous pending quotes for the authenticated user.
     *
     * @param userId The ID of the authenticated user.
     * @throws IllegalArgumentException Thrown if the user exceeds the maximum allowed number of simultaneous pending quotes.
     */
    private void validateSimultaneousPendingQuotes(int userId) {
        long pendingQuotesCount = quoteRepository
                .findBy(QuoteSpecifications.getQuotesByUserId(userId, QuoteStatus.PENDING, true),
                        FluentQuery.FetchableFluentQuery::count);

        Expect.of(pendingQuotesCount).lessThanOrEqualTo(MAX_NUMBER_OF_SIMULTANEOUS_PENDING_QUOTES,
                "You can have at most %s pending quotes at the same time.", MAX_NUMBER_OF_SIMULTANEOUS_PENDING_QUOTES);
    }
}
