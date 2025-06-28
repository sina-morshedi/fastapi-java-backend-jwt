package com.example.fastapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskStatusDTO {

    private String id;
    private String taskStatusName;

    // Constructors
    public TaskStatusDTO() {}

    public TaskStatusDTO(String id, String taskStatusName) {
        this.id = id;
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
