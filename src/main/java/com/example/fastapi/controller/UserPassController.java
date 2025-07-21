package com.example.fastapi.controller;

import com.example.fastapi.config.ContextHolder;
import com.example.fastapi.dboModel.UserPass;
import com.example.fastapi.service.JwtService;
import com.example.fastapi.service.UserPassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/login")
public class UserPassController {

    @Autowired
    private UserPassService userPassService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/")
    public ResponseEntity<?> getByUsername(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam String username) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String token = authHeader.substring(7);

        if (!jwtService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        String storeName = jwtService.getStoreNameFromToken(token);
        ContextHolder.setStoreName(storeName);

        try {
            Optional<UserPass> userPass = userPassService.getByUsername(username);
            return ResponseEntity.ok(userPass);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        } finally {
            ContextHolder.clear();
        }
    }
}
