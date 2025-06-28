package com.example.fastapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class CarRepairLogDTO {

    private String id;
    private String carId;
    private String creatorUserId;
    private String departmentId;       // nullable
    private String description;        // nullable
    private String taskStatusId;       // nullable
    private Date dateTime;
    private String problemReportId;    // nullable

    public CarRepairLogDTO() {}

    public CarRepairLogDTO(String id, String carId, String creatorUserId, String departmentId,
                           String description, String taskStatusId, Date dateTime,
                           String problemReportId) {
        this.id = id;
        this.carId = carId;
        this.creatorUserId = creatorUserId;
        this.departmentId = departmentId;
        this.description = description;
        this.taskStatusId = taskStatusId;
        this.dateTime = dateTime;
        this.problemReportId = problemReportId;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
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
