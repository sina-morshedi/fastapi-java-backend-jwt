package com.example.fastapi.dboModel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "taskStatus")
public class TaskStatus {

    @Id
    private String id;
    private String taskStatusName;

    // Constructors
    public TaskStatus() {}

    public TaskStatus(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getTaskStatusName() {
        return taskStatusName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }
}
