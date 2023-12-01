package dev.cdy.studio.api.features.slot;

import dev.cdy.studio.api.features.slot.dtos.AvailableSlots;
import dev.cdy.studio.api.features.slot.dtos.SlotInfo;
import dev.cdy.studio.api.features.slot.services.SlotService;
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
    public List<SlotInfo> getAll() {
        return slotService.getAll();
    }

    @GetMapping("/available")
    public List<AvailableSlots.Response> getAvailableSlots(@Valid AvailableSlots.Request request) {
        return slotService.findAvailableSlots(request);
    }
}
