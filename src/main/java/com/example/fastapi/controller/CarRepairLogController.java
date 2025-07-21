package com.example.fastapi.controller;

import com.example.fastapi.config.ContextHolder;
import com.example.fastapi.dto.*;
import com.example.fastapi.service.CarRepairLogService;
import com.example.fastapi.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.time.format.DateTimeParseException;

@CrossOrigin(origins = {"http://localhost:xxxx", "https://*.netlify.app"})
@RestController
@RequestMapping("/car-repair-log")
public class CarRepairLogController {

    @Autowired
    private CarRepairLogService carRepairLogService;

    @Autowired
    private JwtService jwtService;

    private ResponseEntity<Object> unauthorizedResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

    private ResponseEntity<Object> invalidTokenResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllLogs(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<CarRepairLogResponseDTO> logs = carRepairLogService.getAllLogs();
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(logs);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<?> getLogById(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            CarRepairLogResponseDTO log = carRepairLogService.getLogById(id);

            if (log == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("bilgi bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(log);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/by-license-plate/{licensePlate}")
    public ResponseEntity<?> getLogsByLicensePlate(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String licensePlate) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<CarRepairLogResponseDTO> logs = carRepairLogService.getLogsByLicensePlate(licensePlate);

            if (logs == null || logs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body(licensePlate + " numaralı plakaya ait bilgi bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(logs);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/log-for-each-car")
    public ResponseEntity<?> getLogsForEachCar(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<CarRepairLogResponseDTO> logs = carRepairLogService.getLatestRepairLogForEachCar();

            if (logs == null || logs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Bilgi bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(logs);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/log-for-each-car-customer-task-filter")
    public ResponseEntity<?> getLatestRepairLogForEachCarFiltered(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam(required = false) String customerFullName,
            @RequestParam(required = false) String taskStatusName) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<CarRepairLogResponseDTO> logs = carRepairLogService.getLatestRepairLogForEachCarFiltered(customerFullName, taskStatusName);

            if (logs == null || logs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Bilgi bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(logs);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/latest-assigned/{userId}")
    public ResponseEntity<?> getLastLogForEachCarAssignedToUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String userId) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<CarRepairLogResponseDTO> logs = carRepairLogService.getLatestRepairLogForEachCarAssignedToUser(userId);
            if (logs == null || logs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Bilgi bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(logs);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/latest-by-license-plate/{licensePlate}")
    public ResponseEntity<?> getLatestLogsByLicensePlate(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String licensePlate) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            CarRepairLogResponseDTO log = carRepairLogService.getLatestLogsByLicensePlate(licensePlate);

            if (log == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Plaka için onarım kaydı bulunamadı: " + licensePlate);
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(log);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/task-status-count")
    public ResponseEntity<?> getCountCarsByLatestTaskStatus(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<TaskStatusCountDTO> log = carRepairLogService.getCountCarsByLatestTaskStatus();

            if (log == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Kaydedilmiş bir bilgi bulamadım.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(log);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/latest-by-task-status-name/{taskStatusName}")
    public ResponseEntity<?> getLatestLogsByTaskName(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String taskStatusName) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<CarRepairLogResponseDTO> logs = carRepairLogService.getLatestLogsByTaskStatusName(taskStatusName);

            if (logs == null || logs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Görev durum adı için onarım kaydı bulunamadı: " + taskStatusName);
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(logs);
        } finally {
            ContextHolder.clear();
        }
    }

    @PostMapping("/latest-by-tasks-status-name-and-userid")
    public ResponseEntity<?> getLogsByStatusNamesAndUserId(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody TaskStatusUserRequestDTO request) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            if (request.getTaskStatusNames() == null || request.getTaskStatusNames().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("geçersiz istek.");
            }

            if (request.getAssignedUserId() == null || request.getAssignedUserId().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("geçersiz istek.");
            }

            List<CarRepairLogResponseDTO> logs =
                    carRepairLogService.getLatestCarRepairLogsByTaskStatusNamesAndAssignedUserId(
                            request.getTaskStatusNames(),
                            request.getAssignedUserId()
                    );

            if (logs == null || logs.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("kaydı bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(logs);

        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/task-status-name")
    public ResponseEntity<?> getLogsByTaskStatusName(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam String taskStatusName) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<CarRepairLogResponseDTO> data = carRepairLogService.getLogsByTaskStatusName(taskStatusName);
            return ResponseEntity.ok().body(data);
        } finally {
            ContextHolder.clear();
        }
    }

    @PostMapping("/invoice-filter-by-date")
    public ResponseEntity<?> getFilteredCarRepairLogs(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody FilterRequestDTO filterRequest) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<CarRepairLogResponseDTO> result = carRepairLogService.getCarRepairLogsByTaskNamesAndDateRange(
                    filterRequest.getTaskStatusNames(),
                    filterRequest.getStartDate(),
                    filterRequest.getEndDate()
            );

            if (result == null || result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("kaydı bulunamadı.");
            }

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(result);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .body("Geçersiz tarih formatı. ISO-8601 bekleniyor.");
        } finally {
            ContextHolder.clear();
        }
    }

    @PostMapping("/invoice-filter-by-licens-plate")
    public ResponseEntity<?> getCarRepairLogsByLicensePlateAndTaskNames(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody FilterRequestDTO filterRequest) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
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
        } finally {
            ContextHolder.clear();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createLog(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody CarRepairLogRequestDTO requestDTO) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            CarRepairLogResponseDTO createdLog = carRepairLogService.createLog(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(createdLog);
        } finally {
            ContextHolder.clear();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLog(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id,
            @RequestBody CarRepairLogRequestDTO requestDTO) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<CarRepairLogResponseDTO> updated = carRepairLogService.updateLog(id, requestDTO);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(updated);
        } finally {
            ContextHolder.clear();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLog(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
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
        } finally {
            ContextHolder.clear();
        }
    }
}
