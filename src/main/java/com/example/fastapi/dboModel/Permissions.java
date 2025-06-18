package com.example.fastapi.dboModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "permissions")
public class Permissions {
    @Id
    private String id;
    private String permissionName;

    Permissions(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionsName) {
        this.permissionName = permissionName;
    }
}
