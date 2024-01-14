package dev.canverse.studio.api.features.quote;

import dev.canverse.studio.api.features.authentication.AuthenticationProvider;
import dev.canverse.studio.api.features.quote.dtos.CreateQuote;
import dev.canverse.studio.api.features.quote.dtos.QuoteInfo;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createQuote(@RequestBody @Valid CreateQuote.Request dto) {
        quoteService.create(dto);
    }

    @GetMapping("/quoteOfTheDay")
    public ResponseEntity<QuoteInfo> getQuoteOfTheDay() {
        var quoteOfTheDay = quoteService.getQuoteOfTheDay();

        if (quoteOfTheDay == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(quoteOfTheDay);
    }

    @GetMapping
    public Page<QuoteInfo> getUserQuotes(@PageableDefault Pageable pageable) {
        return quoteService.getQuotes(pageable, AuthenticationProvider.getAuthentication());
    }

    @PatchMapping("/toggle/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleQuote(@PathVariable int id) {
        quoteService.toggleMyQuote(id);
    }
}
