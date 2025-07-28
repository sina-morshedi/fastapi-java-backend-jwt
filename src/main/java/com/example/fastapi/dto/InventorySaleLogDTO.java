package com.example.fastapi.dto;

import com.example.fastapi.dboModel.SaleItem;
import com.example.fastapi.dboModel.PaymentRecords;

import java.util.Date;
import java.util.List;

public class InventorySaleLogDTO {

    private String id;
    private String customerName;
    private List<SaleItem> soldItems;                 // استفاده مستقیم از مدل اصلی
    private Double totalAmount;
    private Date saleDate;
    private List<PaymentRecords> paymentRecords;      // استفاده مستقیم از مدل اصلی

    public InventorySaleLogDTO() {}

    public InventorySaleLogDTO(String id, String customerName, List<SaleItem> soldItems,
                               Double totalAmount, Date saleDate, List<PaymentRecords> paymentRecords) {
        this.id = id;
        this.customerName = customerName;
        this.soldItems = soldItems;
        this.totalAmount = totalAmount;
        this.saleDate = saleDate;
        this.paymentRecords = paymentRecords;
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<SaleItem> getSoldItems() {
        return soldItems;
    }

    public void setSoldItems(List<SaleItem> soldItems) {
        this.soldItems = soldItems;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public List<PaymentRecords> getPaymentRecords() {
        return paymentRecords;
    }

    public void setPaymentRecords(List<PaymentRecords> paymentRecords) {
        this.paymentRecords = paymentRecords;
    }
}
