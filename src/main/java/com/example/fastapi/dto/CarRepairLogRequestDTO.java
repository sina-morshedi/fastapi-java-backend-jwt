package com.example.fastapi.dto;

import java.util.Date;

public class CarRepairLogRequestDTO {
    private String carId;
    private String creatorUserId;
    private String description;       // nullable
    private String taskStatusId;      // nullable
    private Date dateTime;
    private String problemReportId;   // nullable

    public CarRepairLogRequestDTO() {}

    public CarRepairLogRequestDTO(String carId, String creatorUserId,
                                  String description, String taskStatusId,
                                  Date dateTime, String problemReportId) {
        this.carId = carId;
        this.creatorUserId = creatorUserId;
        this.description = description;
        this.taskStatusId = taskStatusId;
        this.dateTime = dateTime;
        this.problemReportId = problemReportId;
    }

    // Getters and setters

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
}
