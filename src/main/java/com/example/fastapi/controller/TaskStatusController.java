package com.example.fastapi.controller;

import com.example.fastapi.dto.TaskStatusDTO;
import com.example.fastapi.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/task_status")
public class TaskStatusController {


    @Autowired
    private TaskStatusService taskStatusService;


    @GetMapping("/")
    public Optional<TaskStatusDTO> getByStatusName(@RequestParam String taskName)
    {
        return taskStatusService.getByTaskStatusName(taskName);
    }
    @PostMapping("/")
    public ResponseEntity<TaskStatusDTO> addTaskStatus(@RequestBody TaskStatusDTO taskStatusDTO) {
        TaskStatusDTO savedStatus = taskStatusService.saveTaskStatus(taskStatusDTO);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(savedStatus);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTasks() {
        List<TaskStatusDTO> tasks = taskStatusService.getAllStatuses();
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(tasks);
    }
    @PutMapping("/updateTaskStatus/{id}")
    public ResponseEntity<TaskStatusDTO> updateTaskStatus(
            @PathVariable String id,
            @RequestBody TaskStatusDTO updatedStatusDTO) {

        Optional<TaskStatusDTO> updated = taskStatusService.updateTaskStatus(id, updatedStatusDTO);

        if (updated.isPresent()) {
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(updated.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
