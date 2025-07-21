package com.example.fastapi.dto;

public class SettingRequestDTO {

    private String storeName;
    private boolean inventoryEnabled;
    private boolean customerEnabled;
    private String databaseName;
    private String connectionString;

    public SettingRequestDTO() {
    }

    public SettingRequestDTO(String storeName, boolean inventoryEnabled, boolean customerEnabled,
                             String databaseName, String connectionString) {
        this.storeName = storeName;
        this.inventoryEnabled = inventoryEnabled;
        this.customerEnabled = customerEnabled;
        this.databaseName = databaseName;
        this.connectionString = connectionString;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
}
