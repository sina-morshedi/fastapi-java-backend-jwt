package com.example.fastapi.repository;

import com.example.fastapi.dboModel.Setting;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettingRepository extends MongoRepository<Setting, ObjectId> {

    // پیدا کردن تنظیمات بر اساس نام فروشگاه
    Optional<Setting> findByStoreName(String storeName);

    // چک کردن وجود تنظیمات برای یک فروشگاه خاص
    boolean existsByStoreName(String storeName);

    // حذف تنظیمات بر اساس نام فروشگاه (اگر لازم بود)
    void deleteByStoreName(String storeName);

    // پیدا کردن همه تنظیمات فعال (مثلاً انبار فعال)
    List<Setting> findByInventoryEnabledTrue();

    // پیدا کردن همه تنظیمات که مشتری فعال دارند
    List<Setting> findByCustomerEnabledTrue();

}
