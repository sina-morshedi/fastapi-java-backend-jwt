package com.example.fastapi.controller;

import com.example.fastapi.dboModel.Roles;
import com.example.fastapi.dto.UserProfileDTO;
import com.example.fastapi.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    private RolesService rolesService;

    @GetMapping("/")
    public Optional<Roles> getByName(@RequestParam String roleName) {
        return rolesService.getByRoleName(roleName);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRoles() {
        List<Roles> users = rolesService.getAllRoles();
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(users);
        //TODO Return the appropriate error.
    }


}