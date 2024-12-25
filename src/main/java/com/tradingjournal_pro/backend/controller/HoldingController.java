package com.tradingjournal_pro.backend.controller;

import com.tradingjournal_pro.backend.dto.HoldingRequestBody;
import com.tradingjournal_pro.backend.dto.ResponseBody;
import com.tradingjournal_pro.backend.service.HoldingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/holdings")
public class HoldingController {
    private final HoldingService holdingService;

    public HoldingController(HoldingService holdingService) {
        this.holdingService = holdingService;
    }

    @PostMapping("/{userId}/add-holding")
    public ResponseBody addHolding(@Valid @RequestBody HoldingRequestBody holdingRequestBody,
                                   @PathVariable String userId) {
        return holdingService.addHolding(holdingRequestBody, userId);
    }

    @GetMapping("/{userId}/{holdingId}")
    public ResponseBody getHolding(@PathVariable String holdingId, @PathVariable String userId) {
        return holdingService.getHolding(holdingId, userId);
    }

    @GetMapping("/{userId}")
    public ResponseBody getAllHoldings(@PathVariable String userId) {
        return holdingService.getAllHoldings(userId);
    }

    @PutMapping("/{userId}/{holdingId}")
    public ResponseBody updateHolding(@Valid @RequestBody HoldingRequestBody holdingRequestBody,
                                      @PathVariable String userId,
                                      @PathVariable String holdingId) {
        return holdingService.updateHolding(holdingRequestBody, userId, holdingId);
    }

    @DeleteMapping("/{userId}/{holdingId}")
    public ResponseBody squareOffHolding(@PathVariable String userId, @PathVariable String holdingId) {
        return holdingService.squareOffHolding(userId, holdingId);
    }
}
