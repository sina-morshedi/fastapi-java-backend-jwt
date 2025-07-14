package com.example.fastapi.dto;

import java.util.Date;
import java.util.List;
import com.example.fastapi.dboModel.PartUsed;
import com.example.fastapi.dboModel.PaymentRecords;

public class CarRepairLogRequestDTO {
    private String carId;
    private String creatorUserId;
    private String assignedUserId;
    private String description;
    private String taskStatusId;
    private Date dateTime;
    private String problemReportId;
    private List<PartUsed> partsUsed;

    private List<PaymentRecords> paymentRecords;

    private String customerId;



    public CarRepairLogRequestDTO() {}

    public CarRepairLogRequestDTO(String carId, String creatorUserId, String assignedUserId,
                                  String description, String taskStatusId,
                                  Date dateTime, String problemReportId,
                                  List<PartUsed> partsUsed,
                                  List<PaymentRecords> paymentRecords,
                                  String customerId) {
        this.carId = carId;
        this.creatorUserId = creatorUserId;
        this.assignedUserId = assignedUserId;
        this.description = description;
        this.taskStatusId = taskStatusId;
        this.dateTime = dateTime;
        this.problemReportId = problemReportId;
        this.partsUsed = partsUsed;
        this.paymentRecords = paymentRecords;
        this.customerId = customerId;
    }



    // Getter و Setter برای فیلدهای قبلی...

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskStatusId() {
        return taskStatusId;
    }

    public void setTaskStatusId(String taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getProblemReportId() {
        return problemReportId;
    }

    public void setProblemReportId(String problemReportId) {
        this.problemReportId = problemReportId;
    }

    // Getter و Setter برای partsUsed

    public List<PartUsed> getPartsUsed() {
        return partsUsed;
    }

    public void setPartsUsed(List<PartUsed> partsUsed) {
        this.partsUsed = partsUsed;
    }

    public List<PaymentRecords> getPaymentRecords() {
        return paymentRecords;
    }

    public void setPaymentRecords(List<PaymentRecords> paymentRecords) {
        this.paymentRecords = paymentRecords;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

}
