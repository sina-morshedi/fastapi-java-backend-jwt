package com.example.fastapi.dto;

public class InventoryQuantityChangeRequestDTO {
    private String itemId;
    private int amount;  // برای decrement و increment می‌تونیم همون کلاس رو استفاده کنیم

    // Constructor، Getter و Setter

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

