package com.tradingjournal_pro.backend.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class HoldingRequestBody {

    @NotBlank(message = "Asset name is required")
    private String assetName;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Bought price is required")
    @Min(value = 1, message = "Bought price must be at least 1")
    private Double boughtPrice;

    @NotNull(message = "Current price is required")
    @Min(value = 1, message = "Current price must be at least 1")
    private Double currentPrice;

    @NotNull(message = "Date is required")
    private Date date = new Date();

    public HoldingRequestBody(String assetName,
                              Integer quantity,
                              Double boughtPrice,
                              Double currentPrice,
                              Date date) {
        this.assetName = assetName;
        this.quantity = quantity;
        this.boughtPrice = boughtPrice;
        this.currentPrice = currentPrice;
        this.date = date;
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

    public void setQuantity( Integer quantity) {
        this.quantity = quantity;
    }

    public Double getBoughtPrice() {
        return boughtPrice;
    }

    public void setBoughtPrice( Double boughtPrice) {
        this.boughtPrice = boughtPrice;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice( Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate( Date date) {
        this.date = date;
    }
}
