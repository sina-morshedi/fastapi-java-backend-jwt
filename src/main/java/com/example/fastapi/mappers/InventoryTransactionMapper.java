package com.example.fastapi.mappers;

import com.example.fastapi.dto.InventoryTransactionRequestDTO;
import com.example.fastapi.dboModel.InventoryTransactionLog;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class InventoryTransactionMapper {

    public static InventoryTransactionLog toEntity(InventoryTransactionRequestDTO dto) {
        InventoryTransactionLog entity = new InventoryTransactionLog();

        if (dto.getId() != null && !dto.getId().isBlank()) {
            entity.setId(dto.getId());
        }

        if (dto.getCarInfoId() != null && !dto.getCarInfoId().isBlank()) {
            entity.setCarInfoId(new ObjectId(dto.getCarInfoId()));
        }

        if (dto.getCustomerId() != null && !dto.getCustomerId().isBlank()) {
            entity.setCustomerId(new ObjectId(dto.getCustomerId()));
        }

        if (dto.getCreatorUserId() != null && !dto.getCreatorUserId().isBlank()) {
            entity.setCreatorUserId(new ObjectId(dto.getCreatorUserId()));
        }

        if (dto.getInventoryItemId() != null && !dto.getInventoryItemId().isBlank()) {
            entity.setInventoryItemId(new ObjectId(dto.getInventoryItemId()));
        }

        entity.setQuantity(dto.getQuantity());

        // Mapping enum safely (they should match)
        if (dto.getType() != null) {
            entity.setType(InventoryTransactionLog.TransactionType.valueOf(dto.getType().name()));
        }

        entity.setDescription(dto.getDescription());
        entity.setDateTime(dto.getDateTime());

        return entity;
    }
}
