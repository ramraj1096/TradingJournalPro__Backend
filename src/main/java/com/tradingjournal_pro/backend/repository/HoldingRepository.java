package com.tradingjournal_pro.backend.repository;

import com.tradingjournal_pro.backend.models.Holding;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HoldingRepository extends MongoRepository<Holding, String> {
    List<Holding> findAllByUserId(String userId);
}
