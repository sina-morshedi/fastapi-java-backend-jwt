package com.example.fastapi.controller;

import com.example.fastapi.dto.RolesDTO;
import com.example.fastapi.config.ContextHolder;
import com.example.fastapi.service.JwtService;
import com.example.fastapi.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    private RolesService userRoleService;

    @Autowired
    private JwtService jwtService;

    private boolean isTokenInvalid(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ") || !jwtService.validateToken(authHeader.substring(7));
    }

    private boolean prepareContext(String authHeader) {
        if (isTokenInvalid(authHeader)) {
            return false;
        }
        String token = authHeader.substring(7);
        String storeName = jwtService.getStoreNameFromToken(token);
        ContextHolder.setStoreName(storeName);
        return true;
    }

    @GetMapping("/")
    public ResponseEntity<?> getByRoleName(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestParam String roleName) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
            Optional<RolesDTO> role = userRoleService.getByRoleName(roleName);
            return ResponseEntity.ok().body(role);
        } finally {
            ContextHolder.clear();
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> addRole(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody RolesDTO rolesDTO) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
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
        } finally {
            ContextHolder.clear();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllRoles(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
            List<RolesDTO> roles = userRoleService.getAllRoles();
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .body(roles);
        } finally {
            ContextHolder.clear();
        }
    }

    @PutMapping("/updateRole/{id}")
    public ResponseEntity<?> updateRole(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id,
            @RequestBody RolesDTO updatedRoleDTO) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
            Optional<RolesDTO> updated = userRoleService.updateRole(id, updatedRoleDTO);
            if (updated.isPresent()) {
                return ResponseEntity.ok()
                        .header("Content-Type", "application/json; charset=UTF-8")
                        .body(updated.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found.");
            }
        } finally {
            ContextHolder.clear();
        }
    }

    @DeleteMapping("/deleteRole/{id}")
    public ResponseEntity<?> deleteRole(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable String id) {

        if (!prepareContext(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized or invalid token");
        }

        try {
            boolean deleted = userRoleService.deleteRole(id);
            if (deleted) {
                return ResponseEntity.ok().body("Rol başarıyla silindi.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found.");
            }
        } finally {
            ContextHolder.clear();
        }
    }
}
