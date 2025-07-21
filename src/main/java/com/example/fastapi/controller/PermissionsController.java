package com.example.fastapi.controller;

import com.example.fastapi.config.ContextHolder;
import com.example.fastapi.dboModel.Permissions;
import com.example.fastapi.service.JwtService;
import com.example.fastapi.service.PermissionsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/permissions")
public class PermissionsController {

    @Autowired
    private PermissionsService permissionsService;

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

    @GetMapping("/")
    public ResponseEntity<?> getByName(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam String permissionName) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
            Optional<Permissions> permission = permissionsService.getByPermissionName(permissionName);
            return ResponseEntity.ok().body(permission);
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPermissions(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
            List<Permissions> permissions = permissionsService.getAllPermissions();
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(permissions);
        } finally {
            ContextHolder.clear();
        }
    }
}
