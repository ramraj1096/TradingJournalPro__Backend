package com.tradingjournal_pro.backend.controller;

import com.tradingjournal_pro.backend.dto.EmailRequestBody;
import com.tradingjournal_pro.backend.dto.ResponseBody;
import com.tradingjournal_pro.backend.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Endpoint to send OTP email
    @PostMapping("/send-email")
    public ResponseBody sendEmail(@Valid @RequestBody EmailRequestBody emailRequestBody) {
        return emailService.generateAndSendOtp(
                emailRequestBody.getTo(),
                emailRequestBody.getEmail(),
                emailRequestBody.getUseCase(),
                emailRequestBody.getName());
    }

    // Endpoint to verify OTP
    @GetMapping("/verify-otp")
    public ResponseBody verifyOtp(@RequestParam String otp) {
        return emailService.verifyOtp(otp);
    }
}
