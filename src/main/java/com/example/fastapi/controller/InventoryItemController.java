package com.example.fastapi.controller;

import com.example.fastapi.config.ContextHolder;
import com.example.fastapi.dboModel.InventoryItem;
import com.example.fastapi.dto.InventoryChangeRequestDTO;
import com.example.fastapi.service.InventoryItemService;
import com.example.fastapi.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/inventory")
public class InventoryItemController {

    @Autowired
    private InventoryItemService inventoryItemService;

    @Autowired
    private JwtService jwtService;

    private ResponseEntity<Object> unauthorizedResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    private ResponseEntity<Object> invalidTokenResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    @GetMapping("/inventory-items")
    public ResponseEntity<Object> getInventoryItems(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Page<InventoryItem> items = inventoryItemService.getActiveInventoryItemsPaged(page, size);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(items);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Internal server error.");
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/next-barcode")
    public ResponseEntity<Object> getNextBarcodeStartingWith(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam String prefix) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            String nextBarcode = inventoryItemService.generateNextBarcode(prefix);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(nextBarcode);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Internal server error.");
        } finally {
            ContextHolder.clear();
        }
    }


    @PostMapping("/add")
    public ResponseEntity<Object> addInventoryItem(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody InventoryItem item) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            InventoryItem savedItem = inventoryItemService.addItem(item);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(savedItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(Map.of("error", "Internal server error"));
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getAllItems(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<InventoryItem> items = inventoryItemService.getAllItems();

            if (items == null || items.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Depoda hiç parça bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(items);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<InventoryItem> itemOpt = inventoryItemService.getItemById(id);

            if (itemOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Parça bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(itemOpt.get());
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/by-part-name")
    public ResponseEntity<Object> getItemByPartName(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam("partName") String partName) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<InventoryItem> itemOpt = inventoryItemService.getItemByName(partName);

            if (itemOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Parça bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(itemOpt.get());
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchByPartName(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam String keyword) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<InventoryItem> items = inventoryItemService.searchByPartName(keyword);

            if (items == null || items.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Aradığınız isimde parça bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(items);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/barcode")
    public ResponseEntity<Object> getItemByBarcode(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam String barcode) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<InventoryItem> itemOpt = inventoryItemService.getItemByBarcode(barcode);

            if (itemOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Barkod ile parça bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(itemOpt.get());
        } finally {
            ContextHolder.clear();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateItem(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id,
            @RequestBody InventoryItem updatedItem) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<InventoryItem> updated = inventoryItemService.updateItem(id, updatedItem);

            if (updated.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Güncellenecek parça bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("İlgili parçanın bilgileri güncellendi.");
        } finally {
            ContextHolder.clear();
        }
    }

    @DeleteMapping("/deactivate/{id}")
    public ResponseEntity<Object> deactivateItem(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            boolean success = inventoryItemService.deactivateItem(id);

            if (!success) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Bu parça pasif hale getirilmiştir, stoktan çıkış yapılamaz.");
            }

            return ResponseEntity.noContent().build();
        } finally {
            ContextHolder.clear();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteItem(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<InventoryItem> itemOpt = inventoryItemService.getItemById(id);

            if (itemOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Silinecek parça bulunamadı.");
            }

            inventoryItemService.deleteItem(id);

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Parça başarıyla silindi.");
        } finally {
            ContextHolder.clear();
        }
    }

    @PostMapping("/decrementQuantity")
    public ResponseEntity<Object> decrementQuantity(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody InventoryChangeRequestDTO request) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            String itemId = request.getItemId();
            int decrementAmount = request.getAmount();

            if (itemId == null || decrementAmount <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Geçersiz itemId veya decrementAmount");
            }

            Optional<InventoryItem> updatedItem = inventoryItemService.decrementQuantity(request);

            if (updatedItem.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Parça bulunamadı veya yeterli stok yok");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("İlgili parça stoktan düşüldü.");
        } finally {
            ContextHolder.clear();
        }
    }

    @PostMapping("/incrementQuantity")
    public ResponseEntity<Object> incrementQuantity(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody InventoryChangeRequestDTO request) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            String itemId = request.getItemId();
            int incrementAmount = request.getAmount();

            if (itemId == null || incrementAmount <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Geçersiz itemId veya incrementAmount");
            }

            Optional<InventoryItem> updatedItem = inventoryItemService.incrementQuantity(request);

            if (updatedItem.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Parça bulunamadı");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("İlgili parça stoğa eklendi.");
        } finally {
            ContextHolder.clear();
        }
    }
}
