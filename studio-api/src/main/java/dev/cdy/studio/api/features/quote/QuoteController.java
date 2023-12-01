package dev.cdy.studio.api.features.quote;

import dev.cdy.studio.api.features.authentication.AuthenticationProvider;
import dev.cdy.studio.api.features.quote.dtos.CreateQuote;
import dev.cdy.studio.api.features.quote.dtos.QuoteInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quotes")
public class QuoteController {
    private final QuoteService quoteService;
    private final AuthenticationProvider authenticationProvider;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid CreateQuote.Request dto) {
        quoteService.create(dto);
    }

    @GetMapping("/quoteOfTheDay")
    public ResponseEntity<QuoteInfo> getQuoteOfTheDay() {
        var quoteOfTheDay = quoteService.getQuoteOfTheDay();

        if (quoteOfTheDay == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(quoteOfTheDay);
    }

    @GetMapping
    public Page<QuoteInfo> getMyQuotes(@PageableDefault Pageable pageable) {
        return quoteService.getQuotes(pageable, authenticationProvider.getAuthentication());
    }

    @PatchMapping("/toggle/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableOrDisableUserQuote(@PathVariable int id) {
        quoteService.toggleMyQuote(id);
    }
}
