package com.example.fastapi.dto;
import java.util.List;

public class TaskStatusUserRequestDTO {
    private List<String> taskStatusNames;
    private String assignedUserId;

    public List<String> getTaskStatusNames() {
        return taskStatusNames;
    }

    public void setTaskStatusNames(List<String> taskStatusNames) {
        this.taskStatusNames = taskStatusNames;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }
}