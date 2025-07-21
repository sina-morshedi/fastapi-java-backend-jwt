package com.example.fastapi.config;

import com.mongodb.ClientSessionOptions;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import com.example.fastapi.config.SimpleMongoExceptionTranslator;
import org.springframework.lang.Nullable;

import java.util.Map;

public class MultiTenantMongoDbFactory implements MongoDatabaseFactory {

    private final Map<String, MongoClient> clientMap;
    private final Map<String, String> databaseNameMap;

    public MultiTenantMongoDbFactory(Map<String, MongoClient> clientMap, Map<String, String> databaseNameMap) {
        this.clientMap = clientMap;
        this.databaseNameMap = databaseNameMap;
    }

    @Override
    public MongoDatabase getMongoDatabase() {
        String storeName = ContextHolder.getStoreName();
        if (storeName == null) {
            throw new IllegalStateException("storeName not set in ContextHolder");
        }

        MongoClient client = clientMap.get(storeName);
        if (client == null) {
            throw new IllegalStateException("MongoClient not found for store: " + storeName);
        }

        String dbName = databaseNameMap.get(storeName);
        if (dbName == null) {
            throw new IllegalStateException("Database name not found for store: " + storeName);
        }

        return client.getDatabase(dbName);
    }

    @Override
    public MongoDatabase getMongoDatabase(@Nullable String dbName) {
        throw new UnsupportedOperationException("Use getMongoDatabase() without parameters");
    }

    @Override
    public PersistenceExceptionTranslator getExceptionTranslator() {
        return new SimpleMongoExceptionTranslator();
    }


    @Override
    public ClientSession getSession(ClientSessionOptions options) {
        String storeName = ContextHolder.getStoreName();
        if (storeName == null) {
            throw new IllegalStateException("storeName not set in ContextHolder");
        }

        MongoClient client = clientMap.get(storeName);
        if (client == null) {
            throw new IllegalStateException("MongoClient not found for store: " + storeName);
        }

        return client.startSession(options);
    }

    @Override
    public MongoDatabaseFactory withSession(ClientSession session) {
        return new MultiTenantMongoDbFactoryWithSession(this, session);
    }
}
