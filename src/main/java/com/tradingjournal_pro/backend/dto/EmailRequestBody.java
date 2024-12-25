package com.tradingjournal_pro.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class EmailRequestBody {

    @NotBlank(message ="Email is required")
    public String email;

    @NotBlank(message = "To is required")
    public String to;

    @NotBlank(message = "useCase is required")
    public String useCase;

    public String name;

    public String getEmail() {
        return email;
    }

    public void setEmail( String email) {
        this.email = email;
    }

    public  String getTo() {
        return to;
    }

    public void setTo( String to) {
        this.to = to;
    }

    public String getUseCase() {
        return useCase;
    }

    public void setUseCase( String useCase) {
        this.useCase = useCase;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
