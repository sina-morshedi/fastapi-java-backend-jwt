package com.example.fastapi.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Service;
import com.example.fastapi.config.ContextHolder;

import java.util.Optional;

@Service
public class DynamicMongoService {

    private final MongoClient staticClient;
    private final String staticDatabaseName;

    private MongoDatabase currentDatabase;
    private MongoClient dynamicClient;

    public DynamicMongoService() {
        this.staticClient = MongoClients.create("mongodb+srv://sinamorshedi:5aU2xKm9hbuK0AfC@cluster0.ow4in8u.mongodb.net/Setting?retryWrites=true&w=majority&tls=true&tlsAllowInvalidCertificates=true");
        this.staticDatabaseName = "Setting";
    }


    protected String determineDatabaseName() {
        return ContextHolder.getStoreName();
    }

    public Optional<MongoDatabase> getDynamicDatabaseByRepairShop(String repairShopName) {
        try {
            Document settingsDoc = staticClient
                    .getDatabase(staticDatabaseName)
                    .getCollection("settingDb")
                    .find(new Document("storeName", repairShopName))
                    .first();

            if (settingsDoc == null) return Optional.empty();

            String connectionString = settingsDoc.getString("connectionString");
            String databaseName = settingsDoc.getString("databaseName");

            // بستن اتصال قبلی اگر وجود داشته باشه
            if (dynamicClient != null) {
                dynamicClient.close();
            }

            dynamicClient = MongoClients.create(connectionString);
            currentDatabase = dynamicClient.getDatabase(databaseName);

            return Optional.of(currentDatabase);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // ⚠️ متدی که توی پروژه ازش استفاده کردی
    public boolean switchToDatabaseByRepairShop(String repairShopName) {
        Optional<MongoDatabase> optionalDatabase = getDynamicDatabaseByRepairShop(repairShopName);
        return optionalDatabase.map(db -> {
            currentDatabase = db;
            return true;
        }).orElse(false);
    }

    // متدی برای گرفتن دیتابیس فعلی
    public MongoDatabase getCurrentDatabase() {
        return currentDatabase;
    }
}
