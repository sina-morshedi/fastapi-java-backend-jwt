package com.example.fastapi.dboModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "permissions")
public class Permissions {
    @Id
    private String id;
    private String permissionName;
}