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
        InventoryTransactionLog entity = InventoryTransactionMapper.toEntity(dto);

        // زمان فعلی سرور در تایمزون استانبول
        ZoneId istanbulZone = ZoneId.of("Europe/Istanbul");
        LocalDateTime nowInIstanbul = LocalDateTime.now(istanbulZone);

        entity.setDateTime(nowInIstanbul);

        return repository.save(entity);
    }

    // گرفتن همه تراکنش‌ها
    public List<InventoryTransactionResponseDTO> getAllTransactions() {
        return inventoryTransactionLogCustomRepositoryImpl.findAllTransactions();
    }


    public List<InventoryTransactionResponseDTO> findAllTransactionsPaginated(int page, int size) {
        return inventoryTransactionLogCustomRepositoryImpl.findAllTransactionsPaginated(page, size);
    }

    public List<InventoryTransactionResponseDTO> findTransactionsByDateRange(String startDateStr, String endDateStr) {
        ZonedDateTime startUtc = ZonedDateTime.parse(startDateStr);
        ZonedDateTime endUtc = ZonedDateTime.parse(endDateStr);

        // 2. تبدیل زمان UTC به منطقه زمانی ترکیه
        ZoneId istanbulZone = ZoneId.of("Europe/Istanbul");
        ZonedDateTime startIstanbul = startUtc.withZoneSameInstant(istanbulZone);
        ZonedDateTime endIstanbul = endUtc.withZoneSameInstant(istanbulZone);

        // 3. حالا اگر دیتابیس با LocalDateTime کار می‌کند، می‌توانیم LocalDateTime بگیریم
        LocalDateTime startLocal = startIstanbul.toLocalDateTime();
        LocalDateTime endLocal = endIstanbul.toLocalDateTime();
        return inventoryTransactionLogCustomRepositoryImpl.findTransactionsByDateRange(startLocal, endLocal);
    }

    public List<InventoryTransactionResponseDTO> findTransactionsByDateRangePaginated(
            String startDateStr,
            String endDateStr,
            int page,
            int size
    ) {
        // 1. پارس رشته‌های ورودی به ZonedDateTime یا OffsetDateTime (با فرض فرمت ISO8601 و زون UTC)
        ZonedDateTime startUtc = ZonedDateTime.parse(startDateStr);
        ZonedDateTime endUtc = ZonedDateTime.parse(endDateStr);

        // 2. تبدیل زمان UTC به منطقه زمانی ترکیه
        ZoneId istanbulZone = ZoneId.of("Europe/Istanbul");
        ZonedDateTime startIstanbul = startUtc.withZoneSameInstant(istanbulZone);
        ZonedDateTime endIstanbul = endUtc.withZoneSameInstant(istanbulZone);

        // 3. حالا اگر دیتابیس با LocalDateTime کار می‌کند، می‌توانیم LocalDateTime بگیریم
        LocalDateTime startLocal = startIstanbul.toLocalDateTime();
        LocalDateTime endLocal = endIstanbul.toLocalDateTime();

        // 4. کال کردن متد ریپازیتوری با بازه زمانی دقیق
        List<InventoryTransactionResponseDTO> results = inventoryTransactionLogCustomRepositoryImpl.findTransactionsByDateRangePaginated(
                startLocal, endLocal, page, size);

        return results;
    }

    public List<InventoryTransactionResponseDTO> getTransactionsByType(String type) {
        return inventoryTransactionLogCustomRepositoryImpl.findTransactionsByType(type);
    }

    public InventoryTransactionResponseDTO getLastTransactionByCustomerName(String fullName) {
        return inventoryTransactionLogCustomRepositoryImpl.findLastTransactionByCustomerFullName(fullName);
    }

    public List<InventoryTransactionResponseDTO> getTransactionsByCustomerName(String fullName) {
        return inventoryTransactionLogCustomRepositoryImpl.findByCustomerFullName(fullName);
    }

    public boolean deleteTransactionById(String id) {
        if (!repository.existsById(new ObjectId(id))) {
            return false;  // آیتم وجود نداره
        }
        repository.deleteById(new ObjectId(id));
        return true;  // حذف با موفقیت انجام شد
    }

}
