package com.tradingjournal_pro.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Date;

public class TradeRequestBody {
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

    @NotNull(message = " Enter price is required")
    @Min(value = 1, message = "Enter price must be at least 1")
    private Double enterPrice = 0.0;

    @NotNull(message = " Stop loss is required")
    @Min(value = 1, message = "Stop loss must be at least 1")
    private Double stopLoss = 0.0;

    @NotNull(message = "Trade category is required")
    @Min(value = 1, message = "Exit price must be at least 1")
    private Double exitPrice = 0.0;

    @NotBlank(message = "Strategy name is required")
    private String strategyName;

    @NotBlank(message = "Strategy description is required")
    private String strategyDescription;

    @NotNull(message = "Date is required")
    private Date date;

    public  String getAssetName() {
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

    public String getTradeCategory() {
        return tradeCategory;
    }

    public void setTradeCategory( String tradeCategory) {
        this.tradeCategory = tradeCategory;
    }

    public  Double getEnterPrice() {
        return enterPrice;
    }

    public void setEnterPrice( Double enterPrice) {
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

    public void setExitPrice( Double exitPrice) {
        this.exitPrice = exitPrice;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName( String strategyName) {
        this.strategyName = strategyName;
    }

    public String getStrategyDescription() {
        return strategyDescription;
    }

    public void setStrategyDescription( String strategyDescription) {
        this.strategyDescription = strategyDescription;
    }

    public Date getDate() {
        return date;
    }

    public void setDate( Date date) {
        this.date = date;
    }
}
