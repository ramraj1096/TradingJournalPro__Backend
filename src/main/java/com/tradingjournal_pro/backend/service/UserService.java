package com.tradingjournal_pro.backend.service;

import com.tradingjournal_pro.backend.dto.AuthenticationRequestBody;
import com.tradingjournal_pro.backend.dto.LoginRequestBody;
import com.tradingjournal_pro.backend.dto.ResponseBody;
import com.tradingjournal_pro.backend.exceptions.IncorrectPasswordException;
import com.tradingjournal_pro.backend.exceptions.UserNotFoundException;
import com.tradingjournal_pro.backend.models.User;
import com.tradingjournal_pro.backend.repository.UserRepository;
import com.tradingjournal_pro.backend.utils.Encoder;
import com.tradingjournal_pro.backend.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final Encoder encoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, Encoder encoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public ResponseBody register(AuthenticationRequestBody registerRequestBody) {
        User existeduser = userRepository.findByEmail(registerRequestBody.getEmail());

        if (existeduser != null) {
            logger.warn("Registration attempt with existing email: {}", registerRequestBody.getEmail());
            return new ResponseBody(
                    false,
                    "Email is already taken.",
                    null);
        }

        User user = new User(
                registerRequestBody.getName(),
                registerRequestBody.getEmail(),
                encoder.encode(registerRequestBody.getPassword())
        );

        userRepository.save(user);
        logger.info("User registered successfully: {}", user.getEmail());
        return new ResponseBody(
                true,
                "User registered successfully.",
                user);
    }

    public ResponseBody login(LoginRequestBody loginRequestBody) {
        User user = userRepository.findByEmail(loginRequestBody.getEmail());

        if (user == null) {
            logger.warn("Login attempt failed: User not found for email {}", loginRequestBody.getEmail());
            throw new UserNotFoundException("User not found.");
        }

        if (!encoder.matches(loginRequestBody.getPassword(), user.getPassword())) {
            logger.warn("Login attempt failed: Incorrect password for email {}", loginRequestBody.getEmail());
            throw new IncorrectPasswordException("Password is incorrect.");
        }

        logger.info("User logged in successfully: {}", user.getEmail());
        String token = jwtUtil.generateToken(loginRequestBody.getEmail());
        return new ResponseBody(
                true,
                "Authentication succeeded.",
                token);
    }

    public ResponseBody resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            logger.warn("Password reset attempt failed: User not found for email {}", email);
            throw new UserNotFoundException("User not found.");
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
        logger.info("Password reset successfully for user: {}", user.getEmail());

        return new ResponseBody(
                true,
                "Password reset succeeded.",
                user);
    }
}