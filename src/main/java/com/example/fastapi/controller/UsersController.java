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

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;




@CrossOrigin(origins = {"http://localhost:xxxx","https://*.netlify.app"})
@RestController
@RequestMapping("/users")
public class UsersController {

    public class LoginRequest {
        private String username;
        private String password;

        // getters and setters
    }



    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UsersService userService;
    private UserPass userPass;

    @GetMapping("/")
    public Optional<Users> getByName(@RequestParam String firstName) {
        Optional<Users> users = userService.getByFirstName(firstName);
        return users;
    }

    @GetMapping("/all")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }



    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
//        userService.userLogin(username,password);
        boolean success = userService.userLogin(username,password);
        UserProfileDTO profile = userService.getUserProfile(username, password);
        if (success) {

            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(profile);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Invalid username or password");
        }
    }


//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody Users newUser) {
//        try {
//            userService.register(newUser);
//            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
//        } catch (Exception e) {
//            logger.error("Error in register: ", e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
//        }
//    }

    // آپدیت کاربر
//    @PutMapping("/update/{id}")
//    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody Users updatedUser) {
//        try {
//            userService.updateUser(id, updatedUser);
//            return ResponseEntity.ok("User updated successfully");
//        } catch (Exception e) {
//            logger.error("Error in updateUser: ", e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update failed: " + e.getMessage());
//        }
//    }


//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteUser(@PathVariable String id) {
//        try {
//            userService.deleteUser(id);
//            return ResponseEntity.ok("User deleted successfully");
//        } catch (Exception e) {
//            logger.error("Error in deleteUser: ", e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delete failed: " + e.getMessage());
//        }
//    }
}
