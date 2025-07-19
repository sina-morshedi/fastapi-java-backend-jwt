package com.example.fastapi.repository;

import com.example.fastapi.dboModel.InventoryItem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface InventoryItemRepository extends MongoRepository<InventoryItem, ObjectId> {

    Optional<InventoryItem> findByBarcode(String barcode);

    List<InventoryItem> findByIsActiveTrue();

    Page<InventoryItem> findByIsActiveTrue(Pageable pageable);

    List<InventoryItem> findByPartNameContainingIgnoreCase(String partName);

    List<InventoryItem> findByQuantityLessThan(int threshold);

    boolean existsByBarcode(String barcode);

    boolean existsByPartName(String partName);

}
