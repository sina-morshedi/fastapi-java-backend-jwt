package com.example.fastapi.controller;


import com.example.fastapi.dto.RegisterDTO;
import com.example.fastapi.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:xxxx","https://*.netlify.app"})
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsersService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto) {
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
    }
}
