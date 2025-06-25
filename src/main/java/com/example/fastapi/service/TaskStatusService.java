package com.example.fastapi.service;
import org.bson.types.ObjectId;

import com.example.fastapi.dboModel.TaskStatus;
import com.example.fastapi.dto.TaskStatusDTO;
import com.example.fastapi.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskStatusService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    // تبدیل مدل به DTO
    private TaskStatusDTO convertToDTO(TaskStatus taskStatus) {
        return new TaskStatusDTO(taskStatus.getId(), taskStatus.getTaskStatusName());
    }

    public Optional<TaskStatusDTO> getByTaskStatusName(String statusName) {
        return taskStatusRepository.findByTaskStatusName(statusName)
                .map(this::convertToDTO);
    }

    public List<TaskStatusDTO> getAllStatuses() {
        return taskStatusRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TaskStatusDTO saveTaskStatus(TaskStatusDTO dto) {
        TaskStatus entity = new TaskStatus();
        entity.setTaskStatusName(dto.getTaskStatusName());
        TaskStatus saved = taskStatusRepository.save(entity);
        return convertToDTO(saved);
    }

    public Optional<TaskStatusDTO> updateTaskStatus(String id, TaskStatusDTO dto) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return Optional.empty(); // اگر فرمت اشتباه بود
        }

        Optional<TaskStatus> existing = taskStatusRepository.findById(objectId);
        if (existing.isPresent()) {
            TaskStatus entity = existing.get();
            entity.setTaskStatusName(dto.getTaskStatusName());
            TaskStatus saved = taskStatusRepository.save(entity);
            return Optional.of(convertToDTO(saved));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteTaskStatus(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            if (taskStatusRepository.existsById(objectId)) {
                taskStatusRepository.deleteById(objectId);
                return true;
            } else {
                return false;
            }
        } catch (IllegalArgumentException e) {
            return false; // Invalid ID format
        }
    }


}
