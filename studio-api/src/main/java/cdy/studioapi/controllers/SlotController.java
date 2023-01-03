package cdy.studioapi.controllers;

import cdy.studioapi.dtos.queries.SlotCriteria;
import cdy.studioapi.services.SlotService;
import cdy.studioapi.views.SlotView;
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

    @GetMapping({"", "/"})
    public List<SlotView> getAll() {
        return slotService.getAll();
    }

    @GetMapping("/available")
    public List<SlotView> getAvailableSlots(SlotCriteria criteria) {
        return slotService.findAvailableSlots(criteria);
    }
}
