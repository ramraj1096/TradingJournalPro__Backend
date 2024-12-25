package com.tradingjournal_pro.backend.utils;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplateUtils {

    private String getStyledTemplate(String name, String otp, String action) {
        return "<html>" +
                "<head>" +
                "<style>" +
                "body {" +
                "   font-family: Arial, sans-serif;" +
                "   margin: 0;" +
                "   padding: 0;" +
                "   background-color: #f4f4f4;" +
                "   color: #333;" +
                "   text-align: center;" +
                "}" +
                "h1 {" +
                "   color: #333;" +
                "   font-size: 24px;" +
                "   margin-top: 20px;" +
                "}" +
                "p {" +
                "   font-size: 18px;" +
                "   line-height: 1.6;" +
                "}" +
                ".otp {" +
                "   font-size: 20px;" +
                "   font-weight: bold;" +
                "   color: #007bff;" +
                "   margin: 20px 0;" +
                "   padding: 10px;" +
                "   border: 1px solid #ddd;" +
                "   border-radius: 5px;" +
                "   display: inline-block;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h1>Hello " + name + "</h1>" +
                "<p>Your OTP for " + action + " is:</p>" +
                "<p class='otp'>" + otp + "</p>" +
                "</body>" +
                "</html>";
    }

    public String registerHtmlTemplate(String name, String otp) {
        return getStyledTemplate(name, otp, "registration");
    }

    public String loginHtmlTemplate(String name, String otp) {
        return getStyledTemplate(name, otp, "login");
    }

    public String resetHtmlTemplate(String name, String otp) {
        return getStyledTemplate(name, otp, "reset password");
    }
}
