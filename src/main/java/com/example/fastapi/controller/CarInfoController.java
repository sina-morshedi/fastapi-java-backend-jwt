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
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(
                    new ApiResponse("successful", savedCar.getId())
            );
        } catch (IllegalArgumentException e) {
            // chassis_no duplicate error
            return ResponseEntity.badRequest()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(
                    new ApiResponse("error", e.getMessage())
            );
        } catch (Exception e) {
            // other errors
            return ResponseEntity.status(500)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(
                    new ApiResponse("error", "Sunucu iç hatası")
            );
        }
    }

    @GetMapping("/getCarInfo/{licensePlate}")
    public ResponseEntity<?> getCarInfoByLicensePlate(@PathVariable String licensePlate) {
        try {
            CarInfo car = carInfoService.getCarByLicensePlate(licensePlate);
            if (car == null) {
                return ResponseEntity.status(404)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body(new ApiResponse("error", "Araç bulunamadı"));
            }
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(car);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(new ApiResponse("error", "Sunucu iç hatası"));
        }
    }

    @PutMapping("/updateCarInfo/{licensePlate}")
    public ResponseEntity<?> updateCarInfoByLicensePlate(@PathVariable String licensePlate, @RequestBody CarInfo updatedCar) {
        try {
            CarInfo car = carInfoService.updateCarInfoByLicensePlate(licensePlate, updatedCar);
            if (car == null) {
                return ResponseEntity.status(404)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body(new ApiResponse("error", "Güncellenecek araç bulunamadı"));
            }
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(new ApiResponse("successful", "Araç başarıyla güncellendi"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(new ApiResponse("error", "Sunucu iç hatası"));
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

