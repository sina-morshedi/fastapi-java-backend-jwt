package com.example.fastapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class CarProblemReportDTO {

    @JsonProperty("id")
    private String id = "";  // مقدار پیش‌فرض خالی

    private CarInfoDTO carInfo;
    private UserProfileDTO creatorUser;
    private String problemSummary;
    private Date dateTime;

    public CarProblemReportDTO() {}

    public CarProblemReportDTO(String id, CarInfoDTO carInfo, UserProfileDTO creatorUser, String problemSummary, Date dateTime) {
        this.id = id;
        this.carInfo = carInfo;
        this.creatorUser = creatorUser;
        this.problemSummary = problemSummary;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = (id != null) ? id : "";
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
