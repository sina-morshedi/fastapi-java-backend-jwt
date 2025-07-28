package com.example.fastapi.dboModel;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "inventorySaleLogs")
public class InventorySaleLog {

    @Id
    private ObjectId id;

    private String customerName;                       // نام مشتری
    private List<SaleItem> soldItems;                  // لیست اقلام فروخته‌شده
    private Double totalAmount;                        // مبلغ کل فروش
    private Date saleDate;                             // تاریخ فروش
    private List<PaymentRecords> paymentRecords;       // لیست پرداخت‌ها

    // --- Constructors ---
    public InventorySaleLog() {}

    public InventorySaleLog(String customerName, List<SaleItem> soldItems, Double totalAmount,
                            Date saleDate, List<PaymentRecords> paymentRecords) {
        this.customerName = customerName;
        this.soldItems = soldItems;
        this.totalAmount = totalAmount;
        this.saleDate = saleDate;
        this.paymentRecords = paymentRecords;
    }

    // --- Getters & Setters ---
    public String getId() {
        return id != null ? id.toHexString() : null;
    }

    public void setId(String id) {
        if (id != null && !id.isBlank()) {
            this.id = new ObjectId(id);
        }
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
