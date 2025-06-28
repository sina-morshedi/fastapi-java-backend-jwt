package com.example.fastapi.dto;
public class UserProfileDTO {
    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private RolesDTO role;
    private PermissionDTO permission;

    public UserProfileDTO() {}

    public UserProfileDTO(String userId, String username, String firstName, String lastName,
                          RolesDTO role, PermissionDTO permission) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.permission = permission;
    }

    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public RolesDTO getRole() { return role; }
    public void setRole(RolesDTO role) { this.role = role; }

    public PermissionDTO getPermission() { return permission; }
    public void setPermission(PermissionDTO permission) { this.permission = permission; }

    @Override
    public String toString() {
        return "UserProfileDTO{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                ", permission=" + permission +
                '}';
    }
}

