package com.example.fastapi.controller;

import com.example.fastapi.dto.TaskStatusDTO;
import com.example.fastapi.config.ContextHolder;
import com.example.fastapi.service.JwtService;
import com.example.fastapi.service.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/task_status")
public class TaskStatusController {

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private JwtService jwtService;

    private boolean isTokenInvalid(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ") || !jwtService.validateToken(authHeader.substring(7));
    }

    private boolean prepareContext(String authHeader) {
        if (isTokenInvalid(authHeader)) {
            return false;
        }
        String token = authHeader.substring(7);
        String storeName = jwtService.getStoreNameFromToken(token);
        ContextHolder.setStoreName(storeName);
        return true;
    }

    @PostMapping("/getByStatusName")
    public ResponseEntity<Object> getByStatusName(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, String> requestBody) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
            String taskName = requestBody.get("taskName");
            Optional<TaskStatusDTO> optionalStatus = taskStatusService.getByTaskStatusName(taskName);
            if (optionalStatus.isPresent()) {
                return ResponseEntity.ok()
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body(optionalStatus.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("'" + taskName + "' adlı görev durumu bulunamadı");
            }
        } finally {
            ContextHolder.clear();
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> addTaskStatus(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody TaskStatusDTO taskStatusDTO) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
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
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTasks(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
            List<TaskStatusDTO> tasks = taskStatusService.getAllStatuses();
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(tasks);
        } finally {
            ContextHolder.clear();
        }
    }

    @PutMapping("/updateTaskStatus/{id}")
    public ResponseEntity<Object> updateTaskStatus(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id,
            @RequestBody TaskStatusDTO updatedStatusDTO) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
            Optional<TaskStatusDTO> updated = taskStatusService.updateTaskStatus(id, updatedStatusDTO);
            if (updated.isPresent()) {
                return ResponseEntity.ok()
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body(updated.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Görev durumu bulunamadı");
            }
        } finally {
            ContextHolder.clear();
        }
    }

    @DeleteMapping("/deleteTaskStatus/{id}")
    public ResponseEntity<Object> deleteTaskStatus(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
            boolean deleted = taskStatusService.deleteTaskStatus(id);
            if (deleted) {
                return ResponseEntity.ok().body("Görev durumu başarıyla silindi.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Görev durumu bulunamadı.");
            }
        } finally {
            ContextHolder.clear();
        }
    }
}
