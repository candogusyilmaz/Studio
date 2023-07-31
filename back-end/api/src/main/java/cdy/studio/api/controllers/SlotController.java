package cdy.studio.api.controllers;

import cdy.studio.service.requests.queries.SlotCriteria;
import cdy.studio.service.SlotService;
import cdy.studio.service.views.SlotView;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/slots")
public class SlotController {
    private final SlotService slotService;

    @GetMapping
    public List<SlotView> getAll() {
        return slotService.getAll();
    }

    @GetMapping("/available")
    public List<SlotView> getAvailableSlots(@Valid SlotCriteria criteria) {
        return slotService.findAvailableSlots(criteria);
    }
}
