package com.example.fastapi.dto;

import java.time.LocalDateTime;

public class InventoryTransactionResponseDTO {

    public enum TransactionType {
        SALE,
        CONSUMPTION,
        RETURN_SALE,
        RETURN_CONSUMPTION,
        DAMAGE,
        INCOMING
    }

    private String id;
    private CarInfoDTO carInfo;           // اطلاعات ماشین به صورت کامل
    private CustomerDTO customer;          // اطلاعات مشتری به صورت کامل
    private UserProfileDTO creatorUser;    // اطلاعات کاربر ثبت کننده
    private InventoryItemDTO inventoryItem; // اطلاعات قطعه

    private int quantity;
    private TransactionType type;
    private String description;
    private LocalDateTime dateTime;

    public InventoryTransactionResponseDTO() {
    }

    public InventoryTransactionResponseDTO(String id, CarInfoDTO carInfo, CustomerDTO customer,
                                           UserProfileDTO creatorUser, InventoryItemDTO inventoryItem,
                                           int quantity, TransactionType type, String description,
                                           LocalDateTime dateTime) {
        this.id = id;
        this.carInfo = carInfo;
        this.customer = customer;
        this.creatorUser = creatorUser;
        this.inventoryItem = inventoryItem;
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

    public CarInfoDTO getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(CarInfoDTO carInfo) {
        this.carInfo = carInfo;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public UserProfileDTO getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(UserProfileDTO creatorUser) {
        this.creatorUser = creatorUser;
    }

    public InventoryItemDTO getInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(InventoryItemDTO inventoryItem) {
        this.inventoryItem = inventoryItem;
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
