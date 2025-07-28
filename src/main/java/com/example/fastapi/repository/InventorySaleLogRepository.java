package com.example.fastapi.repository;

import com.example.fastapi.dboModel.InventorySaleLog;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventorySaleLogRepository extends MongoRepository<InventorySaleLog, String> {

    // اگر بخوای بر اساس نام مشتری جستجو کنی:
    List<InventorySaleLog> findByCustomerName(String customerName);

    // جستجو بر اساس محدوده تاریخ فروش:
    List<InventorySaleLog> findBySaleDateBetween(java.util.Date startDate, java.util.Date endDate);

    // در صورت نیاز متدهای سفارشی دیگر هم می‌توانی اضافه کنی
}
