package com.example.fastapi.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.fastapi.service.UsersService;
import com.example.fastapi.dboModel.Users;
import com.example.fastapi.dboModel.UserPass;
import com.example.fastapi.dto.UserProfileDTO;
import com.example.fastapi.dto.UpdateUserDTO;
import com.example.fastapi.dto.RegisterDTO;

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


    @GetMapping("/")
    public Optional<Users> getByName(@RequestParam String firstName) {
        Optional<Users> users = userService.getByFirstName(firstName);
        return users;
    }

    // Controller
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<UserProfileDTO> userProfiles = userService.getAllUsersWithUsernames();
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(userProfiles);
    }

    @GetMapping("/count")
    public ResponseEntity<?> countAllUsers() {
        long count = userService.countAllUser();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(response);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        boolean success = userService.userLogin(username,password);
        UserProfileDTO profile = userService.getUserProfile(username, password);
        if (success) {

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(profile);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Geçersiz kullanıcı adı veya şifre");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UpdateUserDTO dto) {
        try {
            boolean success = userService.updateUser(id, dto);
            if(success) {
                return ResponseEntity.ok("Kullanıcı başarıyla güncellendi");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Güncelleme başarısız oldu");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hata: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        boolean success = userService.deleteUserById(id);
        if (success) {
            return ResponseEntity.ok("Kullanıcı başarıyla silindi.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Kullanıcı silinemedi.");
        }
    }

}

