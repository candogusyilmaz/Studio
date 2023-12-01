package dev.cdy.studio.api.features.quote.repositories;

import dev.cdy.studio.api.features.quote.entities.Quote;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Integer>, JpaSpecificationExecutor<Quote> {

    @Query("UPDATE Quote q SET q.status =dev.cdy.studio.api.features.quote.entities.QuoteStatus.PENDING, q.statusResetDate = null " +
            "WHERE q.status = dev.cdy.studio.api.features.quote.entities.QuoteStatus.SHOWN " +
            "AND q.statusResetDate = :date")
    @Modifying
    @Transactional
    void setShownQuoteStatusToPending(LocalDate date);
}