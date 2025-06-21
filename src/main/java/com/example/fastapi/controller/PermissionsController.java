package com.example.fastapi.controller;

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
    public List<Permissions> getAllPermissions() {
        return permissionsService.getAllPermissions();
    }


}