package com.example.fastapi.controller;

import com.example.fastapi.config.ContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.fastapi.service.UsersService;
import com.example.fastapi.dboModel.Users;
import com.example.fastapi.dboModel.Setting;
import com.example.fastapi.dboModel.UserPass;
import com.example.fastapi.dto.UserProfileDTO;
import com.example.fastapi.dto.UpdateUserDTO;
import com.example.fastapi.service.JwtService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;


import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@CrossOrigin(origins = {"http://localhost:xxxx","https://*.netlify.app"})
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService userService;
    private UserPass userPass;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private MongoTemplate centralMongoTemplate;


    public UsersController(JwtService jwtService, UsersService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getByName(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam String firstName) {

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
            Optional<Users> users = userService.getByFirstName(firstName);
            return ResponseEntity.ok().body(users);
        } finally {
            ContextHolder.clear();
        }
    }


    // Controller
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "Authorization", required = false) String authHeader) {
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
            List<UserProfileDTO> userProfiles = userService.getAllUsersWithUsernames();

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(userProfiles);
        } finally {
            ContextHolder.clear();
        }
    }



    @GetMapping("/count")
    public ResponseEntity<?> countAllUsers(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String token = authHeader.substring(7);

        if (!jwtService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        String storeName = jwtService.getStoreNameFromToken(token);

        ContextHolder.setStoreName(storeName);
        long count = userService.countAllUser();
        ContextHolder.clear();

        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam String storeName) {

        ContextHolder.setStoreName(storeName);

        boolean success = userService.userLogin(username, password);

        if (!success) {
            ContextHolder.clear();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz kullanıcı adı veya şifre");
        }

        // گرفتن تنظیمات شعبه از دیتابیس مرکزی
        Query query = Query.query(Criteria.where("storeName").is(storeName));
        Setting setting = centralMongoTemplate.findOne(query, Setting.class, "settings");

        if (setting == null) {
            ContextHolder.clear();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mağaza bulunamadı");
        }

        UserProfileDTO profile = userService.getUserProfile(username, password);
        String token = jwtService.generateToken(
                username,
                storeName,
                profile.getRole().getRoleName(),
                setting.isInventoryEnabled(),
                setting.isCustomerEnabled());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("profile", profile);
        ContextHolder.clear();
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(responseBody);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id,
            @RequestBody UpdateUserDTO dto) {

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
            boolean success = userService.updateUser(id, dto);
            if (success) {
                return ResponseEntity.ok("Kullanıcı başarıyla güncellendi");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Güncelleme başarısız oldu");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hata: " + e.getMessage());
        } finally {
            ContextHolder.clear();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

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
            boolean success = userService.deleteUserById(id);
            if (success) {
                return ResponseEntity.ok("Kullanıcı başarıyla silindi.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Kullanıcı silinemedi.");
            }
        } finally {
            ContextHolder.clear();
        }
    }

}

