package com.example.fastapi.dto;

public class TaskStatusCountDTO {
    private String taskStatusId;
    private String taskStatusName;
    private int count;

    public TaskStatusCountDTO(String taskStatusId, String taskStatusName, int count) {
        this.taskStatusId = taskStatusId;
        this.taskStatusName = taskStatusName;
        this.count = count;
    }

    public String getTaskStatusId() {
        return taskStatusId;
    }

    public void setTaskStatusId(String taskStatusId) {
        this.taskStatusId = taskStatusId;
    }

    public String getTaskStatusName() {
        return taskStatusName;
    }

    public void setTaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
