package com.tradingjournal_pro.backend.repository;

import com.tradingjournal_pro.backend.models.Journal;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface JournalRepository extends MongoRepository<Journal, String> {
    List<Journal> findAllJournalsByUserId(String userId);
}
