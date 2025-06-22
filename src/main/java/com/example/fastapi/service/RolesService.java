package com.example.fastapi.service;

import com.example.fastapi.dboModel.Roles;
import com.example.fastapi.repository.RolesRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesService {
    private final RolesRepository rolesRepository;

    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public Optional<Roles> getByRoleName(String roleName) {
        return rolesRepository.findByRoleName(roleName);
    }

    public List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }
}
