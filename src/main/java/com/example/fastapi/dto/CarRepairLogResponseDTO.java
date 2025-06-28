package com.example.fastapi.dto;

import java.util.Date;

public class CarRepairLogResponseDTO {
    private String id;
    private CarInfoDTO carInfo;
    private UserProfileDTO creatorUser;
    private String description;                      // nullable
    private TaskStatusDTO taskStatus;                // nullable
    private Date dateTime;
    private CarProblemReportDTO problemReport;       // nullable

    public CarRepairLogResponseDTO() {}

    public CarRepairLogResponseDTO(String id, CarInfoDTO carInfo, UserProfileDTO creatorUser,
                                   String description,
                                   TaskStatusDTO taskStatus, Date dateTime,
                                   CarProblemReportDTO problemReport) {
        this.id = id;
        this.carInfo = carInfo;
        this.creatorUser = creatorUser;
        this.description = description;
        this.taskStatus = taskStatus;
        this.dateTime = dateTime;
        this.problemReport = problemReport;
    }

    // Getters and Setters

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
}
