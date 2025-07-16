package com.example.fastapi.controller;

import com.example.fastapi.dto.*;
import com.example.fastapi.dto.FilterRequestDTO;
import com.example.fastapi.service.CarRepairLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.time.LocalTime;


import java.time.format.DateTimeParseException;

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

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getLogById(@PathVariable String id) {
        CarRepairLogResponseDTO log = carRepairLogService.getLogById(id);

        if (log == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("bilgi bulunamadı.");
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(log);
    }

    @GetMapping("/by-license-plate/{licensePlate}")
    public ResponseEntity<?> getLogsByLicensePlate(@PathVariable String licensePlate) {
        List<CarRepairLogResponseDTO> logs = carRepairLogService.getLogsByLicensePlate(licensePlate);

        if (logs == null || logs.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(licensePlate + "numaralı plakaya ait bilgi bulunamadı.");
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(logs);
    }

    @GetMapping("/log-for-each-car")
    public ResponseEntity<?> getLogsForEachCar() {
        List<CarRepairLogResponseDTO> logs = carRepairLogService.getLatestRepairLogForEachCar();

        if (logs == null || logs.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Bilgi bulunamadı.");
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(logs);
    }

    @GetMapping("/log-for-each-car-customer-task-filter")
    public ResponseEntity<?> getLatestRepairLogForEachCarFiltered(
            @RequestParam(required = false) String customerFullName,
            @RequestParam(required = false) String taskStatusName
    ) {
        List<CarRepairLogResponseDTO> logs = carRepairLogService.getLatestRepairLogForEachCarFiltered(
                customerFullName,
                taskStatusName
        );

        if (logs == null || logs.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Bilgi bulunamadı.");
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(logs);
    }

    @GetMapping("/latest-assigned/{userId}")
    public ResponseEntity<?> getLastLogForEachCarAssignedToUser(@PathVariable String userId) {
        List<CarRepairLogResponseDTO> logs = carRepairLogService.getLatestRepairLogForEachCarAssignedToUser(userId);
        System.out.println("Received userId: " + userId);
        if (logs == null || logs.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Bilgi bulunamadı.");
        }

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

    @GetMapping("/task-status-count")
    public ResponseEntity<?> getCountCarsByLatestTaskStatus() {
        List<TaskStatusCountDTO> log = carRepairLogService.getCountCarsByLatestTaskStatus();

        if (log == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Kaydedilmiş bir bilgi bulamadım.");
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(log);
    }

    @GetMapping("/latest-by-task-status-name/{taskStatusName}")
    public ResponseEntity<?> getLatestLogsByTaskName(@PathVariable String taskStatusName) {
        List<CarRepairLogResponseDTO> logs = carRepairLogService.getLatestLogsByTaskStatusName(taskStatusName);

        if (logs == null || logs.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Görev durum adı için onarım kaydı bulunamadı: " + taskStatusName);
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(logs);
    }

    @PostMapping("/latest-by-tasks-status-name-and-userid")
    public ResponseEntity<?> getLogsByStatusNamesAndUserId(
            @RequestBody TaskStatusUserRequestDTO request) {

        if (request.getTaskStatusNames() == null || request.getTaskStatusNames().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("geçersiz istek.");
        }

        if (request.getAssignedUserId() == null || request.getAssignedUserId().isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("geçersiz istek.");
        }

        List<CarRepairLogResponseDTO> logs =
                carRepairLogService.getLatestCarRepairLogsByTaskStatusNamesAndAssignedUserId(
                        request.getTaskStatusNames(),
                        request.getAssignedUserId()
                );

        if (logs == null || logs.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("kaydı bulunamadı.");
        }

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(logs);
    }



    @GetMapping("/task-status-name")
    public ResponseEntity<?> getLogsByTaskStatusName(@RequestParam String taskStatusName) {
        System.out.println("Received taskStatusName: " + taskStatusName);
        List<CarRepairLogResponseDTO> data = carRepairLogService.getLogsByTaskStatusName(taskStatusName);
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/invoice-filter-by-date")
    public ResponseEntity<?> getFilteredCarRepairLogs(@RequestBody FilterRequestDTO filterRequest) {
        try {
            // تبدیل رشته‌های ISO به Instant (UTC)
            Instant start = filterRequest.getStartDate();
            Instant end = filterRequest.getEndDate();

            List<CarRepairLogResponseDTO> result = carRepairLogService.getCarRepairLogsByTaskNamesAndDateRange(
                    filterRequest.getTaskStatusNames(),
                    start,
                    end
            );

            if (result == null || result.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("kaydı bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(result);

        } catch (DateTimeParseException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Geçersiz tarih formatı. ISO-8601 bekleniyor.");
        }
    }


    @PostMapping("/invoice-filter-by-licens-plate")
    public ResponseEntity<?> getCarRepairLogsByLicensePlateAndTaskNames(
            @RequestBody FilterRequestDTO filterRequest) {

        List<CarRepairLogResponseDTO> result = carRepairLogService.getCarRepairLogsByLicensePlateAndTaskNames(
                filterRequest.getLicensePlate(),
                filterRequest.getTaskStatusNames()
        );

        if (result == null || result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Kaydı bulunamadı.");
        }
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(result);
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

        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(updated);
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
