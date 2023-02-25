package cdy.studioapi.controllers;

import cdy.studioapi.dtos.QuoteCreateDto;
import cdy.studioapi.services.QuoteService;
import cdy.studioapi.views.QuoteView;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/quotes")
public class QuoteController {
    private final QuoteService quoteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid QuoteCreateDto dto) {
        quoteService.create(dto);
    }

    @GetMapping("/today")
    public QuoteView getQuoteOfTheDay() {
        return quoteService.getQuoteOfTheDay();
    }
}
