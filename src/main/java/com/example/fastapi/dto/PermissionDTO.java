package com.example.fastapi.dto;

public class PermissionDTO {
    private String permissionId;
    private String permissionName;

    public PermissionDTO() {}

    public PermissionDTO(String permissionId, String permissionName) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
    }

    public String getPermissionId() { return permissionId; }
    public void setPermissionId(String permissionId) { this.permissionId = permissionId; }

    public String getPermissionName() { return permissionName; }
    public void setPermissionName(String permissionName) { this.permissionName = permissionName; }
}
