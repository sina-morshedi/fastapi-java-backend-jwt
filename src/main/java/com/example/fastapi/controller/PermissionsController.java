package com.example.fastapi.controller;

import com.example.fastapi.dboModel.Roles;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;


import com.example.fastapi.service.PermissionsService;
import com.example.fastapi.dboModel.Permissions;
import java.util.Optional;

@RestController
@RequestMapping("/permissions")
public class PermissionsController {

    @Autowired
    private PermissionsService permissionsService;

    @GetMapping("/")
    public Optional<Permissions> getByName(@RequestParam String permissionName) {
        return permissionsService.getByPermissionName(permissionName);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPermissions() {
        List<Permissions> users = permissionsService.getAllPermissions();
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(users);
        //TODO Return the appropriate error.
    }


}