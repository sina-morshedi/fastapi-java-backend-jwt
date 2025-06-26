package com.example.fastapi.dboModel;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class Users {
    @Id
    private String id;

    private String firstName;
    private String lastName;

    private Roles role;           // آبجکت نقش
    private Permissions permission;  // آبجکت دسترسی

    public Users() {}

    // Getter و Setter ها

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public Permissions getPermission() {
        return permission;
    }

    public void setPermission(Permissions permission) {
        this.permission = permission;
    }

    // متدهای مربوط به ObjectId سند کاربر (در صورت نیاز)

    public ObjectId getObjectId() {
        return new ObjectId(this.id);
    }

    public void setObjectId(ObjectId objectId) {
        this.id = objectId.toHexString();
    }
}
