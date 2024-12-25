package com.tradingjournal_pro.backend.service;

import com.tradingjournal_pro.backend.dto.HoldingRequestBody;
import com.tradingjournal_pro.backend.dto.ResponseBody;
import com.tradingjournal_pro.backend.exceptions.HoldingNotFoundException;
import com.tradingjournal_pro.backend.exceptions.UserNotFoundException;
import com.tradingjournal_pro.backend.models.Holding;
import com.tradingjournal_pro.backend.models.Journal;
import com.tradingjournal_pro.backend.models.User;
import com.tradingjournal_pro.backend.repository.HoldingRepository;
import com.tradingjournal_pro.backend.repository.JournalRepository;
import com.tradingjournal_pro.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HoldingService {
    private static final Logger logger = LoggerFactory.getLogger(HoldingService.class);
    private final HoldingRepository holdingRepository;
    private final UserRepository userRepository;
    private final JournalRepository journalRepository;

    public HoldingService(HoldingRepository holdingRepository,
                          UserRepository userRepository,
                          JournalRepository journalRepository) {
        this.holdingRepository = holdingRepository;
        this.userRepository = userRepository;
        this.journalRepository = journalRepository;
    }

    private User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + userId));
    }

    private Holding createHolding(HoldingRequestBody holdingRequestBody, User currentUser) {
        Double totalInvestedValue = holdingRequestBody.getBoughtPrice() * holdingRequestBody.getQuantity();
        Double currentInvestmentValue = holdingRequestBody.getCurrentPrice() * holdingRequestBody.getQuantity();

        return new Holding(
                holdingRequestBody.getAssetName(),
                holdingRequestBody.getQuantity(),
                holdingRequestBody.getBoughtPrice(),
                holdingRequestBody.getCurrentPrice(),
                totalInvestedValue,
                currentInvestmentValue,
                holdingRequestBody.getDate(),
                currentUser
        );
    }

    private Journal createJournal(HoldingRequestBody holdingRequestBody, User currentUser) {
        Double totalTradedValue = holdingRequestBody.getBoughtPrice() * holdingRequestBody.getQuantity();
        Double currTradeValue = holdingRequestBody.getCurrentPrice() * holdingRequestBody.getQuantity();
        Double profitLoss = currTradeValue - totalTradedValue;

        return new Journal(
                holdingRequestBody.getAssetName(),
                "Holding",
                "equity",
                holdingRequestBody.getQuantity(),
                holdingRequestBody.getBoughtPrice(),
                0.0,
                holdingRequestBody.getCurrentPrice(),
                totalTradedValue,
                "buy",
                profitLoss,
                holdingRequestBody.getDate(),
                "Holding",
                "Long term Investment",
                currentUser
        );
    }

    @Transactional
    public ResponseBody addHolding(HoldingRequestBody holdingRequestBody, String userId) {
        User currentUser = findUserById(userId);
        Holding holding = createHolding(holdingRequestBody, currentUser);
        Journal journal = createJournal(holdingRequestBody, currentUser);

        try {
            holdingRepository.save(holding);
            journalRepository.save(journal);
            currentUser.getHoldings().add(holding);
            currentUser.getJournal().add(journal);
            userRepository.save(currentUser);

            logger.info("Holding added successfully for user ID: {}", userId);
            System.out.println(holding);
            return new ResponseBody(true, "Holding added successfully.", holding);
        } catch (Exception e) {
            logger.error("Error adding holding for user ID {}: {}", userId, e.getMessage());
            throw e; // Transaction will roll back in case of any exception
        }
    }

    public ResponseBody getHolding(String holdingId, String userId) {
        User currentUser = findUserById(userId);
        Holding holding = holdingRepository.findById(holdingId)
                .orElseThrow(() -> new HoldingNotFoundException("Holding not found for ID: " + holdingId));

        return new ResponseBody(true, "Holding details.", holding);
    }

    public ResponseBody getAllHoldings(String userId) {
        findUserById(userId); // Ensure the user exists
        List<Holding> holdings = holdingRepository.findAllByUserId(userId);

        return new ResponseBody(true, "Holdings retrieved successfully.", holdings);
    }

    @Transactional
    public ResponseBody updateHolding(HoldingRequestBody holdingRequestBody, String userId, String holdingId) {
        findUserById(userId);
        Holding holding = holdingRepository.findById(holdingId)
                .orElseThrow(() -> new HoldingNotFoundException("Holding not found for ID: " + holdingId));

        Double totalInvestedValue = holdingRequestBody.getBoughtPrice() * holdingRequestBody.getQuantity();
        Double currentInvestmentValue = holdingRequestBody.getCurrentPrice() * holdingRequestBody.getQuantity();

        holding.setAssetName(holdingRequestBody.getAssetName());
        holding.setQuantity(holdingRequestBody.getQuantity());
        holding.setBoughtPrice(holdingRequestBody.getBoughtPrice());
        holding.setCurrentPrice(holdingRequestBody.getCurrentPrice());
        holding.setTotalInvestedValue(totalInvestedValue);
        holding.setCurrentInvestmentValue(currentInvestmentValue);

        holdingRepository.save(holding);

        logger.info("Holding updated successfully for holding ID: {}", holdingId);
        return new ResponseBody(true, "Holding updated successfully.", holding);
    }

    @Transactional
    public ResponseBody squareOffHolding(String userId, String holdingId) {
        User currentUser = findUserById(userId);
        Holding holding = holdingRepository.findById(holdingId)
                .orElseThrow(() -> new HoldingNotFoundException("Holding not found for ID: " + holdingId));

        Journal journal = new Journal(
                holding.getAssetName(),
                "Holding",
                "equity",
                holding.getQuantity(),
                holding.getBoughtPrice(),
                0.0,
                holding.getCurrentPrice(),
                holding.getCurrentInvestmentValue(),
                "sell",
                holding.getCurrentInvestmentValue() - holding.getTotalInvestedValue(),
                holding.getDate(),
                "Holding",
                "Long term Investment",
                currentUser
        );

        journalRepository.save(journal);
        currentUser.getHoldings().remove(holding);
        currentUser.getJournal().add(journal);
        userRepository.save(currentUser);
        holdingRepository.deleteById(holdingId);

        logger.info("Holding squared off successfully for holding ID: {}", holdingId);
        return new ResponseBody(true, "Holding squared off successfully.", holding);
    }
}
