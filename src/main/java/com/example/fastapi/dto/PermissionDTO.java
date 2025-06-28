package com.example.fastapi.dto;

public class PermissionDTO {
    private String id;
    private String permissionName;

    public PermissionDTO() {}

    public PermissionDTO(String permissionId, String permissionName) {
        this.id = permissionId;
        this.permissionName = permissionName;
    }

    public String getPermissionId() { return id; }
    public void setPermissionId(String permissionId) { this.id = permissionId; }

    public String getPermissionName() { return permissionName; }
    public void setPermissionName(String permissionName) { this.permissionName = permissionName; }
}
