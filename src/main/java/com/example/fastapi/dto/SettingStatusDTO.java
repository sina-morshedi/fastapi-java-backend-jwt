package com.example.fastapi.dto;

public class SettingStatusDTO {
    private boolean inventoryEnabled;
    private boolean customerEnabled;

    public SettingStatusDTO() {}

    public SettingStatusDTO(boolean inventoryEnabled, boolean customerEnabled) {
        this.inventoryEnabled = inventoryEnabled;
        this.customerEnabled = customerEnabled;
    }

    public boolean isInventoryEnabled() {
        return inventoryEnabled;
    }

    public void setInventoryEnabled(boolean inventoryEnabled) {
        this.inventoryEnabled = inventoryEnabled;
    }

    public boolean isCustomerEnabled() {
        return customerEnabled;
    }

    public void setCustomerEnabled(boolean customerEnabled) {
        this.customerEnabled = customerEnabled;
    }
}
