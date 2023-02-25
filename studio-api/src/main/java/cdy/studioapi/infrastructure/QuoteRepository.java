package cdy.studioapi.infrastructure;

import cdy.studioapi.models.Quote;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface QuoteRepository extends JpaRepository<Quote, Integer>, JpaSpecificationExecutor<Quote> {

    @Query("UPDATE Quote q SET q.status = cdy.studioapi.enums.QuoteStatus.PENDING, q.statusResetDate = null " +
            "WHERE q.status = cdy.studioapi.enums.QuoteStatus.SHOWN " +
            "AND q.statusResetDate = :date")
    @Modifying
    @Transactional
    void setShownQuoteStatusToPending(LocalDate date);
}