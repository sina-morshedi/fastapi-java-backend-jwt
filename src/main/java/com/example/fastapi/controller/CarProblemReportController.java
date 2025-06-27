package com.example.fastapi.controller;

import com.example.fastapi.dboModel.CarProblemReport;
import com.example.fastapi.service.CarProblemReportService;

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

    @PostMapping("/create")
    public ResponseEntity<?> createReport(@RequestBody CarProblemReport report) {
        CarProblemReport created = reportService.createReport(report);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(created);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CarProblemReport>> getAllReports() {
        List<CarProblemReport> reports = reportService.getAllReports();
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReportById(@PathVariable String id) {
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
    }

    @GetMapping("/by-car/{carId}")
    public ResponseEntity<List<CarProblemReport>> getReportsByCarId(@PathVariable String carId) {
        List<CarProblemReport> reports = reportService.getReportsByCarId(carId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(reports);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<CarProblemReport>> getReportsByCreatorUserId(@PathVariable String userId) {
        List<CarProblemReport> reports = reportService.getReportsByCreatorUserId(userId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(reports);
    }

    @GetMapping("/by-license-plate/{licensePlate}")
    public ResponseEntity<List<CarProblemReport>> getReportsByLicensePlate(@PathVariable String licensePlate) {
        List<CarProblemReport> reports = reportService.getReportsByLicensePlate(licensePlate);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(reports);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReport(@PathVariable String id, @RequestBody CarProblemReport updatedReport) {
        Optional<CarProblemReport> updated = reportService.updateReport(id, updatedReport);
        return updated
                .map(r -> ResponseEntity.ok("Rapor başarıyla güncellendi"))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Rapor bulunamadı"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable String id) {
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
    }
}
