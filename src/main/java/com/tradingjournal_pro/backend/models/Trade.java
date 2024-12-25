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

@Document(collection = "trades")
public class Trade {
    @Id
    private String id;

    @NotBlank(message = "Asset name is required")
    private String assetName;

    @NotBlank(message = "Trade type is required")
    @Pattern(regexp = "Swing Trade|Day Trade|BTST", message = "Trade type must be one of Swing Trade, Day Trade, or BTST")
    private String tradeType;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotBlank(message = "Trade category is required")
    @Pattern(regexp = "equity|option|commodity", message = "Asset type must be one of equity, option, or commodity")
    private String assetType;

    @NotBlank(message = "Trade category is required")
    @Pattern(regexp = "buy|sell", message = "Trade category must be either buy or sell")
    private String tradeCategory;

    private Double profitOrLoss = 0.0;

    private Double enterPrice = 0.0;

    private Double stopLoss = 0.0;

    private Double exitPrice = 0.0;

    private Double totalTradeValue = 0.0;

    @NotBlank(message = "Strategy name is required")
    private String strategyName;

    @NotBlank(message = "Strategy description is required")
    private String strategyDescription;

    @NotNull(message = "Date is required")
    private Date date;

    @NotNull(message = "User reference is required")

    @JsonIgnore
    @DBRef
    private User user;

    private Date createdAt = new Date();

    public Trade(String assetName,
                 String tradeType,
                 Integer quantity,
                 String assetType,
                 Double totalTradeValue,
                 String tradeCategory,
                 Double profitOrLoss,
                 Double enterPrice,
                 Double stopLoss,
                 Double exitPrice,
                 String strategyName,
                 String strategyDescription,
                 Date date,
                 User user) {
        this.assetName = assetName;
        this.tradeType = tradeType;
        this.quantity = quantity;
        this.assetType = assetType;
        this.totalTradeValue = totalTradeValue;
        this.tradeCategory = tradeCategory;
        this.profitOrLoss = profitOrLoss;
        this.enterPrice = enterPrice;
        this.stopLoss = stopLoss;
        this.exitPrice = exitPrice;
        this.strategyName = strategyName;
        this.strategyDescription = strategyDescription;
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

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public Double getTotalTradeValue() {
        return totalTradeValue;
    }

    public void setTotalTradeValue(Double totalTradeValue) {
        this.totalTradeValue = totalTradeValue;
    }

    public String getTradeCategory() {
        return tradeCategory;
    }

    public void setTradeCategory(String tradeCategory) {
        this.tradeCategory = tradeCategory;
    }

    public Double getProfitOrLoss() {
        return profitOrLoss;
    }

    public void setProfitOrLoss(Double profitOrLoss) {
        this.profitOrLoss = profitOrLoss;
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
