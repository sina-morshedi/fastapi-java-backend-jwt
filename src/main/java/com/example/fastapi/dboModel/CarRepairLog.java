package com.example.fastapi.dboModel;

import org.springframework.data.annotation.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Document(collection = "carRepairLog")
public class CarRepairLog {

    @Id
    private ObjectId id;

    private ObjectId carId;
    private ObjectId creatorUserId;
    private String description;
    private ObjectId taskStatusId;
    private Date dateTime;
    private ObjectId problemReportId;
    private ObjectId assignedUserId;

    private List<PartUsed> partsUsed;

    private List<PaymentRecords> paymentRecords;


    public CarRepairLog() {}

    // --- Getters and Setters ---

    public String getId() {
        return id != null ? id.toHexString() : null;
    }

    public void setId(String id) {
        if (id != null && !id.isBlank()) {
            this.id = new ObjectId(id);
        }
    }

    public String getCarId() {
        return carId != null ? carId.toHexString() : null;
    }

    public void setCarId(String carId) {
        if (carId != null && !carId.isBlank()) {
            this.carId = new ObjectId(carId);
        }
    }

    public String getCreatorUserId() {
        return creatorUserId != null ? creatorUserId.toHexString() : null;
    }

    public void setCreatorUserId(String creatorUserId) {
        if (creatorUserId != null && !creatorUserId.isBlank()) {
            this.creatorUserId = new ObjectId(creatorUserId);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskStatusId() {
        return taskStatusId != null ? taskStatusId.toHexString() : null;
    }

    public void setTaskStatusId(String taskStatusId) {
        if (taskStatusId != null && !taskStatusId.isBlank()) {
            this.taskStatusId = new ObjectId(taskStatusId);
        }
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getProblemReportId() {
        return problemReportId != null ? problemReportId.toHexString() : null;
    }

    public void setProblemReportId(String id) {
        if (id != null && !id.isBlank()) {
            this.problemReportId = new ObjectId(id);
        }
    }

    public String getAssignedUserId() {
        return assignedUserId != null ? assignedUserId.toHexString() : null;
    }

    public void setAssignedUserId(String assignedUserId) {
        if (assignedUserId != null && !assignedUserId.isBlank()) {
            this.assignedUserId = new ObjectId(assignedUserId);
        }
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
}
