package com.example.fastapi.config;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoDatabase;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.lang.Nullable;
import com.mongodb.ClientSessionOptions;

public class MultiTenantMongoDbFactoryWithSession implements MongoDatabaseFactory {

    private final MultiTenantMongoDbFactory delegate;
    private final ClientSession session;

    public MultiTenantMongoDbFactoryWithSession(MultiTenantMongoDbFactory delegate, ClientSession session) {
        this.delegate = delegate;
        this.session = session;
    }

    @Override
    public MongoDatabase getMongoDatabase() {
        return delegate.getMongoDatabase();
    }

    @Override
    public MongoDatabase getMongoDatabase(@Nullable String dbName) {
        return delegate.getMongoDatabase(dbName);
    }

    @Override
    public PersistenceExceptionTranslator getExceptionTranslator() {
        return delegate.getExceptionTranslator();
    }

    @Override
    public ClientSession getSession(ClientSessionOptions options) {
        return session; // برمی‌گردونه همون session که پاس دادیم
    }

    @Override
    public MongoDatabaseFactory withSession(ClientSession session) {
        return new MultiTenantMongoDbFactoryWithSession(delegate, session);
    }
}
