package com.example.fastapi.controller;

import com.example.fastapi.dboModel.UserPass;
import com.example.fastapi.dboModel.Users;
import com.example.fastapi.dto.UpdateUserDTO;
import com.example.fastapi.dto.UserProfileDTO;
import com.example.fastapi.dto.CarRepairLogResponseDTO;
import com.example.fastapi.dto.CarRepairLogRequestDTO;
import com.example.fastapi.service.TestService;
import com.example.fastapi.service.UsersService;
import com.example.fastapi.repository.CarRepairLogCustomRepositoryImpl;
import com.example.fastapi.service.CarRepairLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:xxxx","https://*.netlify.app"})
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;
    private UserPass userPass;

    @GetMapping("/test")
    public ResponseEntity<?> usersTest(@RequestParam String id) {
        List<CarRepairLogResponseDTO> data = testService.testService(id);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(data);
    }

    @PostMapping("/status")
    public ResponseEntity<?> usersFindeByStatus(@RequestBody String request) {
        List<CarRepairLogResponseDTO> data = testService.testService(request);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(data);
    }

}

