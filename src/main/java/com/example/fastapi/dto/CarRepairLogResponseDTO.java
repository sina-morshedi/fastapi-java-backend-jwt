package com.example.fastapi.dto;

import com.example.fastapi.dboModel.PartUsed;
import com.example.fastapi.dboModel.PaymentRecords;


import java.util.Date;
import java.util.List;

public class CarRepairLogResponseDTO {

    private String id;
    private CarInfoDTO carInfo;
    private UserProfileDTO creatorUser;
    private UserProfileDTO assignedUser;
    private String description;
    private TaskStatusDTO taskStatus;
    private Date dateTime;
    private CarProblemReportDTO problemReport;
    private List<PartUsed> partsUsed;
    private List<PaymentRecords> paymentRecords;

    private CustomerDTO customer; // new field


    public CarRepairLogResponseDTO() {}

    public CarRepairLogResponseDTO(
            String id,
            CarInfoDTO carInfo,
            UserProfileDTO creatorUser,
            UserProfileDTO assignedUser,
            String description,
            TaskStatusDTO taskStatus,
            Date dateTime,
            CarProblemReportDTO problemReport,
            List<PartUsed> partsUsed,
            List<PaymentRecords> paymentRecords,
            CustomerDTO customer // <-- اضافه شده
    ) {
        this.id = id;
        this.carInfo = carInfo;
        this.creatorUser = creatorUser;
        this.assignedUser = assignedUser;
        this.description = description;
        this.taskStatus = taskStatus;
        this.dateTime = dateTime;
        this.problemReport = problemReport;
        this.partsUsed = partsUsed;
        this.paymentRecords = paymentRecords;
        this.customer = customer;
    }



    // ===== Getters and Setters =====

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CarInfoDTO getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(CarInfoDTO carInfo) {
        this.carInfo = carInfo;
    }

    public UserProfileDTO getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(UserProfileDTO creatorUser) {
        this.creatorUser = creatorUser;
    }

    public UserProfileDTO getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(UserProfileDTO assignedUser) {
        this.assignedUser = assignedUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatusDTO getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatusDTO taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public CarProblemReportDTO getProblemReport() {
        return problemReport;
    }

    public void setProblemReport(CarProblemReportDTO problemReport) {
        this.problemReport = problemReport;
    }

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

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

}
