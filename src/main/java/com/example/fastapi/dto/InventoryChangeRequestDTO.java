package com.example.fastapi.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class InventoryChangeRequestDTO {
    private String itemId;
    private Integer amount;
    private Date updatedAt;
    private Double purchasePrice;
    private Double salePrice;
    private String creatorUserId;

    // Getters
    public String getItemId() {
        return itemId;
    }

    public Integer getAmount() {
        return amount;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    // Setters
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    @Override
    public String toString() {
        return "InventoryChangeRequestDTO{" +
                "itemId='" + itemId + '\'' +
                ", amount=" + amount +
                ", updatedAt=" + updatedAt +
                ", purchasePrice=" + purchasePrice +
                ", salePrice=" + salePrice +
                ", creatorUserId='" + creatorUserId + '\'' +
                '}';
    }
}
