package cdy.studioapi.infrastructure.specs;

import cdy.studioapi.enums.QuoteStatus;
import cdy.studioapi.models.Quote;
import org.springframework.data.jpa.domain.Specification;

public class QuoteSpecifications {
    private QuoteSpecifications() {
    }

    public static Specification<Quote> getQuotesByUserId(int userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Quote> getQuotesByUserId(int userId, int quoteId) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("id"), quoteId),
                cb.equal(root.get("user").get("id"), userId)
        );
    }

    public static Specification<Quote> getQuotesByUserId(int userId, QuoteStatus status, boolean enabled) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("status"), status),
                cb.equal(root.get("user").get("id"), userId),
                cb.equal(root.get("enabled"), enabled)
        );
    }

    public static Specification<Quote> getQuotes(QuoteStatus status, boolean enabled) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("status"), status),
                cb.equal(root.get("enabled"), enabled)
        );
    }

    public static Specification<Quote> getQuoteOfTheDay() {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("status"), QuoteStatus.ACTIVE)
        );
    }
}
