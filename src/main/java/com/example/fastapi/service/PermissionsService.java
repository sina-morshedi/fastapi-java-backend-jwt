package com.example.fastapi.service;

import com.example.fastapi.dboModel.Permissions;
import com.example.fastapi.repository.PermissionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PermissionsService {
    private final PermissionsRepository permissionsRepository;

    public PermissionsService(PermissionsRepository permissionsRepository) {
        this.permissionsRepository = permissionsRepository;
    }

    public List<Permissions> getByPermissionName(String permissionName) {
        return permissionsRepository.findByPermissionName(permissionName);
    }

//    public Users saveUser(Users user) {
//        return userRepository.save(user);
//    }

}
