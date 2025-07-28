package com.example.fastapi.dboModel;

public class SaleItem {

    private String inventoryItemId;     // آیدی قطعه از InventoryItem
    private String partName;            // نام قطعه (برای نمایش سریع)
    private Integer quantitySold;       // تعداد فروخته‌شده
    private Double unitSalePrice;       // قیمت واحد فروش

    // --- Constructors ---
    public SaleItem() {}

    public SaleItem(String inventoryItemId, String partName, Integer quantitySold, Double unitSalePrice) {
        this.inventoryItemId = inventoryItemId;
        this.partName = partName;
        this.quantitySold = quantitySold;
        this.unitSalePrice = unitSalePrice;
    }

    // --- Getters & Setters ---
    public String getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(String inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Integer getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }

    public Double getUnitSalePrice() {
        return unitSalePrice;
    }

    public void setUnitSalePrice(Double unitSalePrice) {
        this.unitSalePrice = unitSalePrice;
    }
}
