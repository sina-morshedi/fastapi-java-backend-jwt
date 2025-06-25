package com.example.fastapi.repository;
import org.bson.types.ObjectId;

import com.example.fastapi.dboModel.TaskStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TaskStatusRepository extends MongoRepository<TaskStatus, ObjectId> {
    Optional<TaskStatus> findByTaskStatusName(String taskStatusName);
}
