package com.example.fastapi.service;

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
        Optional<TaskStatus> existing = taskStatusRepository.findById(id);
        if (existing.isPresent()) {
            TaskStatus entity = existing.get();
            entity.setTaskStatusName(dto.getTaskStatusName());
            TaskStatus saved = taskStatusRepository.save(entity);
            return Optional.of(new TaskStatusDTO(saved.getId(), saved.getTaskStatusName()));
        } else {
            return Optional.empty();
        }
    }

}
