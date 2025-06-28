package com.example.fastapi.dto;
import com.fasterxml.jackson.annotation.JsonProperty;


public class PermissionDTO {
    @JsonProperty("_id")
    private String id;
    private String permissionName;

    public PermissionDTO() {}

    public PermissionDTO(String permissionId, String permissionName) {
        this.id = permissionId;
        this.permissionName = permissionName;
    }

    public String getId() { return id; }
    public void setId(String permissionId) { this.id = permissionId; }

    public String getPermissionName() { return permissionName; }
    public void setPermissionName(String permissionName) { this.permissionName = permissionName; }

    @Override
    public String toString() {
        return "PermissionDTO{" +
                "id='" + id + '\'' +
                ", permissionName='" + permissionName + '\'' +
                '}';
    }

}
