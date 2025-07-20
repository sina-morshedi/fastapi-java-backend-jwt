package com.example.fastapi.service;

import com.example.fastapi.dboModel.InventoryTransactionLog;
import com.example.fastapi.dto.InventoryTransactionRequestDTO;
import com.example.fastapi.dto.InventoryTransactionResponseDTO;
import com.example.fastapi.repository.InventoryTransactionLogRepository;
import com.example.fastapi.repository.InventoryTransactionLogCustomRepositoryImpl;
import com.example.fastapi.mappers.InventoryTransactionMapper;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.Instant;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Date;

@Service
public class InventoryTransactionLogService {

    private final InventoryTransactionLogRepository repository;

    private final InventoryTransactionLogCustomRepositoryImpl inventoryTransactionLogCustomRepositoryImpl;

    public InventoryTransactionLogService(InventoryTransactionLogRepository repository,
                                          InventoryTransactionLogCustomRepositoryImpl inventoryTransactionLogCustomRepositoryImpl) {
        this.repository = repository;
        this.inventoryTransactionLogCustomRepositoryImpl = inventoryTransactionLogCustomRepositoryImpl;
    }

    // اضافه کردن تراکنش جدید
    public InventoryTransactionLog addTransaction(InventoryTransactionRequestDTO dto) {
        System.out.println("Received DTO: " + dto.toString());
        InventoryTransactionLog entity = InventoryTransactionMapper.toEntity(dto);
        return repository.save(entity);
    }


    // گرفتن همه تراکنش‌ها
    public List<InventoryTransactionResponseDTO> getAllTransactions() {
        return inventoryTransactionLogCustomRepositoryImpl.findAllTransactions();
    }


    public List<InventoryTransactionResponseDTO> findAllTransactionsPaginated(int page, int size) {
        return inventoryTransactionLogCustomRepositoryImpl.findAllTransactionsPaginated(page, size);
    }


//    public List<InventoryTransactionResponseDTO> findTransactionsByDateRangePaginated(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
//        return inventoryTransactionLogCustomRepositoryImpl.findTransactionsByDateRangePaginated(startDate, endDate, page, size);
//    }
public List<InventoryTransactionResponseDTO> findTransactionsByDateRangePaginated(
        String startDateStr,
        String endDateStr,
        int page,
        int size
) {
    // تبدیل رشته به LocalDate
    LocalDate startDate = LocalDate.parse(startDateStr);
    LocalDate endDate = LocalDate.parse(endDateStr);

    // منطقه زمانی ترکیه
    ZoneId zoneId = ZoneId.of("Europe/Istanbul");

    // ساخت ZonedDateTime برای شروع و پایان روز با منطقه زمانی ترکیه
    ZonedDateTime startZdt = startDate.atStartOfDay(zoneId);
    ZonedDateTime endZdt = endDate.atTime(LocalTime.MAX).atZone(zoneId);

    // تبدیل ZonedDateTime به LocalDateTime برای استفاده در اگریگیشن
    LocalDateTime startLocalDateTime = startZdt.toLocalDateTime();
    LocalDateTime endLocalDateTime = endZdt.toLocalDateTime();

    // حالا متد اصلی اگریگیشن رو صدا بزن
    List<InventoryTransactionResponseDTO> results = inventoryTransactionLogCustomRepositoryImpl.findTransactionsByDateRangePaginated(
            startLocalDateTime, endLocalDateTime, page, size);

    return results;
}

//
//    // گرفتن تراکنش بر اساس آیدی
//    public Optional<InventoryTransactionLog> getTransactionById(String id) {
//        return repository.findById(new ObjectId(id));
//    }

    // گرفتن تراکنش‌ها بر اساس ماشین
//    public List<InventoryTransactionLog> getTransactionsByCarInfoId(ObjectId carInfoId) {
//        return repository.findByCarInfoId(carInfoId);
//    }
//
//    // گرفتن تراکنش‌ها بر اساس نوع تراکنش
//    public List<InventoryTransactionLog> getTransactionsByType(InventoryTransactionLog.TransactionType type) {
//        return repository.findByType(type);
//    }
//
//    // حذف تراکنش بر اساس آیدی
//    public boolean deleteTransactionById(String id) {
//        if (!repository.existsById(new ObjectId(id))) {
//            return false;  // آیتم وجود نداره
//        }
//        repository.deleteById(new ObjectId(id));
//        return true;  // حذف با موفقیت انجام شد
//    }

}
