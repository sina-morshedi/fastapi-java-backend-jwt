package com.example.fastapi.dto;

import java.time.LocalDateTime;

public class InventoryTransactionLogDTO {

    public enum TransactionType {
        SALE,           // فروش
        CONSUMPTION,    // مصرف در تعمیرگاه
        RETURN_SALE,    // بازگشت از فروش
        RETURN_CONSUMPTION, // بازگشت از مصرف تعمیرگاه
        DAMAGE          // خرابی یا دور ریختن قطعه
    }

    private String id;

    private String carInfoId;       // شناسه ماشین
    private String customerId;      // شناسه مشتری
    private String userId;          // شناسه کاربر ثبت کننده
    private String inventoryItemId; // شناسه قطعه

    private int quantity;           // تعداد قطعات مصرف شده یا برگشتی

    private TransactionType type;  // نوع تراکنش

    private String description;     // توضیحات (nullable)

    private LocalDateTime dateTime; // زمان ثبت تراکنش

    public InventoryTransactionLogDTO() {
    }

    public InventoryTransactionLogDTO(String id, String carInfoId, String customerId, String userId,
                                      String inventoryItemId, int quantity, TransactionType type,
                                      String description, LocalDateTime dateTime) {
        this.id = id;
        this.carInfoId = carInfoId;
        this.customerId = customerId;
        this.userId = userId;
        this.inventoryItemId = inventoryItemId;
        this.quantity = quantity;
        this.type = type;
        this.description = description;
        this.dateTime = dateTime;
    }

    // --- Getters and Setters ---

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
