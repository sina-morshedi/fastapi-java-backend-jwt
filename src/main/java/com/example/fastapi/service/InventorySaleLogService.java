package com.example.fastapi.service;

import com.example.fastapi.dboModel.InventorySaleLog;
import com.example.fastapi.repository.InventorySaleLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InventorySaleLogService {

    private final InventorySaleLogRepository repository;

    @Autowired
    public InventorySaleLogService(InventorySaleLogRepository repository) {
        this.repository = repository;
    }

    public InventorySaleLog save(InventorySaleLog saleLog) {
        return repository.save(saleLog);
    }

    public Optional<InventorySaleLog> findById(String id) {
        return repository.findById(id);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public List<InventorySaleLog> findAll() {
        return repository.findAll();
    }

    public List<InventorySaleLog> findByCustomerName(String customerName) {
        return repository.findByCustomerName(customerName);
    }

    public List<InventorySaleLog> findBySaleDateBetween(Date startDate, Date endDate) {
        return repository.findBySaleDateBetween(startDate, endDate);
    }

    public Optional<InventorySaleLog> updateInventorySaleLog(String id, InventorySaleLog updatedSaleLog) {
        return repository.findById(id)
                .map(existingLog -> {
                    existingLog.setCustomerName(updatedSaleLog.getCustomerName());
                    existingLog.setSaleDate(updatedSaleLog.getSaleDate());
                    existingLog.setSoldItems(updatedSaleLog.getSoldItems());
                    existingLog.setTotalAmount(updatedSaleLog.getTotalAmount());
                    existingLog.setPaymentRecords(updatedSaleLog.getPaymentRecords());
                    existingLog.setRemainingAmount(updatedSaleLog.getRemainingAmount()); // ← این خط اضافه شد

                    return repository.save(existingLog);
                });
    }
    public Double calculateTotalRemainingAmount() {
        List<InventorySaleLog> logs = repository.findAll();

        return logs.stream()
                .filter(log -> log.getRemainingAmount() != null)
                .mapToDouble(InventorySaleLog::getRemainingAmount)
                .sum();
    }

}
