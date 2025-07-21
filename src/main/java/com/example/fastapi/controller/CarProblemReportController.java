package com.example.fastapi.controller;

import com.example.fastapi.config.ContextHolder;
import com.example.fastapi.dboModel.CarProblemReport;
import com.example.fastapi.service.CarProblemReportService;
import com.example.fastapi.service.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:xxxx", "https://*.netlify.app"})
@RestController
@RequestMapping("/car-problem-report")
public class CarProblemReportController {

    @Autowired
    private CarProblemReportService reportService;

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

    @PostMapping("/create")
    public ResponseEntity<?> createReport(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody CarProblemReport report) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            CarProblemReport created = reportService.createReport(report);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(created);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllReports(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<CarProblemReport> reports = reportService.getAllReports();
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(reports);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReportById(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<CarProblemReport> report = reportService.getReportById(id);
            if (report.isPresent()) {
                return ResponseEntity.ok()
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body(report.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Rapor bulunamadı");
            }
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/by-car/{carId}")
    public ResponseEntity<?> getReportsByCarId(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String carId) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<CarProblemReport> reports = reportService.getReportsByCarId(carId);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(reports);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<?> getReportsByCreatorUserId(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String userId) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<CarProblemReport> reports = reportService.getReportsByCreatorUserId(userId);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(reports);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/by-license-plate/{licensePlate}")
    public ResponseEntity<?> getReportsByLicensePlate(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String licensePlate) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<CarProblemReport> reports = reportService.getReportsByLicensePlate(licensePlate);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(reports);
        } finally {
            ContextHolder.clear();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReport(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id,
            @RequestBody CarProblemReport updatedReport) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<CarProblemReport> updated = reportService.updateReport(id, updatedReport);
            return updated
                    .map(r -> ResponseEntity.ok("Rapor başarıyla güncellendi"))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .header("Content-Type", "application/json; charset=UTF-8")
                            .body("Rapor bulunamadı"));
        } finally {
            ContextHolder.clear();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReport(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            boolean deleted = reportService.deleteReport(id);
            if (deleted) {
                return ResponseEntity.ok()
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Rapor başarıyla silindi");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Rapor bulunamadı");
            }
        } finally {
            ContextHolder.clear();
        }
    }
}
