package com.example.fastapi.dboModel;

import org.springframework.data.annotation.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "carInfo")
public class CarInfo {

    @Id
    private ObjectId  id;

    private String chassisNo;
    private String motorNo;
    private String licensePlate;
    private String brand;
    private String brandModel;
    private Integer modelYear;
    private String fuelType;
    private Date dateTime;

    // Default constructor
    public CarInfo() {
    }

    // Constructor with all fields (except id)
    public CarInfo(String chassisNo, String motorNo, String licensePlate, String brand,
                   String brandModel, Integer modelYear, String fuelType, Date dateTime) {
        this.chassisNo = chassisNo;
        this.motorNo = motorNo;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.brandModel = brandModel;
        this.modelYear = modelYear;
        this.fuelType = fuelType;
        this.dateTime = dateTime;
    }

    // Getters and Setters

    public String getId() {
        return id != null ? id.toHexString() : null;
    }

    // Setter تبدیل String به ObjectId
    public void setId(String id) {
        if (id != null && !id.isBlank()) {
            this.id = new ObjectId(id);
        }
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
