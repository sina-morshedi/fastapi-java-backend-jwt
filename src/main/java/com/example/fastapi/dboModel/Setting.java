package com.example.fastapi.dboModel;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "settings")
public class Setting {

    @Id
    private ObjectId id;

    private String storeName;

    private boolean inventoryEnabled;

    private boolean customerEnabled;

    private String databaseName;

    // Optional: connection string for that store's database
    private String connectionString;

    // اگر فعلا نیاز نیست، حذف کن
    // private ObjectId createdByUserId;
    // private Date createdDateTime;

    // Getters & Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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
