package com.example.fastapi.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.example.fastapi.dboModel.Setting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class MongoConfig {

    private final String centralDbName = "Setting";
    private final String centralConnectionString = "mongodb+srv://sinamorshedi:5aU2xKm9hbuK0AfC@cluster0.ow4in8u.mongodb.net/" + centralDbName + "?retryWrites=true&w=majority&tls=true&tlsAllowInvalidCertificates=true";

    public MongoConfig() {
        System.out.println("MongoConfig loaded!");
    }

    @Bean
    public MongoClient centralMongoClient() {
        return MongoClients.create(centralConnectionString);
    }

    @Bean
    public MongoTemplate centralMongoTemplate(MongoClient centralMongoClient) {
        return new MongoTemplate(centralMongoClient, centralDbName);
    }

    @Bean
    public MultiTenantMongoDbFactory multiTenantMongoDbFactory(MongoTemplate centralMongoTemplate) {
        try {
            List<Setting> settings = centralMongoTemplate.findAll(Setting.class, "settings");

            Map<String, MongoClient> clientMap = new HashMap<>();
            Map<String, String> dbNameMap = new HashMap<>();

            for (Setting setting : settings) {
                MongoClient client = MongoClients.create(setting.getConnectionString());
                clientMap.put(setting.getStoreName(), client);
                dbNameMap.put(setting.getStoreName(), setting.getDatabaseName());
            }

            System.out.println("Loaded tenants:");
            clientMap.keySet().forEach(store -> {
                System.out.println("Store: " + store + ", DB Name: " + dbNameMap.get(store));
            });

            return new MultiTenantMongoDbFactory(clientMap, dbNameMap);

        } catch (Exception ex) {
            System.err.println("Error loading tenant settings: " + ex.getMessage());
            throw new RuntimeException("Cannot initialize MultiTenantMongoDbFactory", ex);
        }
    }

    @Bean
    public MongoTemplate mongoTemplate(MultiTenantMongoDbFactory factory) {
        return new MongoTemplate(factory);
    }
}
