package com.example.fastapi.service;

import com.example.fastapi.repository.RolesRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.fastapi.dto.RolesDTO;
import com.example.fastapi.dboModel.Roles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RolesService {

    @Autowired
    private RolesRepository userRoleRepository;

    // تبدیل مدل به DTO
    private RolesDTO convertToDTO(Roles role) {
        return new RolesDTO(role.getId(), role.getRoleName());
    }

    public Optional<RolesDTO> getByRoleName(String roleName) {
        return userRoleRepository.findByRoleName(roleName)
                .map(this::convertToDTO);
    }

    public List<RolesDTO> getAllRoles() {
        return userRoleRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RolesDTO saveRole(RolesDTO dto) {
        Roles entity = new Roles();
        entity.setRoleName(dto.getRoleName());
        Roles saved = userRoleRepository.save(entity);
        return convertToDTO(saved);
    }

    public Optional<RolesDTO> updateRole(String id, RolesDTO dto) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return Optional.empty(); // اگر فرمت اشتباه بود
        }

        Optional<Roles> existing = userRoleRepository.findById(objectId);
        if (existing.isPresent()) {
            Roles entity = existing.get();
            entity.setRoleName(dto.getRoleName());
            Roles saved = userRoleRepository.save(entity);
            return Optional.of(convertToDTO(saved));
        } else {
            return Optional.empty();
        }
    }

    public boolean deleteRole(String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            if (userRoleRepository.existsById(objectId)) {
                userRoleRepository.deleteById(objectId);
                return true;
            } else {
                return false;
            }
        } catch (IllegalArgumentException e) {
            return false; // Invalid ID format
        }
    }
}
