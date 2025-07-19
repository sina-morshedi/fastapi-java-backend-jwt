// اینترفیس
package com.example.fastapi.repository;

import com.example.fastapi.dboModel.InventoryTransactionLog;
import com.example.fastapi.dto.InventoryTransactionResponseDTO;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface InventoryTransactionLogCustomRepository {
    List<InventoryTransactionResponseDTO> findAllTransactions();
//    List<InventoryTransactionLog> findByCarInfoId(Object carInfoId);
//
//    List<InventoryTransactionLog> findByCustomerId(Object customerId);
//
//    List<InventoryTransactionLog> findByUserId(Object userId);
//
//    List<InventoryTransactionLog> findByType(InventoryTransactionLog.TransactionType type);
//
//    // یا ترکیب فیلدها:
//    List<InventoryTransactionLog> findByCarInfoIdAndType(Object carInfoId, InventoryTransactionLog.TransactionType type);

}
