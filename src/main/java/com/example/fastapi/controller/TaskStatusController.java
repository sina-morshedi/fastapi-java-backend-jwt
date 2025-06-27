package com.example.fastapi.controller;

import com.example.fastapi.dto.TaskStatusDTO;
import com.example.fastapi.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/task_status")
public class TaskStatusController {


    @Autowired
    private TaskStatusService taskStatusService;


    @GetMapping("/")
    public ResponseEntity<Object> getByStatusName(@RequestParam String taskName) {
        Optional<TaskStatusDTO> optionalStatus = taskStatusService.getByTaskStatusName(taskName);
        if (optionalStatus.isPresent()) {
            return ResponseEntity.ok(optionalStatus.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("'" + taskName + "' adlı görev durumu bulunamadı");
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> addTaskStatus(@RequestBody TaskStatusDTO taskStatusDTO) {
        // Check if the task status name already exists
        Optional<TaskStatusDTO> existing = taskStatusService.getByTaskStatusName(taskStatusDTO.getTaskStatusName());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Aynı isimli Görev Durumu zaten mevcut.");
        }

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
    public ResponseEntity<Object> updateTaskStatus(
            @PathVariable String id,
            @RequestBody TaskStatusDTO updatedStatusDTO) {

        Optional<TaskStatusDTO> updated = taskStatusService.updateTaskStatus(id, updatedStatusDTO);

        if (updated.isPresent()) {
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(updated.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Görev durumu bulunamadı");
        }
    }


    @DeleteMapping("/deleteTaskStatus/{id}")
    public ResponseEntity<Object> deleteTaskStatus(@PathVariable String id) {
        boolean deleted = taskStatusService.deleteTaskStatus(id);
        if (deleted) {
            return ResponseEntity.ok().body("Görev durumu başarıyla silindi.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Görev durumu bulunamadı.");
        }
    }

}
