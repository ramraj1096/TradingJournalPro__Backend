package com.tradingjournal_pro.backend.controller;


import com.tradingjournal_pro.backend.dto.AuthenticationRequestBody;
import com.tradingjournal_pro.backend.dto.LoginRequestBody;
import com.tradingjournal_pro.backend.dto.ResetPasswordRequest;
import com.tradingjournal_pro.backend.dto.ResponseBody;
import com.tradingjournal_pro.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseBody register(@Valid @RequestBody AuthenticationRequestBody authenticationRequestBody) {
        return userService.register(authenticationRequestBody);
    }

    @PostMapping("/login")
    public ResponseBody login(@Valid @RequestBody LoginRequestBody loginRequestBody) {
        return userService.login(loginRequestBody);
    }

    @PostMapping("/reset")
    public ResponseBody reset(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return userService.resetPassword(resetPasswordRequest.getEmail(), resetPasswordRequest.getNewPassword());
    }
}


