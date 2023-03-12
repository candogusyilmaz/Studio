package cdy.studioapi.controllers;

import cdy.studioapi.requests.QuoteCreateRequest;
import cdy.studioapi.services.AuthenticationProvider;
import cdy.studioapi.services.QuoteService;
import cdy.studioapi.views.QuoteView;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/quotes")
public class QuoteController {
    private final QuoteService quoteService;
    private final AuthenticationProvider authenticationProvider;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid QuoteCreateRequest dto) {
        quoteService.create(dto);
    }

    @GetMapping("/quoteOfTheDay")
    public ResponseEntity<QuoteView> getQuoteOfTheDay() {
        var quoteOfTheDay = quoteService.getQuoteOfTheDay();

        if (quoteOfTheDay == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(quoteOfTheDay);
    }

    @GetMapping
    public Page<QuoteView> getMyQuotes(@PageableDefault Pageable pageable) {
        return quoteService.getQuotes(pageable, authenticationProvider.getAuthentication());
    }

    @PatchMapping("/toggle/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableOrDisableUserQuote(@PathVariable int id) {
        quoteService.enableOrDisableMyQuote(id);
    }
}
