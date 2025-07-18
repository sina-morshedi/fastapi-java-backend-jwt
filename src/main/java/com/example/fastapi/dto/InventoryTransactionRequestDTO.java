package com.example.fastapi.dto;

import java.time.LocalDateTime;

public class InventoryTransactionRequestDTO {

    public enum TransactionType {
        SALE,
        CONSUMPTION,
        RETURN_SALE,
        RETURN_CONSUMPTION,
        DAMAGE
    }

    private String id;               // می‌تونید این رو اختیاری در نظر بگیرید
    private String carInfoId;
    private String customerId;
    private String creatorUserId;
    private String inventoryItemId;

    private int quantity;
    private TransactionType type;
    private String description;
    private LocalDateTime dateTime;

    public InventoryTransactionRequestDTO() {
    }

    public InventoryTransactionRequestDTO(String id, String carInfoId, String customerId, String creatorUserId,
                                          String inventoryItemId, int quantity, TransactionType type,
                                          String description, LocalDateTime dateTime) {
        this.id = id;
        this.carInfoId = carInfoId;
        this.customerId = customerId;
        this.creatorUserId = creatorUserId;
        this.inventoryItemId = inventoryItemId;
        this.quantity = quantity;
        this.type = type;
        this.description = description;
        this.dateTime = dateTime;
    }

    // Getter و Setter ها

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarInfoId() {
        return carInfoId;
    }

    public void setCarInfoId(String carInfoId) {
        this.carInfoId = carInfoId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(String inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
