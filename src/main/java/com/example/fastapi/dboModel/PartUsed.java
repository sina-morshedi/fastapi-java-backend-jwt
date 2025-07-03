package com.example.fastapi.dboModel;

public class PartUsed {
    private String partName;
    private double partPrice;
    private int quantity = 1;  // مقدار پیش‌فرض

    // سازنده پیش‌فرض
    public PartUsed() {
    }

    // سازنده با پارامترها
    public PartUsed(String partName, double partPrice, int quantity) {
        this.partName = partName;
        this.partPrice = partPrice;
        this.quantity = quantity;
    }

    // Getter و Setter ها

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public double getPartPrice() {
        return partPrice;
    }

    public void setPartPrice(double partPrice) {
        this.partPrice = partPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
