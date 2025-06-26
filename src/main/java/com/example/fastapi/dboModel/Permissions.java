package com.example.fastapi.dboModel;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "permissions")
public class Permissions {
    @Id
    private ObjectId id;  // <-- اینجا از ObjectId استفاده کن

    private String permissionName;

    public Permissions() {}

    public Permissions(ObjectId id, String roleName) {
        this.id = id;
        this.permissionName = roleName;
    }
    public String getId() {
        return id != null ? id.toHexString() : null;
    }

    // setter تبدیل String به ObjectId
    public void setId(String id) {
        if (id != null && !id.isBlank()) {
            this.id = new ObjectId(id);
        }
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}
