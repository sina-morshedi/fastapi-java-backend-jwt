package com.example.fastapi.dboModel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "carRepairLog")
public class CarRepairLog {

    @Id
    private String id;

    private String carId;
    private String creatorUserId;
    private String departmentId;
    private String description;
    private String taskStatusId;
    private Date dateTime;
    private String problemReportId;
    private String carRequiredDepartmentId;

    public CarRepairLog() {}

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

    public String getCarRequiredDepartmentId() {
        return carRequiredDepartmentId;
    }

    public void setCarRequiredDepartmentId(String carRequiredDepartmentId) {
        this.carRequiredDepartmentId = carRequiredDepartmentId;
    }
}
