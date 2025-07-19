package com.example.fastapi.repository;

import com.example.fastapi.dboModel.InventoryTransactionLog;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryTransactionLogRepository
        extends MongoRepository<InventoryTransactionLog, ObjectId>,
        InventoryTransactionLogCustomRepository {
    // اینجا نیاز نیست متدی اضافه کنی
}
