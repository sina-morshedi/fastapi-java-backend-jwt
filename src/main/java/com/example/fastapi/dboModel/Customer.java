package com.example.fastapi.dboModel;

import org.springframework.data.annotation.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "customers")
public class Customer {

    @Id
    private ObjectId id;

    private String fullName;
    private String phone;
    private String nationalId;
    private String address;
    private Date createdAt;

    public Customer() {
        this.createdAt = new Date();
    }

    public Customer(String fullName, String phone, String nationalId, String address) {
        this.fullName = fullName;
        this.phone = phone;
        this.nationalId = nationalId;
        this.address = address;
        this.createdAt = new Date();
    }

    public String getId() {
        return id != null ? id.toHexString() : null;
    }

    public void setId(String id) {
        if (id != null && !id.isBlank()) {
            this.id = new ObjectId(id);
        }
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
