package com.example.fastapi.controller;

import com.example.fastapi.dboModel.InventoryTransactionLog;
import com.example.fastapi.dto.InventoryTransactionRequestDTO;
import com.example.fastapi.dto.InventoryTransactionResponseDTO;
import com.example.fastapi.service.InventoryTransactionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


@RestController
@RequestMapping("/inventoryTransaction")
public class InventoryTransactionLogController {

    @Autowired
    private InventoryTransactionLogService inventoryTransactionLogService;

    // Yeni işlem ekle
    @PostMapping("/add")
    public ResponseEntity<Object> addTransaction(@RequestBody InventoryTransactionRequestDTO transaction) {
        try {
            InventoryTransactionLog savedTransaction = inventoryTransactionLogService.addTransaction(transaction);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Parça çıkışı başarıyla kaydedildi.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(Map.of("error", "Sunucu hatası"));
        }
    }

    // Tüm işlemleri listele
    @GetMapping("/list")
    public ResponseEntity<Object> getAllTransactions() {
        List<InventoryTransactionResponseDTO> transactions = inventoryTransactionLogService.getAllTransactions();

        if (transactions == null || transactions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Hiç işlem bulunamadı");
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(transactions);
    }

    @GetMapping("/paged")
    public ResponseEntity<Object> getAllTransactionsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<InventoryTransactionResponseDTO> transactions = inventoryTransactionLogService.findAllTransactionsPaginated(page, size);

        if (transactions == null || transactions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Hiç işlem bulunamadı");
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(transactions);
    }

    @GetMapping("/date-range")
    public ResponseEntity<Object> getTransactionsByDateRange(
            @RequestParam String startDate,   // مثلا "2025-07-20"
            @RequestParam String endDate,     // مثلا "2025-07-22"
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            // متد سرویس ورودی رشته رو تبدیل میکنه و عملیات رو انجام میده
            List<InventoryTransactionResponseDTO> transactions =
                    inventoryTransactionLogService.findTransactionsByDateRangePaginated(
                            startDate, endDate, page, size);

            if (transactions == null || transactions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Belirtilen tarih aralığında işlem bulunamadı");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(transactions);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Geçersiz tarih formatı. Format: yyyy-MM-dd");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Bir hata oluştu: " + e.getMessage());
        }
    }




    // ID ile işlem getir
//    @GetMapping("/{id}")
//    public ResponseEntity<Object> getTransactionById(@PathVariable String id) {
//        Optional<InventoryTransactionLog> transactionOpt = inventoryTransactionLogService.getTransactionById(id);
//
//        if (transactionOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .header("Content-Type", "application/json; charset=UTF-8")
//                    .body("İşlem bulunamadı");
//        }
//
//        return ResponseEntity.ok()
//                .header("Content-Type", "application/json; charset=UTF-8")
//                .body(transactionOpt.get());
//    }
//
//    // İşlem sil
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Object> deleteTransaction(@PathVariable String id) {
//        boolean deleted = inventoryTransactionLogService.deleteTransactionById(id);
//
//        if (!deleted) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .header("Content-Type", "application/json; charset=UTF-8")
//                    .body("Silinecek işlem bulunamadı");
//        }
//
//        return ResponseEntity.ok()
//                .header("Content-Type", "application/json; charset=UTF-8")
//                .body("İşlem başarıyla silindi");
//    }
//
//
//    // İşlem türüne göre arama
//    @GetMapping("/searchByType")
//    public ResponseEntity<Object> searchByType(@RequestParam InventoryTransactionLog.TransactionType type) {
//        List<InventoryTransactionLog> transactions = inventoryTransactionLogService.getTransactionsByType(type);
//
//        if (transactions == null || transactions.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .header("Content-Type", "application/json; charset=UTF-8")
//                    .body("Bu türde işlem bulunamadı");
//        }
//
//        return ResponseEntity.ok()
//                .header("Content-Type", "application/json; charset=UTF-8")
//                .body(transactions);
//    }

}
