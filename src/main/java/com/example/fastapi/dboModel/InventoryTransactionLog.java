package com.example.fastapi.dboModel;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

@Document(collection = "inventoryTransactionLog")
public class InventoryTransactionLog {

    public enum TransactionType {
        SALE,
        CONSUMPTION,
        RETURN_SALE,
        RETURN_CONSUMPTION,
        DAMAGE,
        INCOMING
    }

    @Id
    private ObjectId id;

    private ObjectId carInfoId;       // شناسه ماشین
    private ObjectId customerId;      // شناسه مشتری
    private ObjectId creatorUserId;          // شناسه کاربر ثبت کننده
    private ObjectId inventoryItemId; // شناسه قطعه مورد نظر

    private int quantity;             // تعداد قطعات مصرف شده یا برگشتی

    private TransactionType type;    // نوع تراکنش

    @Nullable
    private String description;       // توضیحات اضافی (اختیاری)

    private LocalDateTime dateTime;   // زمان ثبت تراکنش

    // --- Constructors ---

    public InventoryTransactionLog() {
        this.dateTime = LocalDateTime.now();
    }

    public InventoryTransactionLog(ObjectId carInfoId, ObjectId customerId, ObjectId creatorUserId, ObjectId inventoryItemId,
                                   int quantity, TransactionType type, @Nullable String description) {
        this.carInfoId = carInfoId;
        this.customerId = customerId;
        this.creatorUserId = creatorUserId;
        this.inventoryItemId = inventoryItemId;
        this.quantity = quantity;
        this.type = type;
        this.description = description;
        this.dateTime = LocalDateTime.now();
    }

    // --- Getters and Setters ---

    public String getId() {
        return id != null ? id.toHexString() : null;
    }

    public void setId(String id) {
        if (id != null && !id.isBlank()) {
            this.id = new ObjectId(id);
        }
    }

    public ObjectId getCarInfoId() {
        return carInfoId;
    }

    public void setCarInfoId(ObjectId carInfoId) {
        this.carInfoId = carInfoId;
    }

    public ObjectId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(ObjectId customerId) {
        this.customerId = customerId;
    }

    public ObjectId getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(ObjectId creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public ObjectId getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(ObjectId inventoryItemId) {
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

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
