package com.tradingjournal_pro.backend.service;

import com.tradingjournal_pro.backend.dto.ResponseBody;
import com.tradingjournal_pro.backend.exceptions.UserNotFoundException;
import com.tradingjournal_pro.backend.models.User;
import com.tradingjournal_pro.backend.repository.UserRepository;
import com.tradingjournal_pro.backend.utils.EmailTemplateUtils;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class EmailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailTemplateUtils emailTemplateUtils;

    private static final long OTP_EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(5); // 5 minutes expiration
    private String generatedOtp;
    private long otpTimestamp;

    // Method to generate a 6-digit OTP
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generates a 6-digit OTP
        return String.valueOf(otp);
    }

    // Method to send OTP email
    public void sendHtmlOtpEmail(String to, String email, String useCase, String name, String otp) {
        try {
            String htmlContent = "";
            String subject = "";

            if (useCase.equals("register")) {
                htmlContent = emailTemplateUtils.registerHtmlTemplate(name, otp);
                subject = "OTP for register TradingJournalPro";
            } else if (useCase.equals("login")) {
                User user = userRepository.findByEmail(email);

                if (user == null) {
                    throw new UserNotFoundException("User not found with email: " + email);
                }

                name = user.getName();
                htmlContent = emailTemplateUtils.loginHtmlTemplate(name, otp);
                subject = "OTP for login TradingJournalPro";
            } else {
                htmlContent = emailTemplateUtils.resetHtmlTemplate(name, otp);
                subject = "OTP for reset password TradingJournalPro";
            }

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);  // true enables HTML content
            helper.setFrom("tradingjournalpro@gmail.com");

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            e.printStackTrace(); // Handle properly by logging in real production code
        }
    }

    // Method to verify OTP
    public ResponseBody verifyOtp(String otp) {
        // Check if OTP is expired
        if (System.currentTimeMillis() - otpTimestamp > OTP_EXPIRATION_TIME) {
            return new ResponseBody(false, "OTP expired", null);
        }

        // Check if the OTP is correct
        if (generatedOtp.equals(otp)) {
            return new ResponseBody(true, "OTP is correct", otp);
        } else {
            return new ResponseBody(false, "Invalid OTP", null);
        }
    }

    // Method to generate and send OTP
    public ResponseBody generateAndSendOtp(String to, String email, String useCase, String name) {
        String otp = generateOtp();
        generatedOtp = otp;
        otpTimestamp = System.currentTimeMillis();
        sendHtmlOtpEmail(to, email, useCase, name, otp);

        return new ResponseBody(true, "OTP sent successfully", generatedOtp);
    }
}
