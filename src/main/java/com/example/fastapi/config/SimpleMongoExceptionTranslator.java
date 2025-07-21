package com.example.fastapi.config;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import com.mongodb.MongoException;

public class SimpleMongoExceptionTranslator implements PersistenceExceptionTranslator {

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        if (ex instanceof MongoException) {
            return new org.springframework.data.mongodb.UncategorizedMongoDbException(ex.getMessage(), ex);
        }
        return null;
    }
}
