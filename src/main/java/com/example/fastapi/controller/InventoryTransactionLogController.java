package com.example.fastapi.controller;

import com.example.fastapi.config.ContextHolder;
import com.example.fastapi.dboModel.InventoryTransactionLog;
import com.example.fastapi.dto.InventoryTransactionRequestDTO;
import com.example.fastapi.dto.InventoryTransactionResponseDTO;
import com.example.fastapi.service.InventoryTransactionLogService;
import com.example.fastapi.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventoryTransaction")
public class InventoryTransactionLogController {

    @Autowired
    private InventoryTransactionLogService inventoryTransactionLogService;

    @Autowired
    private JwtService jwtService;

    private boolean isTokenInvalid(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ") || !jwtService.validateToken(authHeader.substring(7));
    }

    private boolean prepareContext(String authHeader) {
        if (isTokenInvalid(authHeader)) return false;
        String token = authHeader.substring(7);
        String storeName = jwtService.getStoreNameFromToken(token);
        ContextHolder.setStoreName(storeName);
        return true;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addTransaction(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody InventoryTransactionRequestDTO transaction) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

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
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getAllTransactions(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
            List<InventoryTransactionResponseDTO> transactions = inventoryTransactionLogService.getAllTransactions();

            if (transactions == null || transactions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Hiç işlem bulunamadı");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(transactions);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/paged")
    public ResponseEntity<Object> getAllTransactionsPaged(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
            List<InventoryTransactionResponseDTO> transactions = inventoryTransactionLogService.findAllTransactionsPaginated(page, size);

            if (transactions == null || transactions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Hiç işlem bulunamadı");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(transactions);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/date-range")
    public ResponseEntity<Object> getTransactionsByDateRange(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
            List<InventoryTransactionResponseDTO> transactions =
                    inventoryTransactionLogService.findTransactionsByDateRangePaginated(startDate, endDate, page, size);

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
        } finally {
            ContextHolder.clear();
        }
    }
}
