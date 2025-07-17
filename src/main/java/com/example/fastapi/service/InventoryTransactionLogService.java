package com.example.fastapi.service;

import com.example.fastapi.dboModel.InventoryTransactionLog;
import com.example.fastapi.repository.InventoryTransactionLogRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryTransactionLogService {

    private final InventoryTransactionLogRepository repository;

    public InventoryTransactionLogService(InventoryTransactionLogRepository repository) {
        this.repository = repository;
    }

    // اضافه کردن تراکنش جدید
    public InventoryTransactionLog addTransaction(InventoryTransactionLog transactionLog) {
        return repository.save(transactionLog);
    }

    // گرفتن همه تراکنش‌ها
    public List<InventoryTransactionLog> getAllTransactions() {
        return repository.findAll();
    }

    // گرفتن تراکنش بر اساس آیدی
    public Optional<InventoryTransactionLog> getTransactionById(String id) {
        return repository.findById(new ObjectId(id));
    }

    // گرفتن تراکنش‌ها بر اساس ماشین
    public List<InventoryTransactionLog> getTransactionsByCarInfoId(ObjectId carInfoId) {
        return repository.findByCarInfoId(carInfoId);
    }

    // گرفتن تراکنش‌ها بر اساس نوع تراکنش
    public List<InventoryTransactionLog> getTransactionsByType(InventoryTransactionLog.TransactionType type) {
        return repository.findByType(type);
    }

    // حذف تراکنش بر اساس آیدی
    public boolean deleteTransactionById(String id) {
        if (!repository.existsById(new ObjectId(id))) {
            return false;  // آیتم وجود نداره
        }
        repository.deleteById(new ObjectId(id));
        return true;  // حذف با موفقیت انجام شد
    }

}
