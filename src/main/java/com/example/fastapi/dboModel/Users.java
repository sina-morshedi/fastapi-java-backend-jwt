package com.example.fastapi.dboModel;

import org.springframework.data.annotation.Id;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class Users {
    @Id
    private ObjectId id;

    private String firstName;
    private String lastName;

    private ObjectId roleId;         // تغییر دادیم از String به ObjectId
    private ObjectId permissionId;   // تغییر دادیم از String به ObjectId

    public Users() {}

    // --- Getters and Setters ---

    public String getId() {
        return id != null ? id.toHexString() : null;
    }

    public void setId(String id) {
        if (id != null && !id.isBlank()) {
            this.id = new ObjectId(id);
        }
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
        return roleId != null ? roleId.toHexString() : null;
    }

    public void setRoleId(String roleId) {
        if (roleId != null && !roleId.isBlank()) {
            this.roleId = new ObjectId(roleId);
        }
    }

    public String getPermissionId() {
        return permissionId != null ? permissionId.toHexString() : null;
    }

    public void setPermissionId(String permissionId) {
        if (permissionId != null && !permissionId.isBlank()) {
            this.permissionId = new ObjectId(permissionId);
        }
    }
}
