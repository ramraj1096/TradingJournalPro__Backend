package com.tradingjournal_pro.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "journals")
public class Journal {

    @Id
    private String id;

    @NotNull(message = "Asset name is required")
    private String assetName;

    @NotBlank(message = "Journal type is required")
    @Pattern(regexp = "Trade|Holding", message = "Journal type must be either Trade or Holding")
    private String journalFor;

    @NotBlank(message = "Asset type is required")
    @Pattern(regexp = "equity|option|commodity", message = "Asset type must be one of equity, option, or commodity")
    private String assetType;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Enter price is required")
    private Double enterPrice;

    private Double stopLoss = 0.0;

    @NotNull(message = "Exit price is required")
    private Double exitPrice;


    @Min(value = 0, message = "Total traded value cannot be negative")
    private Double totalTradedValue;

    @NotBlank(message = "Trade category is required")
    @Pattern(regexp = "buy|sell", message = "Trade category must be either buy or sell")
    private String tradeCategory;


    private Double profitorLoss;

    @NotNull(message = "Date is required")
    private Date date;

    @NotBlank(message = "Strategy name is required")
    private String strategyName;

    @NotBlank(message = "Strategy description is required")
    private String strategyDescription;

    @JsonIgnore
    @DBRef
    private User user;
    private Date createdAt = new Date();

    public Journal(String assetName,
                   String journalFor,
                   String assetType,
                   Integer quantity,
                   Double enterPrice,
                   Double stopLoss,
                   Double exitPrice,
                   Double totalTradedValue,
                   String tradeCategory,
                   Double profitorLoss,
                   Date date,
                   String strategyName,
                   String strategyDescription,
                   User user) {
        this.assetName = assetName;
        this.journalFor = journalFor;
        this.assetType = assetType;
        this.quantity = quantity;
        this.enterPrice = enterPrice;
        this.stopLoss = stopLoss;
        this.exitPrice = exitPrice;
        this.totalTradedValue = totalTradedValue;
        this.tradeCategory = tradeCategory;
        this.profitorLoss = profitorLoss;
        this.date = date;
        this.strategyName = strategyName;
        this.strategyDescription = strategyDescription;
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

    public String getJournalFor() {
        return journalFor;
    }

    public void setJournalFor(String journalFor) {
        this.journalFor = journalFor;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getEnterPrice() {
        return enterPrice;
    }

    public void setEnterPrice(Double enterPrice) {
        this.enterPrice = enterPrice;
    }

    public Double getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(Double stopLoss) {
        this.stopLoss = stopLoss;
    }

    public Double getExitPrice() {
        return exitPrice;
    }

    public void setExitPrice(Double exitPrice) {
        this.exitPrice = exitPrice;
    }

    public Double getTotalTradedValue() {
        return totalTradedValue;
    }

    public void setTotalTradedValue(Double totalTradedValue) {
        this.totalTradedValue = totalTradedValue;
    }

    public String getTradeCategory() {
        return tradeCategory;
    }

    public void setTradeCategory(String tradeCategory) {
        this.tradeCategory = tradeCategory;
    }

    public Double getProfitorLoss() {
        return profitorLoss;
    }

    public void setProfitorLoss(Double profitorLoss) {
        this.profitorLoss = profitorLoss;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getStrategyDescription() {
        return strategyDescription;
    }

    public void setStrategyDescription(String strategyDescription) {
        this.strategyDescription = strategyDescription;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
