package com.example.fastapi.dboModel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.bson.types.ObjectId;

@Document(collection = "task_status")
public class TaskStatus {

    @Id
    private ObjectId id;

    private String taskStatusName;

    // Getter برای تبدیل به String در DTO
    public String getId() {
        return id != null ? id.toHexString() : null;
    }

    public void setId(String id) {
        if (id != null) {
            this.id = new ObjectId(id);
        }
    }

    public String getTaskStatusName() {
        return taskStatusName;
    }

    public void setTaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }
}
