package com.example.fastapi.controller;

import com.example.fastapi.dboModel.Roles;
import com.example.fastapi.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Roles> getAllRoles() {
        return rolesService.getAllRoles();
    }


}