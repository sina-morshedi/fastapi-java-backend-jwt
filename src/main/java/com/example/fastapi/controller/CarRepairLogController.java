package com.example.fastapi.controller;

import com.example.fastapi.dto.CarRepairLogRequestDTO;
import com.example.fastapi.dto.CarRepairLogResponseDTO;
import com.example.fastapi.service.CarRepairLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:xxxx", "https://*.netlify.app"})
@RestController
@RequestMapping("/car-repair-log")
public class CarRepairLogController {

    @Autowired
    private CarRepairLogService carRepairLogService;

    @GetMapping("/all")
    public ResponseEntity<List<CarRepairLogResponseDTO>> getAllLogs() {
        List<CarRepairLogResponseDTO> logs = carRepairLogService.getAllLogs();
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(logs);
    }


    @GetMapping("/by-license-plate/{licensePlate}")
    public ResponseEntity<List<CarRepairLogResponseDTO>> getLogsByLicensePlate(@PathVariable String licensePlate) {
        List<CarRepairLogResponseDTO> logs = carRepairLogService.getLogsByLicensePlate(licensePlate);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(logs);
    }

    @GetMapping("/latest-by-license-plate/{licensePlate}")
    public ResponseEntity<?> getLatestLogsByLicensePlate(@PathVariable String licensePlate) {
        CarRepairLogResponseDTO log = carRepairLogService.getLatestLogsByLicensePlate(licensePlate);

        if (log == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Plaka için onarım kaydı bulunamadı: " + licensePlate);
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(log);
    }
    @PostMapping("/latest-by-task-status-name")
    public ResponseEntity<?> getLatestLogsByTaskName(@RequestBody String taskStatusName) {

        if (taskStatusName == null || taskStatusName.trim().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("son görev durum adı bulunamadı.");
        }

        List<CarRepairLogResponseDTO> logs = carRepairLogService.getLatestLogsByTaskStatusName(taskStatusName);

        if (logs == null || logs.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Görev durum adı için onarım kaydı bulunamadı: " + taskStatusName);
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(logs);
    }


    @PostMapping("/task-status-name")
    public ResponseEntity<?> getLogsByTaskStatusName(@RequestBody String taskStatusName) {
        List<CarRepairLogResponseDTO> data = carRepairLogService.getLogsByTaskStatusName(taskStatusName);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(data);
    }


    @PostMapping("/create")
    public ResponseEntity<CarRepairLogResponseDTO> createLog(@RequestBody CarRepairLogRequestDTO requestDTO) {
        CarRepairLogResponseDTO createdLog = carRepairLogService.createLog(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(createdLog);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLog(@PathVariable String id, @RequestBody CarRepairLogRequestDTO requestDTO) {
        Optional<CarRepairLogResponseDTO> updated = carRepairLogService.updateLog(id, requestDTO);

        if (updated.isPresent()) {
            return ResponseEntity.ok(updated.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Log bulunamadı");
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLog(@PathVariable String id) {
        boolean deleted = carRepairLogService.deleteLog(id);
        if (deleted) {
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Log başarıyla silindi");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Log bulunamadı");
        }
    }
}
