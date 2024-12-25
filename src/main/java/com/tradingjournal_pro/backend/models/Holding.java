package com.tradingjournal_pro.backend.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "holdings")
public class Holding {

    @Id
    private String id;

    @NotBlank(message = "Asset name is required")
    private String assetName;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Bought price is required")
    private Double boughtPrice;

    @NotNull(message = "Current price is required")
    private Double currentPrice;

    @Min(value = 0, message = "Total invested value cannot be negative")
    private Double totalInvestedValue = 0.0;

    @Min(value = 0, message = "Current investment value cannot be negative")
    private Double currentInvestmentValue = 0.0;

    @NotNull(message = "Date is required")
    private Date date = new Date();

    @JsonIgnore
    @DBRef
    private User user;

    private Date createdAt = new Date();

    public Holding(String assetName,
                   Integer quantity,
                   Double boughtPrice,
                   Double currentPrice,
                   Double totalInvestedValue,
                   Double currentInvestmentValue,
                   Date date, User user) {
        this.assetName = assetName;
        this.quantity = quantity;
        this.boughtPrice = boughtPrice;
        this.currentPrice = currentPrice;
        this.totalInvestedValue = totalInvestedValue;
        this.currentInvestmentValue = currentInvestmentValue;
        this.date = date;
        this.user = user;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getBoughtPrice() {
        return boughtPrice;
    }

    public void setBoughtPrice(Double boughtPrice) {
        this.boughtPrice = boughtPrice;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Double getTotalInvestedValue() {
        return totalInvestedValue;
    }

    public void setTotalInvestedValue(Double totalInvestedValue) {
        this.totalInvestedValue = totalInvestedValue;
    }

    public Double getCurrentInvestmentValue() {
        return currentInvestmentValue;
    }

    public void setCurrentInvestmentValue(Double currentInvestmentValue) {
        this.currentInvestmentValue = currentInvestmentValue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
