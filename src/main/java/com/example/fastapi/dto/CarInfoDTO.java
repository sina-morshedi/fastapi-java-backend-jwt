package com.example.fastapi.dto;

import java.util.Date;

public class CarInfoDTO {

    private String id;
    private String chassisNo;
    private String motorNo;
    private String licensePlate;
    private String brand;
    private String brandModel;
    private Integer modelYear;
    private String fuelType;
    private Date dateTime;

    // Constructors
    public CarInfoDTO() {
    }

    public CarInfoDTO(String id, String chassisNo, String motorNo, String licensePlate,
                      String brand, String brandModel, Integer modelYear,
                      String fuelType, Date dateTime) {
        this.id = id;
        this.chassisNo = chassisNo;
        this.motorNo = motorNo;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.brandModel = brandModel;
        this.modelYear = modelYear;
        this.fuelType = fuelType;
        this.dateTime = dateTime;
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getMotorNo() {
        return motorNo;
    }

    public void setMotorNo(String motorNo) {
        this.motorNo = motorNo;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

}
