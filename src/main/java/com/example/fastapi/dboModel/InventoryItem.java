package com.example.fastapi.dboModel;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "inventoryItems")
public class InventoryItem {

    @Id
    private ObjectId id;

    private String partName;         // نام قطعه
    private String barcode;          // بارکد یکتا
    private String category;         // دسته‌بندی (مثلاً ترمز، موتور...)
    private Integer quantity;        // تعداد موجودی
    private String unit;             // واحد (عدد، جفت، لیتر...)
    private String location;         // محل نگهداری در انبار
    private Double purchasePrice;    // قیمت خرید
    private Double salePrice;        // قیمت فروش
    private Boolean isActive;        // وضعیت فعال بودن
    private Date createdAt;          // زمان ثبت
    private Date updatedAt;          // زمان آخرین تغییر

    // --- Constructors ---
    public InventoryItem() {
    }

    public InventoryItem(String partName, String barcode, String category, Integer quantity, String unit,
                         String location, Double purchasePrice, Double salePrice,
                         Boolean isActive, Date createdAt, Date updatedAt) {
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

    // --- Getters & Setters ---

    public String getId() {
        return id != null ? id.toHexString() : null;
    }

    public void setId(String id) {
        if (id != null && !id.isBlank()) {
            this.id = new ObjectId(id);
        }
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
