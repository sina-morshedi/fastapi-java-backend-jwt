package com.example.fastapi.service;

import com.example.fastapi.dboModel.InventoryItem;
import com.example.fastapi.dto.InventoryChangeRequestDTO;
import com.example.fastapi.repository.InventoryItemRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class InventoryItemService {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;


    public Page<InventoryItem> getActiveInventoryItemsPaged(int page, int size) {
        return inventoryItemRepository.findByIsActiveTrue(PageRequest.of(page, size));
    }
    // افزودن قطعه جدید
    public InventoryItem addItem(InventoryItem item) {
        // Barkod kontrolü
        if (inventoryItemRepository.existsByBarcode(item.getBarcode())) {
            throw new IllegalArgumentException("Aynı barkoda sahip bir parça zaten mevcut.");
        }

        // Parça adı kontrolü
        if (inventoryItemRepository.existsByPartName(item.getPartName())) {
            throw new IllegalArgumentException("Aynı parçaya sahip bir parça zaten mevcut.");
        }

        item.setCreatedAt(new Date());
        item.setUpdatedAt(new Date());
        item.setIsActive(true);

        return inventoryItemRepository.save(item);
    }

    // گرفتن تمام قطعات فعال
    public List<InventoryItem> getAllItems() {
        return inventoryItemRepository.findByIsActiveTrue();
    }

    // گرفتن قطعه بر اساس ID
    public Optional<InventoryItem> getItemById(String id) {
        return inventoryItemRepository.findById(new ObjectId(id));
    }

    // جستجو بر اساس نام (کلمه جزئی)
    public List<InventoryItem> searchByPartName(String keyword) {
        return inventoryItemRepository.findByPartNameContainingIgnoreCase(keyword);
    }

    // به‌روزرسانی اطلاعات قطعه
    public Optional<InventoryItem> updateItem(String id, InventoryItem newItem) {
        Optional<InventoryItem> existingItemOpt = inventoryItemRepository.findById(new ObjectId(id));

        if (existingItemOpt.isPresent()) {
            InventoryItem existing = existingItemOpt.get();

            existing.setPartName(newItem.getPartName());
            existing.setBarcode(newItem.getBarcode());
            existing.setCategory(newItem.getCategory());
            existing.setQuantity(newItem.getQuantity());
            existing.setUnit(newItem.getUnit());
            existing.setLocation(newItem.getLocation());
            existing.setPurchasePrice(newItem.getPurchasePrice());
            existing.setSalePrice(newItem.getSalePrice());
            existing.setIsActive(newItem.getIsActive());
            existing.setUpdatedAt(new Date());

            return Optional.of(inventoryItemRepository.save(existing));
        }

        return Optional.empty();
    }

    // حذف منطقی (غیرفعال کردن قطعه)
    public boolean deactivateItem(String id) {
        Optional<InventoryItem> existingItemOpt = inventoryItemRepository.findById(new ObjectId(id));

        if (existingItemOpt.isPresent()) {
            InventoryItem item = existingItemOpt.get();
            item.setIsActive(false);
            item.setUpdatedAt(new Date());
            inventoryItemRepository.save(item);
            return true;
        }

        return false;
    }

    // جستجو بر اساس بارکد
    public Optional<InventoryItem> getItemByBarcode(String barcode) {
        return inventoryItemRepository.findByBarcode(barcode);
    }

    public boolean deleteItem(String id) {
        Optional<InventoryItem> itemOpt = inventoryItemRepository.findById(new ObjectId(id));

        if (itemOpt.isPresent()) {
            inventoryItemRepository.deleteById(new ObjectId(id));
            return true;
        }
        return false;
    }

    public Optional<InventoryItem> decrementQuantity(InventoryChangeRequestDTO request) {
        String itemId = request.getItemId();
        int decrementAmount = request.getAmount();
        Date date = request.getUpdatedAt();

        Optional<InventoryItem> itemOpt = inventoryItemRepository.findById(new ObjectId(itemId));
        if (itemOpt.isEmpty()) {
            return Optional.empty(); // قطعه پیدا نشد
        }

        InventoryItem item = itemOpt.get();

        int currentQuantity = item.getQuantity() != null ? item.getQuantity() : 0;
        if (currentQuantity < decrementAmount) {
            return Optional.empty(); // موجودی کافی نیست
        }

        item.setQuantity(currentQuantity - decrementAmount);

        item.setUpdatedAt(date);
        inventoryItemRepository.save(item);

        return Optional.of(item);
    }

    public Optional<InventoryItem> incrementQuantity(InventoryChangeRequestDTO request) {
        String itemId = request.getItemId();
        int incrementAmount = request.getAmount();
        Date updatedAt = request.getUpdatedAt();

        Optional<InventoryItem> itemOpt = inventoryItemRepository.findById(new ObjectId(itemId));
        if (itemOpt.isEmpty()) {
            return Optional.empty(); // قطعه پیدا نشد
        }

        InventoryItem item = itemOpt.get();

        int currentQuantity = item.getQuantity() != null ? item.getQuantity() : 0;
        item.setQuantity(currentQuantity + incrementAmount);

        // اگر قیمت خرید موجود بود، به‌روزرسانی شود
        if (request.getPurchasePrice() != null) {
            item.setPurchasePrice(request.getPurchasePrice());
        }

        // اگر قیمت فروش موجود بود، به‌روزرسانی شود
        if (request.getSalePrice() != null) {
            item.setSalePrice(request.getSalePrice());
        }

        item.setUpdatedAt(updatedAt);

        inventoryItemRepository.save(item);

        return Optional.of(item);
    }


}
