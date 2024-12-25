package com.tradingjournal_pro.backend.controller;

import com.tradingjournal_pro.backend.dto.ResponseBody;
import com.tradingjournal_pro.backend.dto.TradeRequestBody;
import com.tradingjournal_pro.backend.service.TradeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/trades")
public class TradeController {
    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping("/{userId}/add-trade")
    public ResponseBody addTrade(@Valid @RequestBody TradeRequestBody tradeRequestBody,
                                 @PathVariable String userId) {
        return tradeService.addTrade(tradeRequestBody, userId);
    }

    @GetMapping("/{userId}/all-trades")
    public ResponseBody getAllTrades(@PathVariable String userId) {
        return tradeService.getAllTrades(userId);
    }

    @GetMapping("/{userId}/{tradeId}")
    public ResponseBody getTrade(@PathVariable String tradeId,
                                 @PathVariable String userId) {
        return  tradeService.getTrade(tradeId, userId);
    }

    @PutMapping("/{userId}/{tradeId}")
    public ResponseBody updateTrade(@Valid @RequestBody TradeRequestBody tradeRequestBody,
                                    @PathVariable String userId,
                                    @PathVariable String tradeId){
        return tradeService.updateTrade(tradeRequestBody, userId, tradeId);
    }

    @DeleteMapping("/{userId}/{tradeId}")
    public ResponseBody deleteTrade(@PathVariable String userId,
                                    @PathVariable String tradeId){
        return  tradeService.squareOffTrade(userId, tradeId);
    }

}
