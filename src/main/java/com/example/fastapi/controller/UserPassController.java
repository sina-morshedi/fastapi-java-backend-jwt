package com.example.fastapi.controller;

import com.example.fastapi.dboModel.UserPass;
import com.example.fastapi.service.UserPassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/login")
public class UserPassController {

    @Autowired
    private UserPassService userPassService;

    @GetMapping("/")
    public Optional<UserPass> getByUsername(@RequestParam String username) {
        Optional<UserPass> userPass = userPassService.getByUsername(username);
        return userPass;
    }
}
