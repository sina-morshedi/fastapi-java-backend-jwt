package com.example.fastapi.controller;

import com.example.fastapi.dto.RolesDTO;
import com.example.fastapi.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    private RolesService userRoleService;

    @GetMapping("/")
    public Optional<RolesDTO> getByRoleName(@RequestParam String roleName) {
        return userRoleService.getByRoleName(roleName);
    }

    @PostMapping("/")
    public ResponseEntity<?> addRole(@RequestBody RolesDTO rolesDTO) {
        // چک کردن اینکه نقش با این نام قبلا وجود نداشته باشد
        Optional<RolesDTO> existing = userRoleService.getByRoleName(rolesDTO.getRoleName());
        if (existing.isPresent()) {
            return ResponseEntity.badRequest()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body("Role with the same name already exists.");
        }

        RolesDTO savedRole = userRoleService.saveRole(rolesDTO);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(savedRole);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRoles() {
        List<RolesDTO> roles = userRoleService.getAllRoles();
        return ResponseEntity.ok()
                .header("Content-Type", "application/json; charset=UTF-8")
                .body(roles);
    }

    @PutMapping("/updateRole/{id}")
    public ResponseEntity<RolesDTO> updateRole(
            @PathVariable String id,
            @RequestBody RolesDTO updatedRoleDTO) {

        Optional<RolesDTO> updated = userRoleService.updateRole(id, updatedRoleDTO);

        if (updated.isPresent()) {
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(updated.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteRole/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable String id) {
        boolean deleted = userRoleService.deleteRole(id);
        if (deleted) {
            return ResponseEntity.ok().body("Role deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
