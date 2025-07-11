package com.example.fastapi.dboModel;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "carProblemReport")
public class CarProblemReport {

    @Id
    private ObjectId id;

    private ObjectId carId;           // ✅ تبدیل به ObjectId
    private ObjectId creatorUserId;   // ✅ تبدیل به ObjectId
    private String problemSummary;
    private Date dateTime;

    public CarProblemReport() {}

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

    public String getProblemSummary() {
        return problemSummary;
    }

    public void setProblemSummary(String problemSummary) {
        this.problemSummary = problemSummary;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
