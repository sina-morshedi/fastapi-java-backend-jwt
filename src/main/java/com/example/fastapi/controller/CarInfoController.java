package com.example.fastapi.controller;

import com.example.fastapi.config.ContextHolder;
import com.example.fastapi.dboModel.CarInfo;
import com.example.fastapi.service.CarInfoService;
import com.example.fastapi.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarInfoController {

    private final CarInfoService carInfoService;
    private final JwtService jwtService;

    @Autowired
    public CarInfoController(CarInfoService carInfoService, JwtService jwtService) {
        this.carInfoService = carInfoService;
        this.jwtService = jwtService;
    }

    private ResponseEntity<Object> unauthorizedResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Unauthorized");
    }

    private ResponseEntity<Object> invalidTokenResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid or expired token");
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    @PostMapping("/insertCarInfo")
    public ResponseEntity<?> insertCarInfo(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody CarInfo carInfo) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            CarInfo savedCar = carInfoService.insertCarInfo(carInfo);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(new ApiResponse("successful", savedCar.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(new ApiResponse("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(new ApiResponse("error", "Sunucu iç hatası"));
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/getCarInfo/{licensePlate}")
    public ResponseEntity<?> getCarInfoByLicensePlate(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String licensePlate) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<CarInfo> car = carInfoService.getCarByLicensePlate(licensePlate);
            if (!car.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body(new ApiResponse("error", "Araç bulunamadı"));
            }
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(car.get());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(new ApiResponse("error", "Sunucu iç hatası"));
        } finally {
            ContextHolder.clear();
        }
    }

    @PutMapping("/updateCarInfo/{licensePlate}")
    public ResponseEntity<?> updateCarInfoByLicensePlate(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String licensePlate,
            @RequestBody CarInfo updatedCar) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            CarInfo car = carInfoService.updateCarInfoByLicensePlate(licensePlate, updatedCar);
            if (car == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
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
        } finally {
            ContextHolder.clear();
        }
    }

    // Internal helper class for standard JSON response
    static class ApiResponse {
        private final String status;
        private final String idOrMessage;

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
