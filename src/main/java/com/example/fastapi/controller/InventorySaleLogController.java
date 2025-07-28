package com.example.fastapi.controller;

import com.example.fastapi.dboModel.InventorySaleLog;
import com.example.fastapi.service.InventorySaleLogService;
import com.example.fastapi.service.JwtService;
import com.example.fastapi.config.ContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/inventorySaleLogs")
public class InventorySaleLogController {

    private final InventorySaleLogService service;
    private final JwtService jwtService;

    @Autowired
    public InventorySaleLogController(InventorySaleLogService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    private ResponseEntity<Object> unauthorizedResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("Content-Type", "application/json; charset=UTF-8")
                .body("Yetkisiz erişim");
    }

    private ResponseEntity<Object> invalidTokenResponse() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .header("Content-Type", "application/json; charset=UTF-8")
                .body("Geçersiz token");
    }

    private ResponseEntity<Object> internalServerErrorResponse() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Content-Type", "application/json; charset=UTF-8")
                .body("Sunucu hatası");
    }

    private ResponseEntity<Object> notFoundResponse() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("Content-Type", "application/json; charset=UTF-8")
                .body("Bulunamadı");
    }

    // ثبت یا به‌روزرسانی لاگ فروش
    @PostMapping("/saveLog")
    public ResponseEntity<Object> saveSaleLog(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody InventorySaleLog saleLog) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            InventorySaleLog saved = service.save(saleLog);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(saved);
        } catch (Exception e) {
            return internalServerErrorResponse();
        } finally {
            ContextHolder.clear();
        }
    }

    // دریافت لاگ فروش بر اساس آیدی
    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getSaleLogById(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<InventorySaleLog> saleLog = service.findById(id);
            if (saleLog.isPresent()) {
                return ResponseEntity.ok()
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body(saleLog.get());
            } else {
                return notFoundResponse();
            }
        } catch (Exception e) {
            return internalServerErrorResponse();
        } finally {
            ContextHolder.clear();
        }
    }

    // حذف لاگ فروش بر اساس آیدی
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteSaleLog(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return internalServerErrorResponse();
        } finally {
            ContextHolder.clear();
        }
    }

    // گرفتن همه لاگ‌ها
    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllSaleLogs(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<InventorySaleLog> allLogs = service.findAll();
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(allLogs);
        } catch (Exception e) {
            return internalServerErrorResponse();
        } finally {
            ContextHolder.clear();
        }
    }

    // جستجو بر اساس نام مشتری
    @GetMapping("/searchByCustomer")
    public ResponseEntity<Object> getByCustomerName(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam String customerName) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<InventorySaleLog> results = service.findByCustomerName(customerName);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(results);
        } catch (Exception e) {
            return internalServerErrorResponse();
        } finally {
            ContextHolder.clear();
        }
    }

    // جستجو بر اساس بازه تاریخ فروش
    @GetMapping("/searchByDate")
    public ResponseEntity<Object> getBySaleDateRange(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<InventorySaleLog> results = service.findBySaleDateBetween(startDate, endDate);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(results);
        } catch (Exception e) {
            return internalServerErrorResponse();
        } finally {
            ContextHolder.clear();
        }
    }
}
