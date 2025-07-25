package com.example.fastapi.controller;

import com.example.fastapi.config.ContextHolder;
import com.example.fastapi.dboModel.Setting;
import com.example.fastapi.dto.SettingStatusDTO;
import com.example.fastapi.service.JwtService;
import com.example.fastapi.service.SettingService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:xxxx", "https://*.netlify.app"})
@RestController
@RequestMapping("/settings")
public class SettingController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private JwtService jwtService;

    private ResponseEntity<Object> unauthorizedResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("Content-Type", "application/json; charset=UTF-8")
                .body("Unauthorized");
    }

    private ResponseEntity<Object> invalidTokenResponse() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("Content-Type", "application/json; charset=UTF-8")
                .body("Invalid or expired token");
    }

    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }

    @PostMapping
    public ResponseEntity<?> createSetting(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Setting setting) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Setting saved = settingService.saveSetting(setting);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(saved);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSettingById(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<Setting> setting = settingService.getSettingById(new ObjectId(id));
            if (setting.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Setting not found with id: " + id);
            }
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(setting.get());
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/store/{storeName}")
    public ResponseEntity<?> getSettingByStoreName(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String storeName) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<Setting> setting = settingService.getSettingByStoreName(storeName);
            if (setting.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Setting not found for store: " + storeName);
            }
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(setting.get());
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/status/{storeName}")
    public ResponseEntity<?> getSettingStatus(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String storeName) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<SettingStatusDTO> status = settingService.getSettingStatusByStoreName(storeName);
            if (status.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Setting not found for store: " + storeName);
            }
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(status.get());
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllSettings(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            List<Setting> settings = settingService.getAllSettings();
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(settings);
        } finally {
            ContextHolder.clear();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSetting(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id,
            @RequestBody Setting setting) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            Optional<Setting> existing = settingService.getSettingById(new ObjectId(id));
            if (existing.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Setting not found with id: " + id);
            }

            setting.setId(new ObjectId(id));
            Setting updated = settingService.saveSetting(setting);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(updated);
        } finally {
            ContextHolder.clear();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSetting(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            settingService.deleteSettingById(new ObjectId(id));
            boolean deleted = true;
            if (deleted) {
                return ResponseEntity.ok()
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Setting successfully deleted");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Setting not found");
            }
        } finally {
            ContextHolder.clear();
        }
    }
}
