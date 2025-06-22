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
    private ObjectId roleId;
    private ObjectId permissionId;
    private String team; // ✅ این اضافه شده

    public Users() {}

    // Getters and Setters

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

    public ObjectId getRoleId() {
        return roleId;
    }

    public void setRoleId(ObjectId roleId) {
        this.roleId = roleId;
    }

    public ObjectId getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(ObjectId permissionId) {
        this.permissionId = permissionId;
    }

    public String getTeam() { // ✅ Getter
        return team;
    }

    public void setTeam(String team) { // ✅ Setter
        this.team = team;
    }
}
