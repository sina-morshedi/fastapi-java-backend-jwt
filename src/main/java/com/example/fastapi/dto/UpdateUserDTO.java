package com.example.fastapi.dto;

public class UpdateUserDTO {

    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private String roleId;
    private String permissionId;
    private String password;
    private boolean updatePassword;

    public UpdateUserDTO() {}

    public UpdateUserDTO(String userId, String username, String firstName, String lastName,
                         String roleId, String permissionId, String password, boolean updatePassword) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.password = password;
        this.updatePassword = updatePassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUpdatePassword() {
        return updatePassword;
    }

    public void setUpdatePassword(boolean updatePassword) {
        this.updatePassword = updatePassword;
    }
}
