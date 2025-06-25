package com.example.fastapi.repository;

import com.example.fastapi.dboModel.TaskStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TaskStatusRepository extends MongoRepository<TaskStatus, String> {
    Optional<TaskStatus> findByTaskStatusName(String taskStatusName);
}
