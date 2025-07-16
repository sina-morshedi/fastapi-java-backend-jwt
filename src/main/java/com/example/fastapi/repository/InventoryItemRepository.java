package com.example.fastapi.repository;

import com.example.fastapi.dboModel.InventoryItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface InventoryItemRepository extends MongoRepository<InventoryItem, ObjectId> {

    // پیدا کردن با بارکد (برای چک‌کردن یکتایی)
    Optional<InventoryItem> findByBarcode(String barcode);

    // فقط آیتم‌های فعال
    List<InventoryItem> findByIsActiveTrue();

    // جستجو بر اساس نام قطعه (مثلاً برای فیلتر در فرانت)
    List<InventoryItem> findByPartNameContainingIgnoreCase(String partName);

    // آیتم‌هایی که تعدادشون کمتر از مقدار مشخصه (مثلاً هشدار کم بودن موجودی)
    List<InventoryItem> findByQuantityLessThan(int threshold);
}
