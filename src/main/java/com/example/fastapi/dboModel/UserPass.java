package com.example.fastapi.dboModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userPass")
public class UserPass {
    @Id
    private String id;
    private String user_name;
    private String password;
}