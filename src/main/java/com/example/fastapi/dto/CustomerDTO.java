package com.example.fastapi.dto;

public class CustomerDTO {

    private String id;           // optional هنگام ایجاد، ولی برای نمایش لازمه
    private String fullName;
    private String phone;
    private String nationalId;
    private String address;

    // Constructors
    public CustomerDTO() {
    }

    public CustomerDTO(String id, String fullName, String phone, String nationalId, String address) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.nationalId = nationalId;
        this.address = address;
    }

    // Getters & Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
