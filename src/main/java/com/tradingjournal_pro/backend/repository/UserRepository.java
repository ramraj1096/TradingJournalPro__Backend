package com.tradingjournal_pro.backend.repository;

import com.tradingjournal_pro.backend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> { // Ensure _id type matches
    User findByEmail(String email);

}
