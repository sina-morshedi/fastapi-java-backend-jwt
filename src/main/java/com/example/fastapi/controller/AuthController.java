package com.example.fastapi.controller;

import com.example.fastapi.config.ContextHolder;
import com.example.fastapi.dto.RegisterDTO;
import com.example.fastapi.service.JwtService;
import com.example.fastapi.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:xxxx","https://*.netlify.app"})
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsersService userService;
    private final JwtService jwtService;

    @Autowired
    public AuthController(UsersService userService, JwtService jwtService) {
        this.userService = userService;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody RegisterDTO dto) {

        String token = extractToken(authHeader);
        if (token == null) return unauthorizedResponse();
        if (!jwtService.validateToken(token)) return invalidTokenResponse();

        ContextHolder.setStoreName(jwtService.getStoreNameFromToken(token));

        try {
            boolean success = userService.registerUser(dto);
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Kullanıcı başarıyla kaydedildi");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body("Kullanıcı adı zaten mevcut");
            }
        } finally {
            ContextHolder.clear();
        }
    }
}
