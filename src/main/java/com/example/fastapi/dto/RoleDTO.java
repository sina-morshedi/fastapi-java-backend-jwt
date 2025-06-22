package com.example.fastapi.dto;

public class RoleDTO {
    private String roleId;
    private String roleName;

    public RoleDTO() {}

    public RoleDTO(String roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}
