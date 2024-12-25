package com.tradingjournal_pro.backend.repository;

import com.tradingjournal_pro.backend.models.Journal;
import com.tradingjournal_pro.backend.models.Trade;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TradeRepository extends MongoRepository<Trade, String> {
   List<Trade> findAllTradesByUserId(String userId);

}