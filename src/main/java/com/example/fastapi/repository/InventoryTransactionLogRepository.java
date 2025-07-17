package com.example.fastapi.repository;

import com.example.fastapi.dboModel.InventoryTransactionLog;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface InventoryTransactionLogRepository extends MongoRepository<InventoryTransactionLog, ObjectId> {

    // اگر لازم داری جستجوهای خاص مثل بر اساس نوع تراکنش یا ماشین یا کاربر انجام بدی، اینجا میتونی متد اضافه کنی:

    List<InventoryTransactionLog> findByCarInfoId(Object carInfoId);

    List<InventoryTransactionLog> findByCustomerId(Object customerId);

    List<InventoryTransactionLog> findByUserId(Object userId);

    List<InventoryTransactionLog> findByType(InventoryTransactionLog.TransactionType type);

    // یا ترکیب فیلدها:
    List<InventoryTransactionLog> findByCarInfoIdAndType(Object carInfoId, InventoryTransactionLog.TransactionType type);

}
