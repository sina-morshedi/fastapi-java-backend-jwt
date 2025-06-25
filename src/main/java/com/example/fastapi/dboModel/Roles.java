package com.example.fastapi.dboModel;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
public class Roles {

    @Id
    private ObjectId id;

    private String roleName;

    public Roles() {}

    public Roles(ObjectId id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public String getId() {
        return id != null ? id.toHexString() : null;
    }

    // Setter تبدیل String به ObjectId
    public void setId(String id) {
        if (id != null && !id.isBlank()) {
            this.id = new ObjectId(id);
        }
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
