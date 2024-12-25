package com.tradingjournal_pro.backend.service;

import com.tradingjournal_pro.backend.dto.JournalRequestBody;
import com.tradingjournal_pro.backend.dto.ResponseBody;
import com.tradingjournal_pro.backend.exceptions.JournalNotFoundException;
import com.tradingjournal_pro.backend.exceptions.UserNotFoundException;
import com.tradingjournal_pro.backend.models.Journal;
import com.tradingjournal_pro.backend.models.User;
import com.tradingjournal_pro.backend.repository.JournalRepository;
import com.tradingjournal_pro.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JournalService {
    private static final Logger logger = LoggerFactory.getLogger(JournalService.class);
    private final UserRepository userRepository;
    private final JournalRepository journalRepository;

    public JournalService(UserRepository userRepository, JournalRepository journalRepository) {
        this.userRepository = userRepository;
        this.journalRepository = journalRepository;
    }

    private User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for ID: " + userId));
    }

    public Journal createJournal(JournalRequestBody journalRequestBody, User currUser) {
        Double totalTradedVal = journalRequestBody.getEnterPrice() * journalRequestBody.getQuantity();
        Double currTradedVal = journalRequestBody.getExitPrice() * journalRequestBody.getQuantity();

        return new Journal(
                journalRequestBody.getAssetName(),
                journalRequestBody.getJournalFor(),
                journalRequestBody.getAssetType(),
                journalRequestBody.getQuantity(),
                journalRequestBody.getEnterPrice(),
                journalRequestBody.getStopLoss(),
                journalRequestBody.getExitPrice(),
                totalTradedVal,
                journalRequestBody.getTradeCategory(),
                currTradedVal - totalTradedVal,
                journalRequestBody.getDate(),
                journalRequestBody.getStrategyName(),
                journalRequestBody.getStrategyDescription(),
                currUser
        );
    }

    @Transactional
    public ResponseBody addJournal(JournalRequestBody journalRequestBody, String userId) {
        User currUser = findUserById(userId);

        Journal journal = createJournal(journalRequestBody, currUser);
        journalRepository.save(journal);

        currUser.getJournal().add(journal);
        userRepository.save(currUser);

        return new ResponseBody(true, "Journal created successfully", journal);
    }

    public ResponseBody getAllJournals(String userId) {
        findUserById(userId); // Ensure the user exists
        List<Journal> list = journalRepository.findAllJournalsByUserId(userId);
        return new ResponseBody(true, "All Journals", list);
    }

    public ResponseBody getJournal(String userId, String journalId) {
        findUserById(userId); // Ensure the user exists
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException("Journal Not Found"));
        return new ResponseBody(true, "Journal details", journal);
    }

    @Transactional
    public ResponseBody updateJournal(JournalRequestBody journalRequestBody, String userId, String journalId) {
        findUserById(userId); // Ensure the user exists

        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException("Journal Not Found"));

        // Update journal details
        journal.setAssetName(journalRequestBody.getAssetName());
        journal.setJournalFor(journalRequestBody.getJournalFor());
        journal.setAssetType(journalRequestBody.getAssetType());
        journal.setQuantity(journalRequestBody.getQuantity());
        journal.setEnterPrice(journalRequestBody.getEnterPrice());
        journal.setStopLoss(journalRequestBody.getStopLoss());
        journal.setExitPrice(journalRequestBody.getExitPrice());
        journal.setTradeCategory(journalRequestBody.getTradeCategory());
        journal.setStrategyName(journalRequestBody.getStrategyName());
        journal.setStrategyDescription(journalRequestBody.getStrategyDescription());
        journal.setDate(journalRequestBody.getDate());

        // Recalculate trade values
        Double totalTradedVal = journalRequestBody.getEnterPrice() * journalRequestBody.getQuantity();
        Double currTradedVal = journalRequestBody.getExitPrice() * journalRequestBody.getQuantity();
        journal.setTotalTradedValue(totalTradedVal);
        journal.setProfitorLoss(currTradedVal - totalTradedVal);

        // Save updated journal
        journalRepository.save(journal);

        logger.info("Journal updated successfully for Journal ID: {}", journalId);
        return new ResponseBody(true, "Journal updated successfully", journal);
    }

    @Transactional
    public ResponseBody squareOffJournal(String userId, String journalId) {
        User user = findUserById(userId);

        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException("Journal Not Found"));

        // Remove journal from user's list and delete it
        user.getJournal().remove(journal);
        userRepository.save(user);
        journalRepository.deleteById(journalId);

        logger.info("Journal deleted successfully for Journal ID: {}", journalId);
        return new ResponseBody(true, "Journal deleted successfully", null);
    }
}
