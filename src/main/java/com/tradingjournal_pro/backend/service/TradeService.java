package com.tradingjournal_pro.backend.service;

import com.tradingjournal_pro.backend.dto.HoldingRequestBody;
import com.tradingjournal_pro.backend.dto.ResponseBody;
import com.tradingjournal_pro.backend.dto.TradeRequestBody;
import com.tradingjournal_pro.backend.exceptions.HoldingNotFoundException;
import com.tradingjournal_pro.backend.exceptions.TradeNotFoundException;
import com.tradingjournal_pro.backend.exceptions.UserNotFoundException;
import com.tradingjournal_pro.backend.models.Holding;
import com.tradingjournal_pro.backend.models.Journal;
import com.tradingjournal_pro.backend.models.Trade;
import com.tradingjournal_pro.backend.models.User;
import com.tradingjournal_pro.backend.repository.JournalRepository;
import com.tradingjournal_pro.backend.repository.TradeRepository;
import com.tradingjournal_pro.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TradeService {
    private static final Logger logger = LoggerFactory.getLogger(TradeService.class);
    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final JournalRepository journalRepository;

    public TradeService(TradeRepository tradeRepository, UserRepository userRepository, JournalRepository journalRepository) {
        this.tradeRepository = tradeRepository;
        this.userRepository = userRepository;
        this.journalRepository = journalRepository;
    }

    private User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + userId));
    }

    public Trade createTrade(TradeRequestBody tradeRequestBody, User currUser) {
        Double totalTradedVal = tradeRequestBody.getEnterPrice() * tradeRequestBody.getQuantity();
        Double currTradedVal = tradeRequestBody.getExitPrice() * tradeRequestBody.getQuantity();

        return new Trade(
                tradeRequestBody.getAssetName(),
                tradeRequestBody.getTradeType(),
                tradeRequestBody.getQuantity(),
                tradeRequestBody.getAssetType(),
                totalTradedVal,
                tradeRequestBody.getTradeCategory(),
                currTradedVal-totalTradedVal,
                tradeRequestBody.getEnterPrice(),
                tradeRequestBody.getStopLoss(),
                tradeRequestBody.getExitPrice(),
                tradeRequestBody.getStrategyName(),
                tradeRequestBody.getStrategyDescription(),
                tradeRequestBody.getDate(),
                currUser
        );
    }

    public Journal createJournal(TradeRequestBody tradeRequestBody, User currUser){
        Double totalTradedVal = tradeRequestBody.getEnterPrice() * tradeRequestBody.getQuantity();
        Double currTradedVal = tradeRequestBody.getExitPrice() * tradeRequestBody.getQuantity();

        return new Journal(
                tradeRequestBody.getAssetName(),
                "Trade",
                tradeRequestBody.getAssetType(),
                tradeRequestBody.getQuantity(),
                tradeRequestBody.getEnterPrice(),
                tradeRequestBody.getStopLoss(),
                tradeRequestBody.getExitPrice(),
                totalTradedVal,
                tradeRequestBody.getTradeCategory(),
                currTradedVal - totalTradedVal,
                tradeRequestBody.getDate(),
                tradeRequestBody.getStrategyName(),
                tradeRequestBody.getStrategyDescription(),
                currUser
        );

    }

    @Transactional
    public ResponseBody addTrade(TradeRequestBody tradeRequestBody, String userId) {
        User currUser = findUserById(userId);

        if (tradeRequestBody.getTradeType().equals("Day Trade")) {
            Journal journal = createJournal(tradeRequestBody, currUser);

            journalRepository.save(journal);
            currUser.getJournal().add(journal);
            userRepository.save(currUser);

            return new ResponseBody(
                    true,
                    "Journal created successfully",
                    journal);
        }

        Trade trade = createTrade(tradeRequestBody, currUser);

        if (tradeRequestBody.getTradeCategory().equals("sell")) {
            trade.setProfitOrLoss(Math.abs(trade.getProfitOrLoss()));
        }

        tradeRepository.save(trade);

        currUser.getTrades().add(trade);
        tradeRepository.save(trade);

        return new ResponseBody(true, "Trade added successfully", trade);

    }


    public ResponseBody getTrade(String tradeId, String userId) {
        User currentUser = findUserById(userId);
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new TradeNotFoundException("Trade not found for ID: " + tradeId));

        return new ResponseBody(true, "Trade details.", trade);
    }


    public ResponseBody getAllTrades(String userId) {
        findUserById(userId); // Ensure the user exists
        List<Trade> trades = tradeRepository.findAllTradesByUserId(userId);

        return new ResponseBody(true, "Holdings retrieved successfully.", trades);
    }

    @Transactional
    public ResponseBody updateTrade(TradeRequestBody tradeRequestBody, String userId, String tradeId) {
        // Ensure the user exists
        User currentUser = findUserById(userId);

        // Find the existing trade by tradeId
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new TradeNotFoundException("Trade not found for ID: " + tradeId));

        // Update trade details with the new values from tradeRequestBody
        trade.setAssetName(tradeRequestBody.getAssetName());
        trade.setTradeType(tradeRequestBody.getTradeType());
        trade.setQuantity(tradeRequestBody.getQuantity());
        trade.setAssetType(tradeRequestBody.getAssetType());
        trade.setEnterPrice(tradeRequestBody.getEnterPrice());
        trade.setStopLoss(tradeRequestBody.getStopLoss());
        trade.setExitPrice(tradeRequestBody.getExitPrice());
        trade.setTradeCategory(tradeRequestBody.getTradeCategory());
        trade.setStrategyName(tradeRequestBody.getStrategyName());
        trade.setStrategyDescription(tradeRequestBody.getStrategyDescription());
        trade.setDate(tradeRequestBody.getDate());

        // Recalculate total trade value and profit/loss based on updated information
        Double totalTradedVal = trade.getEnterPrice() * trade.getQuantity();
        Double currTradedVal = trade.getExitPrice() * trade.getQuantity();
        trade.setTotalTradeValue(totalTradedVal);
        trade.setProfitOrLoss(currTradedVal - totalTradedVal);

        // Save the updated trade to the repository
        tradeRepository.save(trade);

        logger.info("Trade updated successfully for Trade ID: {}", tradeId);

        // Return the updated trade information in the response
        return new ResponseBody(true, "Trade updated successfully.", trade);
    }


    @Transactional
    public ResponseBody squareOffTrade(String userId, String tradeId) {
        User currentUser = findUserById(userId);
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new HoldingNotFoundException("Trade not found for ID: " + tradeId));

        Journal journal = new Journal(
                trade.getAssetName(),
                "Trade",
                trade.getAssetType(),
                trade.getQuantity(),
                trade.getEnterPrice(),
                trade.getStopLoss(),
                trade.getExitPrice(),
                trade.getTotalTradeValue(),
                "sell",
                trade.getProfitOrLoss(),
                trade.getDate(),
                trade.getStrategyName(),
                trade.getStrategyDescription(),
                currentUser
        );

        journalRepository.save(journal);

        currentUser.getTrades().remove(trade);
        currentUser.getJournal().add(journal);

        userRepository.save(currentUser);
        tradeRepository.deleteById(tradeId);

        logger.info("Trade squared off successfully for trade ID: {}", tradeId);
        return new ResponseBody(true, "trade squared off successfully.", trade);
    }


}
