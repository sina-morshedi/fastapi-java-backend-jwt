package com.example.fastapi.dboModel;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "userPass")
public class UserPass {
    @Id
    private ObjectId id;

    private String username;
    private String password;

    @Field("user_id")
    private ObjectId userId;  // ✅ تغییر به ObjectId

    public UserPass() {}

    // Getters and Setters

    public String getId() {
        return id != null ? id.toHexString() : null;
    }

    public void setId(String id) {
        if (id != null && !id.isBlank()) {
            this.id = new ObjectId(id);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId != null ? userId.toHexString() : null;
    }

    public void setUserId(String userId) {
        if (userId != null && !userId.isBlank()) {
            this.userId = new ObjectId(userId);
        }
    }
}
