package com.example.fastapi.controller;

import com.example.fastapi.dboModel.InventoryItem;
import com.example.fastapi.service.InventoryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import com.example.fastapi.dto.*;

@RestController
@RequestMapping("/inventory")
public class InventoryItemController {

    @Autowired
    private InventoryItemService inventoryItemService;

    // افزودن قطعه جدید
    @PostMapping("/add")
    public ResponseEntity<Object> addInventoryItem(@RequestBody InventoryItem item) {
        try {
            InventoryItem savedItem = inventoryItemService.addItem(item);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(savedItem);
        } catch (IllegalArgumentException e) {
            // در صورت خطای تکراری، پاسخ مناسب با کد وضعیت 400 (Bad Request) برگردان
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(e.getMessage());
        } catch (Exception e) {
            // برای سایر خطاها می‌توانی پاسخ عمومی بدهی
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(Map.of("error", "Internal server error"));
        }
    }


    // گرفتن لیست تمام قطعات فعال
    @GetMapping("/list")
    public ResponseEntity<Object> getAllItems() {
        List<InventoryItem> items = inventoryItemService.getAllItems();

        if (items == null || items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Depoda hiç parça bulunamadı."); // هیچ قطعه‌ای در انبار پیدا نشد
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(items);
    }

    // گرفتن قطعه بر اساس ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable String id) {
        Optional<InventoryItem> itemOpt = inventoryItemService.getItemById(id);

        if (itemOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Parça bulunamadı."); // قطعه پیدا نشد
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(itemOpt.get());
    }

    // جستجو بر اساس نام قطعه
    @GetMapping("/search")
    public ResponseEntity<Object> searchByPartName(@RequestParam String keyword) {
        List<InventoryItem> items = inventoryItemService.searchByPartName(keyword);

        if (items == null || items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Aradığınız isimde parça bulunamadı."); // قطعه‌ای با این نام پیدا نشد
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(items);
    }

    // جستجو بر اساس بارکد
    @GetMapping("/barcode")
    public ResponseEntity<Object> getItemByBarcode(@RequestParam String barcode) {
        Optional<InventoryItem> itemOpt = inventoryItemService.getItemByBarcode(barcode);

        if (itemOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Barkod ile parça bulunamadı."); // قطعه با این بارکد پیدا نشد
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(itemOpt.get());
    }

    // به‌روزرسانی قطعه
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateItem(@PathVariable String id, @RequestBody InventoryItem updatedItem) {
        Optional<InventoryItem> updated = inventoryItemService.updateItem(id, updatedItem);

        if (updated.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Güncellenecek parça bulunamadı."); // قطعه‌ای برای بروزرسانی پیدا نشد
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(updated.get());
    }

    // حذف منطقی (غیرفعال کردن قطعه)
    @DeleteMapping("/deactivate/{id}")
    public ResponseEntity<Object> deactivateItem(@PathVariable String id) {
        boolean success = inventoryItemService.deactivateItem(id);

        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Silinecek parça bulunamadı."); // قطعه‌ای برای حذف پیدا نشد
        }

        return ResponseEntity.noContent().build();
    }

    // حذف کامل یک قطعه (delete واقعی از دیتابیس)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable String id) {
        Optional<InventoryItem> itemOpt = inventoryItemService.getItemById(id);

        if (itemOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Silinecek parça bulunamadı."); // قطعه‌ای برای حذف پیدا نشد
        }

        inventoryItemService.deleteItem(id);

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body("Parça başarıyla silindi."); // قطعه با موفقیت حذف شد
    }

    // کاهش تعداد قطعه
    @PostMapping("/decrementQuantity")
    public ResponseEntity<Object> decrementQuantity(@RequestBody InventoryQuantityChangeRequestDTO request) {
        String itemId = request.getItemId();
        int decrementAmount = request.getAmount();

        if (itemId == null || decrementAmount <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Geçersiz itemId veya decrementAmount");
        }

        Optional<InventoryItem> updatedItem = inventoryItemService.decrementQuantity(itemId, decrementAmount);

        if (updatedItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Parça bulunamadı veya yeterli stok yok");
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(updatedItem.get());
    }



    @PostMapping("/incrementQuantity")
    public ResponseEntity<Object> incrementQuantity(@RequestBody InventoryQuantityChangeRequestDTO request) {
        String itemId = request.getItemId();
        int incrementAmount = request.getAmount();

        if (itemId == null || incrementAmount <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Geçersiz itemId veya incrementAmount"); // ورودی نامعتبر
        }

        Optional<InventoryItem> updatedItem = inventoryItemService.incrementQuantity(itemId, incrementAmount);

        if (updatedItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Parça bulunamadı"); // قطعه پیدا نشد
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(updatedItem.get());
    }

}
