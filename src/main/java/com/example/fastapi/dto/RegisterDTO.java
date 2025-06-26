package com.example.fastapi.dto;

public class RegisterDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String roleId;
    private String roleName;         // اضافه شده
    private String permissionId;
    private String permissionName;   // اضافه شده

    public RegisterDTO() {}

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getRoleId() { return roleId; }
    public String getRoleName() { return roleName; }          // اضافه شده
    public String getPermissionId() { return permissionId; }
    public String getPermissionName() { return permissionName; }  // اضافه شده

    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setRoleId(String roleId) { this.roleId = roleId; }
    public void setRoleName(String roleName) { this.roleName = roleName; }      // اضافه شده
    public void setPermissionId(String permissionId) { this.permissionId = permissionId; }
    public void setPermissionName(String permissionName) { this.permissionName = permissionName; }  // اضافه شده
}
