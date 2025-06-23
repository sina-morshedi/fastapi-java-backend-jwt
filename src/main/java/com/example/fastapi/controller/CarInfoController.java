package com.example.fastapi.controller;

import com.example.fastapi.dto.CarInfo;
import com.example.fastapi.service.CarInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cars")  // می‌تونی هر prefix دلخواه بزاری
public class CarInfoController {

    private final CarInfoService carInfoService;

    @Autowired
    public CarInfoController(CarInfoService carInfoService) {
        this.carInfoService = carInfoService;
    }

    @PostMapping("/insertCarInfo")
    public ResponseEntity<?> insertCarInfo(@RequestBody CarInfo carInfo) {
        try {
            CarInfo savedCar = carInfoService.insertCarInfo(carInfo);
            return ResponseEntity.ok().body(
                    new ApiResponse("successful", savedCar.getId())
            );
        } catch (IllegalArgumentException e) {
            // chassis_no duplicate error
            return ResponseEntity.badRequest().body(
                    new ApiResponse("error", "Bu şasi numarasına sahip araç zaten mevcut")
            );
        } catch (Exception e) {
            // other errors
            return ResponseEntity.status(500).body(
                    new ApiResponse("error", "Sunucu iç hatası")
            );
        }
    }

    // Internal helper class for standard JSON response
    static class ApiResponse {
        private String status;
        private String idOrMessage;

        public ApiResponse(String status, String idOrMessage) {
            this.status = status;
            this.idOrMessage = idOrMessage;
        }

        public String getStatus() {
            return status;
        }

        public String getIdOrMessage() {
            return idOrMessage;
        }
    }
}

