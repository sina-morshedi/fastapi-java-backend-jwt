package com.example.fastapi.dto;

import java.util.Date;

public class InventoryItemDTO {

    private String id;
    private String partName;
    private String barcode;
    private String category;
    private Integer quantity;
    private String unit;
    private String location;
    private Double purchasePrice;
    private Double salePrice;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;

    // Constructors
    public InventoryItemDTO() {
    }

    public InventoryItemDTO(String id, String partName, String barcode, String category,
                            Integer quantity, String unit, String location,
                            Double purchasePrice, Double salePrice,
                            Boolean isActive, Date createdAt, Date updatedAt) {
        this.id = id;
        this.partName = partName;
        this.barcode = barcode;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.location = location;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
